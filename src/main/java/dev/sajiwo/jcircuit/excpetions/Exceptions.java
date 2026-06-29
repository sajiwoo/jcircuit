package dev.sajiwo.jcircuit.excpetions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public abstract class Exceptions {

  public static ErrorResponseException error(HttpStatus status, String title) {
    return Exceptions.error(status, title, null);
  }

  public static ErrorResponseException error(HttpStatus status, String title, Throwable cause) {
    ProblemDetail body = ProblemDetail.forStatus(status);
    body.setTitle(title);
    body.setProperty("timestamp", Instant.now());

    return new ErrorResponseException(status, body, cause);
  }

}
