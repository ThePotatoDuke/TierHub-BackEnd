package com.example.TierHub.Exceptions;

public class StorageException extends RuntimeException
{
    public StorageException(){}
    public StorageException(String message){
        super(message);
    }


}
