package dev.sajiwo.jcircuit.entity;

import java.time.Duration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CIRCUIT")
public class Circuit {

  @Id
  @Column(name = "instance_key", nullable = false, updatable = false)
  private String instanceKey;

  @Enumerated(EnumType.STRING)
  @Column(name = "sliding_window_type", nullable = false)
  private SlidingWindowType slidingWindowType = SlidingWindowType.COUNT_BASED;

  @Column(name = "sliding_window_size", nullable = false)
  private int slidingWindowSize = 100;

  @Column(name = "minimum_number_of_calls", nullable = false)
  private int minimumNumberOfCalls = 100;

  @Column(name = "failure_rate_threshold", nullable = false)
  private float failureRateThreshold = 50.0f;

  @Column(name = "slow_call_rate_threshold", nullable = false)
  private float slowCallRateThreshold = 100.0f;

  @Column(name = "slow_call_duration_threshold_ms", nullable = false)
  private long slowCallDurationThresholdMs = 60000L;

  @Column(name = "wait_duration_in_open_state_ms", nullable = false)
  private long waitDurationInOpenStateMs = 60000L;

  @Column(name = "permitted_calls_in_half_open", nullable = false)
  private int permittedNumberOfCallsInHalfOpenState = 10;

  @Column(name = "max_wait_duration_in_half_open_ms", nullable = false)
  private long maxWaitDurationInHalfOpenStateMs = 0L;

  @Column(name = "auto_transition_to_half_open", nullable = false)
  private boolean automaticTransitionFromOpenToHalfOpenEnabled = false;

  public CircuitBreakerConfig toCircuitBreakerConfig() {
    CircuitBreakerConfig.Builder builder = CircuitBreakerConfig.custom()
        .slidingWindowType(this.slidingWindowType)
        .slidingWindowSize(this.slidingWindowSize)
        .minimumNumberOfCalls(this.minimumNumberOfCalls)
        .failureRateThreshold(this.failureRateThreshold)
        .slowCallRateThreshold(this.slowCallRateThreshold)
        .slowCallDurationThreshold(Duration.ofMillis(this.slowCallDurationThresholdMs))
        .waitDurationInOpenState(Duration.ofMillis(this.waitDurationInOpenStateMs))
        .permittedNumberOfCallsInHalfOpenState(this.permittedNumberOfCallsInHalfOpenState)
        .maxWaitDurationInHalfOpenState(Duration.ofMillis(this.maxWaitDurationInHalfOpenStateMs));

    if (this.automaticTransitionFromOpenToHalfOpenEnabled) {
      builder.enableAutomaticTransitionFromOpenToHalfOpen();
    }
    return builder.build();
  }
}
