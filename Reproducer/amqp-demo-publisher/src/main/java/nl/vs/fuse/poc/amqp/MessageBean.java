package nl.vs.fuse.poc.amqp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class MessageBean {

	private static final String HOST_NAME;
	private static int counter;
	private Date time = new Date();

	static {
		InetAddress localHost = null;
		try {
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
		}
		HOST_NAME = (localHost != null ? localHost.getHostName() : "Unknown");
	}

	public MessageBean createMessage() {
		return new MessageBean();
	}

	public int getCounter() {
		return ++counter;
	}

	public Date getTime() {
		return time;
	}

	public String getHostName() {
		return HOST_NAME;
	}
}
