package com.jwss.studios.loja_virtual_BackEnd.enums;

public enum TipoPessoa {

    JURIDICA("Juridica"),
    JURIDICA_FORNECEDOR("Juridica e Fornecedor"),
    FISICA("Fisica");

    private String descricao;

    private TipoPessoa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
