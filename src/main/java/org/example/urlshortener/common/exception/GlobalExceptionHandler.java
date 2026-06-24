package org.example.urlshortener.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<String> handleNotFound(
                NotFoundException ex
        ) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<String> handleAccessDenied(
                AccessDeniedException ex
        ) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ex.getMessage());
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<String> handleBadRequest(
                BadRequestException ex
        ) {
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<String> handleValidation(
                MethodArgumentNotValidException ex
        ) {
            String message =
                    ex.getBindingResult()
                            .getFieldError()
                            .getDefaultMessage();

            return ResponseEntity
                    .badRequest()
                    .body(message);
        }

        @ExceptionHandler(LinkExpiredException.class)
        public ResponseEntity<String> handleExpired(
                LinkExpiredException ex
        ) {
            return ResponseEntity
                    .status(HttpStatus.GONE)
                    .body(ex.getMessage());
        }

        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<String> handleConflict(
                ConflictException ex
        ) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ex.getMessage());
        }

        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<String> handleUnauthorized(
                UnauthorizedException ex
        ) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ex.getMessage());
        }
}