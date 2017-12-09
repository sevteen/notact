package com.example.standings;

import com.example.model.event.OperationCompleted;
import com.example.model.event.OperationStarted;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.lang.IllegalStateException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Beka Tsotsoria
 */
public class ActiveMQStandingsProvider implements StandingsProvider {

    private final Logger log = LoggerFactory.getLogger(ActiveMQStandingsProvider.class);

    private ConcurrentHashMap<String, Participant> participants = new ConcurrentHashMap<>();

    private String address;
    private String startedTopic;
    private String completedTopic;

    private Connection connection;
    private Session session;
    private boolean started;

    public ActiveMQStandingsProvider(String address, String startedTopic, String completedTopic) {
        this.address = address;
        this.startedTopic = startedTopic;
        this.completedTopic = completedTopic;
    }

    public void start() {
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(address);
            factory.setTrustAllPackages(true);
            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer startedConsumer = session.createConsumer(session.createTopic(startedTopic));
            MessageConsumer completedConsumer = session.createConsumer(session.createTopic(completedTopic));

            started = true;
            connection.start();

            new Thread(() -> {
                while (started) {
                    try {
                        Message msg = startedConsumer.receive();
                        if (msg instanceof ObjectMessage) {
                            OperationStarted operationStarted = (OperationStarted) ((ObjectMessage) msg).getObject();
                            boolean newParticipant = participants.putIfAbsent(operationStarted.getUser(),
                                new Participant(operationStarted.getUser(), operationStarted.getOperationId(), LocalDateTime.now(), 0)) == null;
                            if (newParticipant) {
                                log.info("New participant \"{}\"", operationStarted.getUser());
                            }
                        }
                    } catch (JMSException e) {
                        if (started) {
                            log.error("Failed to receive message from topic {}", startedTopic, e);
                        }
                    }
                }
            }, "Consumer-" + startedTopic).start();

            new Thread(() -> {
                while (started) {
                    try {
                        Message msg = completedConsumer.receive();
                        if (msg instanceof ObjectMessage) {
                            OperationCompleted operationCompleted = (OperationCompleted) ((ObjectMessage) msg).getObject();
                            Participant participant = participants.get(operationCompleted.getUser());
                            if (participant != null) {
                                if (!participant.getOperationId().equals(operationCompleted.getOperationId())) {
                                    log.info("Participant \"{}\" is now busy with operation \"{}\"", participant.getOperationId());
                                } else {
                                    participant.calculateScore();
                                }
                            } else {
                                log.info("Participant with \"{}\" does not exist", operationCompleted.getUser());
                            }
                        }
                    } catch (JMSException e) {
                        if (started) {
                            log.error("Failed to receive message from topic {}", startedTopic, e);
                        }
                    }
                }
            }, "Consumer-" + completedTopic).start();

        } catch (JMSException e) {
            throw new IllegalStateException("Failed to initiate connection with ActiveMQ", e);
        }
    }

    public void stop() {
        started = false;
        try {
            session.close();
            connection.close();
        } catch (JMSException e) {
            log.warn("Failed to gracefully close ActiveMQ connection", e);
        }
    }

    @Override
    public Standings getStandings() {
        return new Standings(new ArrayList<>(participants.values()));
    }
}
