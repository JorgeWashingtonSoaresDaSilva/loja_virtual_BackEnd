package com.jwss.studios.loja_virtual_BackEnd.model.dto;

import java.io.Serial;
import java.io.Serializable;

public class ObjetoErroDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String erro;
    private String code;

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
