package com.jwss.studios.loja_virtual_BackEnd;

import java.io.Serial;

public class ExceptionMentoriaJava extends Exception{

    @Serial
    private static final long serialVersionUID = 1L;
    public ExceptionMentoriaJava(String message ) {
        super(message);
    }
}
