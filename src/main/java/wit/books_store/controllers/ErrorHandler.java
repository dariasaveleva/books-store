package wit.books_store.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wit.books_store.exceptions.DuplicationException;
import wit.books_store.exceptions.KafkaProcessingException;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public String getValidationException(final ValidationException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getNotFoundException(final NotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String getDuplicationException(final DuplicationException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> map = new HashMap<>();
      ex.getBindingResult().getFieldErrors().forEach(error ->
              map.put(error.getField(), error.getDefaultMessage()));
      return map;
    }

    @ExceptionHandler(KafkaProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleKafkaProcessingException(KafkaProcessingException ex) {
        return ex.getMessage();
    }
}
