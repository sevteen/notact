package com.example.standings;

import com.example.model.event.OperationCompleted;
import com.example.model.event.OperationStarted;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.lang.IllegalStateException;

/**
 * @author Beka Tsotsoria
 */
public class ActiveMQStandingsProvider extends AbstractStandingsProvider {

    private final Logger log = LoggerFactory.getLogger(ActiveMQStandingsProvider.class);

    private String address;
    private String startedTopic;
    private String completedTopic;

    private Connection connection;
    private Session session;
    private volatile boolean started;

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
                            onOperation((OperationStarted) ((ObjectMessage) msg).getObject());
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
                            onOperation((OperationCompleted) ((ObjectMessage) msg).getObject());
                        }
                    } catch (JMSException e) {
                        if (started) {
                            log.error("Failed to receive message from topic {}", completedTopic, e);
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
}
