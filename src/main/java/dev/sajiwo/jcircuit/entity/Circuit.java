package dev.sajiwo.jcircuit.entity;

import java.time.Duration;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity
@Table(name = "CIRCUIT", uniqueConstraints = {
    @UniqueConstraint(name = "uniqueDomainPath", columnNames = { "targetDomain", "targetPath" })
})
public class Circuit {

  @Id
  @Column(name = "instance_key", nullable = false, updatable = false)
  private String instanceKey;

  @NaturalId(mutable = false)
  @Column(name = "TARGET_PATH", nullable = false)
  private String targetPath;

  @NaturalId(mutable = false)
  @Column(name = "TARGET_DOMAIN", nullable = false)
  private String targetDomain;

  @Enumerated(EnumType.STRING)
  @Column(name = "sliding_window_type", nullable = false)
  private SlidingWindowType slidingWindowType;

  @Column(name = "sliding_window_size", nullable = false)
  private int slidingWindowSize;

  @Column(name = "minimum_number_of_calls", nullable = false)
  private int minimumNumberOfCalls;

  @Column(name = "failure_rate_threshold", nullable = false)
  private float failureRateThreshold;

  @Column(name = "slow_call_rate_threshold", nullable = false)
  private float slowCallRateThreshold;

  @Column(name = "slow_call_duration_threshold_ms", nullable = false)
  private long slowCallDurationThresholdMs;

  @Column(name = "wait_duration_in_open_state_ms", nullable = false)
  private long waitDurationInOpenStateMs;

  @Column(name = "permitted_calls_in_half_open", nullable = false)
  private int permittedNumberOfCallsInHalfOpenState;

  @Column(name = "max_wait_duration_in_half_open_ms", nullable = false)
  private long maxWaitDurationInHalfOpenStateMs;

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
