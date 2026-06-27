// package dev.sajiwo.jcircuit.configuration;
//
// import static org.assertj.core.api.Assertions.assertThat;
//
// import org.junit.jupiter.api.Test;
// import
// org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
//
// import io.github.resilience4j.circuitbreaker.CircuitBreaker;
// import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
// import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
//
// class CircuitBreakerReloadTest {
//
// @Test
// void testRegistryReplaceUpdatesConfigAtRuntime() {
// // 1. Setup Registry and Factory
// CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();
// ReactiveResilience4JCircuitBreakerFactory factory = new
// ReactiveResilience4JCircuitBreakerFactory();
// factory.configureCircuitBreakerRegistry(registry);
//
// String instanceId = "testService";
//
// // 2. Initial Config: Set threshold to 50%
// CircuitBreakerConfig initialConfig = CircuitBreakerConfig.custom()
// .failureRateThreshold(50.0f)
// .build();
// registry.addConfiguration("default", initialConfig);
//
// // 3. Verify initial state
// var breaker1 = factory.create(instanceId);
// // Accessing underlying CB to verify config
// assertThat(registry.circuitBreaker(instanceId).getCircuitBreakerConfig().getFailureRateThreshold())
// .isEqualTo(50.0f);
//
// // 4. Update Config at Runtime: Set threshold to 80%
// CircuitBreakerConfig updatedConfig = CircuitBreakerConfig.custom()
// .failureRateThreshold(80.0f)
// .build();
//
// // Use the replace logic
// registry.replace(instanceId, CircuitBreaker.of(instanceId, updatedConfig));
//
// // 5. Verify the registry now returns the new config
// // Note: The factory uses registry.getConfiguration(), which now returns the
// new
// // instance
// float newThreshold =
// registry.circuitBreaker(instanceId).getCircuitBreakerConfig().getFailureRateThreshold();
//
// assertThat(newThreshold).isEqualTo(80.0f);
// System.out.println("Runtime update successful! New threshold: " +
// newThreshold);
// }
// }
