package com.example.demokafka;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class DemoProducerFunctionTest {
  @Test
  public void testSampleProducerFunction() {
    try(ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(
        TestChannelBinderConfiguration.getCompleteConfiguration(DemoKafkaApplication.class))
        .run("--spring.cloud.function.definition=sampleConsumer;sampleProducer;consumerWithErrorHandling")){
      InputDestination inputDestination = applicationContext.getBean(InputDestination.class);
      OutputDestination outputDestination = applicationContext.getBean(OutputDestination.class);

      Message<byte[]> inputMessage = MessageBuilder.withPayload("Sample Message Produced".getBytes()).build();

      inputDestination.send(inputMessage, "another-consumer-topic");

      Message<byte[]> outputMessage = outputDestination.receive(0, "another-producer-topic");
      assertThat(outputMessage.getPayload()).isEqualTo("23".getBytes());
    }
  }

}


