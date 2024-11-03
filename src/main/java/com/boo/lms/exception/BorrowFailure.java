package com.boo.lms.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class BorrowFailure extends RuntimeException{
    public BorrowFailure(String msg){
        super(msg);
    }
}
