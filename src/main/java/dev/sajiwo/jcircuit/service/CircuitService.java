package dev.sajiwo.jcircuit.service;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;

import reactor.core.publisher.Mono;

public interface CircuitService {

  Mono<ResponseEntity<?>> runOnCircuit(ServerHttpRequest httpRequest);

}
