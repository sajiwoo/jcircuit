package dev.sajiwo.jcircuit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dev.sajiwo.jcircuit.service.CircuitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
public class GenericController {

  private final CircuitService circuitService;

  @RequestMapping(method = RequestMethod.GET, path = "/ping")
  public Mono<ResponseEntity<?>> ping(ServerHttpRequest httpRequest) {
    return Mono.just(ResponseEntity.ok("pong"));
  }

  @RequestMapping("/**")
  public Mono<ResponseEntity<?>> generic(ServerHttpRequest httpRequest) {
    return circuitService.runOnCircuit(httpRequest);
  }

}
