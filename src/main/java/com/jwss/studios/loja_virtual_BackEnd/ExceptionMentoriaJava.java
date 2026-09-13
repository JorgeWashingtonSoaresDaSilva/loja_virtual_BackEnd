package com.jwss.studios.loja_virtual_BackEnd;

import java.io.Serial;

public class ExcepitionMentoriaJava extends Exception{

    @Serial
    private static final long serialVersionUID = 1L;
    public ExcepitionMentoriaJava(String message ) {
        super(message);
    }
}
