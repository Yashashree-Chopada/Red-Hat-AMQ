import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

public class SSL_broker
{
    public static void main(String[] args) throws JMSException {
        String user = "admin";
        String password ="admin";
        System.setProperty("javax.net.debug","ssl,handshake");
        String url = "tcp://localhost:61616?sslEnabled=true;trustStorePath=/home/ychopada/NotBackedUp/AMQ/amq-broker-7.8.2/bin/SslBroker/etc/truststore.jks;trustStorePassword=password";

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(url,user,password);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("TEST1");
        MessageProducer messageProducer = session.createProducer(destination);
        messageProducer.send(session.createTextMessage("HelloSSL"));
        System.out.println("Done!!");
    }

}

/*
Tool to Generate Key-Pair -> KeyStore Explorer
Steps
1. New -> JKS -> Right-click "Generate-key-Pair" -> RSA
2. In Signature Algorithm select SHA-512 -> Add Name (CN, ON ...)
3. set the password
4. Export public key
5. ctrl+s set password again.
6. save the file as keystore.jks
7.Export certificate in X.509 format and save file as broker.crt
8 Now create trustStore -> keytool -keytool -import -file broker.crt -keystore truststore.jks -storepass password (Make sure keystore.jks broker.crt should be in same folder)

To view certificates
    1. KeyStore(PrivateKey)-> keytool -list -v -keystore keystore.jks -storepass password ->> Entry type: PrivateKeyEntry
    2. trustStore (PublicKey)-> keytool -list -v -keystore trustStore.jks -storepass password ->> Entry type: trustedCertEntry

Refer attached Broker for ssl configuration.

HW.
Try one way SSL in simple broker
Try One way SSL in Cluster broker with HA as replication.


 */