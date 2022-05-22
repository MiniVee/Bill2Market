package com.example.demo.exception.contract;

public class TokenNotFoundException extends RuntimeException{

    public TokenNotFoundException(){
        super();
    }

    public TokenNotFoundException(String message){
        super(message);
    }

    public TokenNotFoundException(String message, Throwable th){
        super(message, th);
    }
}
