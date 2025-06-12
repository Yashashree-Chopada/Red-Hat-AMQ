package org.example;

import javax.jms.*;
import javax.naming.*;


public class App {
    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext initialContext = null;
        JMSContext jmsContext = null;

        // Obtain the InitialContext with proper JNDI properties
        initialContext = new InitialContext();

        // Lookup the ConnectionFactory from the JNDI context
        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");

        // Create JMS context
        jmsContext = connectionFactory.createContext("admin", "admin");

        // Create Topic and send a message to topic
        Topic topic = jmsContext.createTopic("testAddress::client1.testQueue1");
        JMSProducer producer = jmsContext.createProducer();
        producer.send(topic, "testMsg");
        System.out.println("Message sent to topic");

    }
}
