package com.example.demo.exception.contract;

public class ContractTransferErrorException extends RuntimeException{

    public ContractTransferErrorException(){
        super();
    }

    public ContractTransferErrorException(String message){
        super(message);
    }

    public ContractTransferErrorException(String message, Throwable th){
        super(message, th);
    }
}
