import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;

public class SSL_JMS
{
        public static void main(String[] args) throws NamingException, JMSException
        {
           // System.setProperty("javax.net.debug","ssl,handshake");

            //Create Context
            Context context = new InitialContext();

            //JNDI Lookup -> ConnectionFactory
            JmsConnectionFactory connectionFactory = (JmsConnectionFactory) context.lookup("ConnectionFactory");

            //Part of your code --> For this add dependency in pom.xml <qpid-jms-client>
           /* Map<String,String> props = new HashMap<String,String>();
            props.put("qpid.sasl_mechs", "EXTERNAL");
            connectionFactory.setProperties(props);*/

            //Create connection
            Connection jmsConn = connectionFactory.createConnection();
            jmsConn.start();

            //Create Session
            Session session = jmsConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination queue = session.createQueue("Test1");
            MessageProducer producer = session.createProducer(queue);

            TextMessage message = session.createTextMessage("Hello");

            producer.send(message, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, 600000);
            System.out.println("Message :" + message.getText() + "");

            System.out.println("Done!");

        }


}
