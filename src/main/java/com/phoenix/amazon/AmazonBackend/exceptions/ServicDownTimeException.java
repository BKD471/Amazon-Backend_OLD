package com.phoenix.amazon.AmazonBackend.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
public class ServicDownTimeException extends Exception{
    public ServicDownTimeException(Object className, String reason, String methodName){
        super(String.format("%s has occurred  for %s in %s",className,reason,methodName));
    }
}
