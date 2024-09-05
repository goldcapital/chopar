package com.example.chopar_1.controller;

import com.example.chopar_1.exp.AppBadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
  /*  @ExceptionHandler(ForbiddenException.class)
    private ResponseEntity<?> handle(ForbiddenException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();*/
  @ExceptionHandler(AppBadException.class)
  public ResponseEntity<?> handle(AppBadException appBadException){
      return ResponseEntity.badRequest().body(appBadException.getMessage());
  }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?>handle(RuntimeException appBadException){
      appBadException.printStackTrace();
        return ResponseEntity.internalServerError().body(appBadException.getMessage());
        }

}
