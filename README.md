## Spring cloud stream with kafka binder
Quick guide for understanding basic configuration required for setting the kafka binder.

### To run the Kafka containers

Move to the `docker` directory and start up the kafka broker, zookeeper containers.
1. `cd docker`
2. `docker-compose up -d`

kafka broker will be up and running on `localhost:29092` port can be configured [here](./docker/docker-compose.yml#16)

### To install and run the project
1. `mvn clean install`
2. `mvn spring-boot:run`
3. `mvn clean test` to run specifically the tests.

### [Project configuration](./src/main/resources/application.yaml)
Configuration gives the details about 
- [Default Kafka binder properties](src/main/resources/application.yaml#L6)
- [Sample Producer](src/main/java/com/example/demokafka/routingConfig/EventBindingConfig.java#L25)
- [Sample Consumer](src/main/java/com/example/demokafka/routingConfig/EventBindingConfig.java#L13)
- [Error handling scenario](src/main/resources/application.yaml#L12)
- [Message Routing](src/test/java/com/example/demokafka/DemoMessageRoutingTests.java)
