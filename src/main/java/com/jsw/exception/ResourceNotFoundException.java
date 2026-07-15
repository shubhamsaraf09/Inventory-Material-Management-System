package com.jsw.exception;

import org.yaml.snakeyaml.scanner.Constant;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
