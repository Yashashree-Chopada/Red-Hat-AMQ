
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import java.util.HashMap;
import java.util.Map;

public class RequestReplyExample {

    public static void main(final String[] args) throws Exception {
        final Map<String, TextMessage> requestMap = new HashMap<>();
        Connection connection = null;
        InitialContext initialContext = null;

        try {

            SimpleRequestServer server = new SimpleRequestServer();
            server.start();
            initialContext = new InitialContext();
            Queue requestQueue = (Queue) initialContext.lookup("queue/Test11");
            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = cf.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(requestQueue);
            TemporaryQueue replyQueue = session.createTemporaryQueue();
            MessageConsumer replyConsumer = session.createConsumer(replyQueue);
            TextMessage requestMsg = session.createTextMessage("A request message");
            requestMsg.setJMSReplyTo(replyQueue);
            producer.send(requestMsg);
            System.out.println("Request message sent.");
            requestMap.put(requestMsg.getJMSMessageID(), requestMsg);
            TextMessage replyMessageReceived = (TextMessage) replyConsumer.receive();
            System.out.println("Received reply: " + replyMessageReceived.getText());
            System.out.println("CorrelatedId: " + replyMessageReceived.getJMSCorrelationID());
            TextMessage matchedMessage = requestMap.get(replyMessageReceived.getJMSCorrelationID());
            System.out.println("We found matched request: " + matchedMessage.getText());
            replyConsumer.close();
           // replyQueue.delete();
            server.shutdown();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (initialContext != null) {
                initialContext.close();
            }
        }
    }
}

class SimpleRequestServer implements MessageListener {

    private Connection connection;

    private Session session;

    MessageProducer replyProducer;

    MessageConsumer requestConsumer;

    public void start() throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/Test11");
        ConnectionFactory cfact = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
        connection = cfact.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        replyProducer = session.createProducer(null);
        requestConsumer = session.createConsumer(requestQueue);
        requestConsumer.setMessageListener(this);
    }

    @Override
    public void onMessage(final Message request) {
        try {
            System.out.println("Received request message: " + ((TextMessage) request).getText());

            Destination replyDestination = request.getJMSReplyTo();

            System.out.println("Reply to queue: " + replyDestination);

            TextMessage replyMessage = session.createTextMessage("A reply message");

            replyMessage.setJMSCorrelationID(request.getJMSMessageID());


            replyProducer.send(replyDestination, replyMessage);

            System.out.println("Reply sent");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() throws JMSException {
        connection.close();
    }
}

/*
Output:
Request message sent.
Received request message: A request message
Reply to queue: ActiveMQTemporaryQueue[ed9519fd-97e4-470f-a2df-e7dc3fe04cad]
Reply sent
Received reply: A reply message
CorrelatedId: ID:66f3da4b-6de8-11ec-8be0-28d0ea5c2452
We found matched request: A request message
 */