package dev.sajiwo.jcircuit.configurations;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.sajiwo.jcircuit.entity.Circuit;
import dev.sajiwo.jcircuit.repository.CircuitRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class CircuitBreakersConfig {

  private final CircuitRepository circuitRepository;

  @Bean
  public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
    return factory -> {
      for (Circuit config : circuitRepository.findAll()) {
        factory.configure(builder -> builder.circuitBreakerConfig(config.toCircuitBreakerConfig()),
            config.getInstanceKey());
      }
    };
  }

}
