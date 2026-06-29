package dev.sajiwo.jcircuit.excpetions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import brave.Tracer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

  private final Tracer tracer;

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> errorException(Exception exception) {
    exception.printStackTrace();

    ProblemDetail body = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    body.setProperty("timestamp", Instant.now());
    body.setProperty("trace_id", tracer.currentSpan().context().traceIdString());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .header("x-trace-id", tracer.currentSpan().context().traceIdString()).body(body);
  }

  @ExceptionHandler(ErrorResponseException.class)
  public ResponseEntity<ProblemDetail> errorResponseExceptionHandler(ErrorResponseException exception) {
    exception.printStackTrace();

    exception.getBody().setProperty("trace_id", tracer.currentSpan().context().traceIdString());
    return ResponseEntity.status(exception.getStatusCode())
        .header("x-trace-id", tracer.currentSpan().context().traceIdString()).body(exception.getBody());
  }

}
