package com.jwss.studios.loja_virtual_BackEnd.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.br.CNPJ;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "pessoa_juridica")
@PrimaryKeyJoinColumn(name = "id", foreignKey = @ForeignKey(name = "pessoa_fk"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PessoaJuridica extends Pessoa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @CNPJ(message = "CNPJ está inválido")
    @Column(nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private String InscEstatual;

    private String InscMunicipal;

    @Column(nullable = false)
    private String nomeFantasia;

    @Column(nullable = false)
    private String razaoSocial;

    private String categoria;

    // REMOVIDO: O atributo "empresa" foi deletado daqui pois já existe na classe pai Pessoa!

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscEstatual() {
        return InscEstatual;
    }

    public void setInscEstatual(String inscEstatual) {
        InscEstatual = inscEstatual;
    }

    public String getInscMunicipal() {
        return InscMunicipal;
    }

    public void setInscMunicipal(String inscMunicipal) {
        InscMunicipal = inscMunicipal;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // REMOVIDOS: Os métodos getEmpresa() e setEmpresa() foram deletados daqui!

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PessoaJuridica that = (PessoaJuridica) o;
        return Objects.equals(cnpj, that.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cnpj);
    }
}
