import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer_JMS
{
    public static void main(String[] args) throws JMSException {

           // final CountDownLatch countDownLatch = new CountDownLatch(1);
            //String url ="(tcp://localhost:61617,tcp://localhost:61619)?ha=true;useTopologyForLoadBalancing=false";
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = activeMQConnectionFactory.createConnection("admin", "admin");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination =  session.createQueue("Test11");
        MessageProducer producer = session.createProducer(destination);

        TextMessage message = session.createTextMessage("Hello");

        producer.send(message, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, 600000);
        System.out.println("Message :" + message.getText() + "");

        System.out.println("Done!");

            session.close();
            connection.close();



    }
}
