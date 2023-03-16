package com.example.demokafka.routingConfig;

import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.kafka.common.KafkaException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBindingConfig {

  @Bean
  public Consumer<String> sampleConsumer() {
    return (s) -> System.out.println("Message consumed by consumer 1: " +s);
  }

  @Bean
  public Consumer<String> consumerWithErrorHandling() {
    return (inputString) -> {
      throw new KafkaException("any random exception while consuming the message : " +inputString);
    };
  }

  @Bean
  public Function<String, String> sampleProducer() {
    return (consumedMessage) -> String.valueOf(consumedMessage.length());
  }

}
