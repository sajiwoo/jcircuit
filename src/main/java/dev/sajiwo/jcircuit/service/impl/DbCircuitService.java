package dev.sajiwo.jcircuit.service.impl;

import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import dev.sajiwo.jcircuit.entity.Circuit;
import dev.sajiwo.jcircuit.excpetions.Exceptions;
import dev.sajiwo.jcircuit.repository.CircuitRepository;
import dev.sajiwo.jcircuit.service.CircuitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class DbCircuitService implements CircuitService {

  private final CircuitRepository circuitRepository;
  private final ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory;
  private final WebClient webClient;

  @Override
  public Mono<ResponseEntity<?>> runOnCircuit(ServerHttpRequest httpRequest) {
    Circuit circuitInfo = circuitRepository.findByTargetPathAndTargetDomain(httpRequest.getURI().getPath(),
        httpRequest.getURI().getHost()).orElseThrow(() -> {
          throw Exceptions.error(HttpStatus.NOT_FOUND, "Configuration is missing");
        });

    final String domain = StringUtils.hasText(circuitInfo.getTargetDomain()) ? circuitInfo.getTargetDomain()
        : httpRequest.getHeaders().getFirst("X-Target-Domain");

    if (!StringUtils.hasText(domain)) {
      throw Exceptions.error(HttpStatus.NOT_FOUND, "Missing target domain configuration");
    }

    WebClient httpClient = webClient.mutate().baseUrl(domain).build();
    ReactiveCircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitInfo.getInstanceKey());

    return circuitBreaker.run(httpClient.method(httpRequest.getMethod())
        .uri(httpRequest.getURI().getPath())
        .headers(headers -> headers.addAll(httpRequest.getHeaders()))
        .headers(headers -> headers.add("X-Service-Forwarder", "JCircuit"))
        .body(BodyInserters.fromDataBuffers(httpRequest.getBody())).retrieve().toEntity(String.class)
        .map(entity -> entity), fallback -> {
          throw Exceptions.error(HttpStatus.SERVICE_UNAVAILABLE, "Service is temporary un-available");
        });
  }

}
