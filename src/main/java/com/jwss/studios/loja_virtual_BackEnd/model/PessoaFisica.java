package com.jwss.studios.loja_virtual_BackEnd.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "pessoa_fisica")
@PrimaryKeyJoinColumn(name = "id", foreignKey = @ForeignKey(name = "pessoa_fk"))
// ADICIONE ESSA LINHA PARA PROTEGER DE PROXIES DO HIBERNATE 7:
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PessoaFisica extends Pessoa {

    @Serial
    private static final long serialVersionUID = 1L;

    @CPF(message = "CPF está inválido")
    @Column(nullable = false)
    private String cpf;

    private LocalDate dataNascimento;

    // REMOVIDO: O atributo "empresa" foi deletado daqui pois já é herdado do pai!

    public String getCPF() {
        return cpf;
    }

    public void setCPF(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    // REMOVIDOS: Os métodos getEmpresa() e setEmpresa() foram deletados daqui!

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PessoaFisica that = (PessoaFisica) o;
        return Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cpf);
    }
}
