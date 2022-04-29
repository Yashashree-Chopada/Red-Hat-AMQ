package nl.vs.fuse.poc.amqp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * A spring-boot application that includes a Camel route builder to setup the
 * Camel routes
 */
@SpringBootApplication
@ImportResource({ "classpath:spring/camel-context.xml" })
public class DemoSubscriber {
	public static void main(String[] args) {
		SpringApplication.run(DemoSubscriber.class, args);
	}
}
