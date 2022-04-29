package nl.vs.fuse.poc.amqp;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.camel.Exchange;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.support.SynchronizationAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientAckBean {

	private static final Logger LOG = LoggerFactory.getLogger(ClientAckBean.class);

	public void prepareForAcknowledge(Exchange exchange) throws JMSException {
		final Message jms = exchange.getIn(JmsMessage.class).getJmsMessage();

		exchange.addOnCompletion(new SynchronizationAdapter() {
			@Override
			public void onComplete(Exchange exchange) {
				LOG.debug("Using JMS client acknowledge to accept the JMS message consumed: {}", jms);
				try {
					jms.acknowledge();
				} catch (JMSException e) {
					LOG.warn("JMS client acknowledge failed due: " + e.getMessage(), e);
				}
			}
		});
	}
}
