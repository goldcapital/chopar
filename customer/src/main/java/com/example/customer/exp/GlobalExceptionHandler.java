package com.example.customer.exp;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice("com.example.customer")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers
            , HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : error.getField(),
                        (msg1, msg2) -> String.join(",", msg1,msg2)
                ));

return ResponseEntity.
        status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(errors));
    }

    @ExceptionHandler(AppBadException.class)
    public ResponseEntity<?> handle(AppBadException appBadException){
        return ResponseEntity.badRequest().body(appBadException.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?>handle(RuntimeException appBadException){
        appBadException.printStackTrace();
        return ResponseEntity.internalServerError().body(appBadException.getMessage());
    }
     /*  @ExceptionHandler(ForbiddenException.class)
    private ResponseEntity<?> handle(ForbiddenException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();*/

}
