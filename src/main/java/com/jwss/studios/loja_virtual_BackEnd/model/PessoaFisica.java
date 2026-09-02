package com.jwss.studios.loja_virtual_BackEnd.model;


import jakarta.persistence.*;
import org.hibernate.annotations.Temporal;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import java.util.Objects;

@Entity
@Table(name = "pessoa_fisica")
@PrimaryKeyJoinColumn(name = "id",foreignKey = @ForeignKey(name = "pessoa_fk"))
public class PessoaFisica extends Pessoa{

    @Serial
    private static final long serialVersionUID =1L;
    @Column(nullable = false)
    private String CPF;

    private LocalDate dataNascimento;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PessoaFisica that = (PessoaFisica) o;
        return Objects.equals(CPF, that.CPF);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(CPF);
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
