package com.jwss.studios.loja_virtual_BackEnd.model.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class QsaDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String nome;
    private String qual;
    private String pais_origem;
    private String nome_rep_legal;
    private String qual_rep_legal;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQual() {
        return qual;
    }

    public void setQual(String qual) {
        this.qual = qual;
    }

    public String getPais_origem() {
        return pais_origem;
    }

    public void setPais_origem(String pais_origem) {
        this.pais_origem = pais_origem;
    }

    public String getNome_rep_legal() {
        return nome_rep_legal;
    }

    public void setNome_rep_legal(String nome_rep_legal) {
        this.nome_rep_legal = nome_rep_legal;
    }

    public String getQual_rep_legal() {
        return qual_rep_legal;
    }

    public void setQual_rep_legal(String qual_rep_legal) {
        this.qual_rep_legal = qual_rep_legal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QsaDTO qsaDTO = (QsaDTO) o;
        return Objects.equals(nome, qsaDTO.nome) && Objects.equals(qual, qsaDTO.qual) && Objects.equals(pais_origem, qsaDTO.pais_origem) && Objects.equals(nome_rep_legal, qsaDTO.nome_rep_legal) && Objects.equals(qual_rep_legal, qsaDTO.qual_rep_legal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, qual, pais_origem, nome_rep_legal, qual_rep_legal);
    }
}
