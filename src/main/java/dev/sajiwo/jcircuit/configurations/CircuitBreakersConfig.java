package dev.sajiwo.jcircuit.configurations;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import dev.sajiwo.jcircuit.entity.Circuit;
import dev.sajiwo.jcircuit.repository.CircuitRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CircuitBreakersConfig {

  private final CircuitRepository circuitRepository;
  private final CircuitBreakerRegistry circuitBreakerRegistry;
  private final MeterRegistry meterRegistry;

  @EventListener
  public void configListener(RefreshScopeRefreshedEvent refreshEvent) {
    Circuit circuit = circuitRepository.findAll().getFirst();
    log.info("Context has been refresh : {}", circuit);
    circuitBreakerRegistry.replace(circuit.getInstanceKey(),
        CircuitBreaker.of(circuit.getInstanceKey(), circuit.toCircuitBreakerConfig()));
  }

  @Bean
  public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
    TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(circuitBreakerRegistry)
        .bindTo(meterRegistry);

    return factory -> {
      for (Circuit config : circuitRepository.findAll()) {
        factory.configure(builder -> builder.circuitBreakerConfig(config.toCircuitBreakerConfig()),
            config.getInstanceKey());
      }
    };
  }

}
