package com.example.demokafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.TEXT_PLAIN_VALUE;

import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

public class DemoMessageRoutingTests {
  @Test
  public void messageRoutingTest() {
    try(ConfigurableApplicationContext context = new SpringApplicationBuilder(
        TestChannelBinderConfiguration
            .getCompleteConfiguration(RoutingStreamApplication.class))
        .run("--spring.cloud.function.definition=consumerForTextPlain;consumerForApplicationJson;functionRouter",
            "--spring.cloud.function.routing-expression=" + "headers.contentType.toString().equals('text/plain') ? 'consumerForTextPlain' : 'consumerForApplicationJson'")){
      InputDestination inputDestination = context.getBean(InputDestination.class);
      OutputDestination outputDestination = context.getBean(OutputDestination.class);

      Message<byte[]> inputTextPlainMessage = MessageBuilder.withPayload("Sample text plain message".getBytes())
          .setHeader(MessageHeaders.CONTENT_TYPE, TEXT_PLAIN_VALUE).build();
      inputDestination.send(inputTextPlainMessage, "functionRouter-in-0");

      //Event will be routed to consumer for text/plain
      Message<byte[]> outputTextPlainMessage = outputDestination.receive(0, "functionRouter-out-0");
      assertThat(outputTextPlainMessage.getPayload()).isEqualTo(TEXT_PLAIN_VALUE.getBytes());


      Message<byte[]> inputApplicationJsonMessage = MessageBuilder.withPayload("{\"id\": \"123\"}".getBytes())
          .setHeader(MessageHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE).build();
      inputDestination.send(inputApplicationJsonMessage, "functionRouter-in-0");

      //Event will be routed to consumer for application/json
      Message<byte[]> outputApplicationJsonMessage = outputDestination.receive(0, "functionRouter-out-0");
      assertThat(outputApplicationJsonMessage.getPayload()).isEqualTo(APPLICATION_JSON_VALUE.getBytes());
    }
  }

  @SpringBootApplication
  public static class RoutingStreamApplication {

    public static void main(String[] args) {
      SpringApplication.run(RoutingStreamApplication.class, args);
    }

    @Bean
    public Function<String, String> consumerForTextPlain() {
      return value -> {
        System.out.println("Consumer text/plain" + value);
        return "text/plain";
      };
    }

    @Bean
    public Function<String, String> consumerForApplicationJson() {
      return value -> {
        System.out.println("Consumer application/json" + value);
        return "application/json";
      };
    }
  }
}
