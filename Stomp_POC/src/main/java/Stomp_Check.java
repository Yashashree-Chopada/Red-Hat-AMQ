import javax.jms.*;
import javax.jms.IllegalStateException;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;
import org.fusesource.stomp.jms.StompJmsTopic;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Stomp_Check
{
    public static void main(final String[] args) throws Exception {
        String user = "admin";
        String password = "admin";
        Socket socket = new Socket("localhost", 61616);
        StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
        factory.setDisconnectTimeout(5000);
        factory.setBrokerURI("tcp://localhost:61613");
        Connection connection = factory.createConnection(user,password);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination dest = new StompJmsDestination("queue1");  //queue

        MessageProducer producer = session.createProducer(dest);
        for(int i=0;i<10;i++)
        {
            producer.send(session.createTextMessage("Hello"));
        }

        System.out.println("Messages produced");

        connection.start();
        //Consumer

        MessageConsumer consumer = session.createConsumer(dest);
        Message msg = consumer.receive(1000);
        TextMessage textMessage = (TextMessage) msg;
        System.out.println("Message recived -> "+textMessage.getText());

        System.out.println("Done!!");
        connection.close();
    }
}

/*

[ychopada@ychopada bin]$ ./artemis queue stat
        Connection brokerURL = tcp://localhost:61616
        |NAME                     |ADDRESS                  |CONSUMER_COUNT |MESSAGE_COUNT |MESSAGES_ADDED |DELIVERING_COUNT |MESSAGES_ACKED |SCHEDULED_COUNT |ROUTING_TYPE |
        |DLQ                      |DLQ                      |0              |0             |0              |0                |0              |0               |ANYCAST      |
        |ExpiryQueue              |ExpiryQueue              |0              |0             |0              |0                |0              |0               |ANYCAST      |
        |activemq.management.d237fe5d-09c7-4fe5-a059-c8196393c3fc|activemq.management.d237fe5d-09c7-4fe5-a059-c8196393c3fc|1              |0             |0              |0                |0              |0               |MULTICAST    |
        |queue1                   |queue1                   |0              |0             |0              |0                |0              |0               |ANYCAST      |
        [ychopada@ychopada bin]$ ./artemis queue stat
        Connection brokerURL = tcp://localhost:61616
        |NAME                     |ADDRESS                  |CONSUMER_COUNT |MESSAGE_COUNT |MESSAGES_ADDED |DELIVERING_COUNT |MESSAGES_ACKED |SCHEDULED_COUNT |ROUTING_TYPE |
        |DLQ                      |DLQ                      |0              |0             |0              |0                |0              |0               |ANYCAST      |
        |ExpiryQueue              |ExpiryQueue              |0              |0             |0              |0                |0              |0               |ANYCAST      |
        |activemq.management.b04ec2cc-edf4-4685-b9d4-9037ec08adc4|activemq.management.b04ec2cc-edf4-4685-b9d4-9037ec08adc4|1              |0             |0              |0                |0              |0               |MULTICAST    |
        |queue1                   |queue1                   |0              |9             |10             |0                |1              |0               |ANYCAST      |
        [ychopada@ychopada bin]$
*/

