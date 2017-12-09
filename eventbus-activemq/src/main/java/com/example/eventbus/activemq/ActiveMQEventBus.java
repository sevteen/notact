package com.example.eventbus.activemq;

import com.example.model.EventBus;
import com.example.model.event.OperationCompleted;
import com.example.model.event.OperationStarted;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;
import java.lang.IllegalStateException;

/**
 * @author Beka Tsotsoria
 */
public class ActiveMQEventBus implements EventBus {

    private final Logger log = LoggerFactory.getLogger(ActiveMQEventBus.class);

    private String address;
    private String startedTopic;
    private String completedTopic;

    private Connection connection;
    private Session session;

    public ActiveMQEventBus(String address, String startedTopic, String completedTopic) {
        this.address = address;
        this.startedTopic = startedTopic;
        this.completedTopic = completedTopic;
    }

    public void open() {
        try {
            connection = new ActiveMQConnectionFactory(address).createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            throw new IllegalStateException("Failed to initiate connection with ActiveMQ", e);
        }
    }

    @Override
    public void publish(OperationStarted operationStarted) {
        doPublish(startedTopic, operationStarted);
    }

    @Override
    public void publish(OperationCompleted operationCompleted) {
        doPublish(completedTopic, operationCompleted);
    }

    private void doPublish(String topicName, Serializable object) {
        try {
            Topic topic = session.createTopic(topicName);
            ObjectMessage msg = new ActiveMQObjectMessage();
            msg.setObject(object);
            session.createProducer(topic).send(msg);
        } catch (JMSException e) {
            throw new IllegalStateException("Failed to publish " + object, e);
        }
    }

    public void close() {
        try {
            session.close();
            connection.close();
        } catch (JMSException e) {
            log.warn("Failed to gracefully close ActiveMQ connection", e);
        }
    }
}
