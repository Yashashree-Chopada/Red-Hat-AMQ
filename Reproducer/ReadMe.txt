[1] First start the broker cluster by running 'docker-compose up -d' from the amqp-demo-publisher directory. 

[2] Start the publisher application by running 'mvn clean install spring-boot:run' Same for Subscriber application.

[3] To check broker queue stat - Check docker PID for both broker and then run below commands

 - docker exec -it pid-broker1 or 'name-of-broker' /bin/bash
 - cd broker/bin 
 - ./artemis queue stat.

