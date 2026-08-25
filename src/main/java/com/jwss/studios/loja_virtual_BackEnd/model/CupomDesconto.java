package com.jwss.studios.loja_virtual_BackEnd.model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "cupom_desconto")
@SequenceGenerator(name = "seq_cupom_desconto",sequenceName ="seq_cupom_desconto" ,allocationSize = 1,initialValue = 1)
public class CupomDesconto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cupom_desconto")
    private Long id;
    private  String codDescricao;
    private BigDecimal valorRealDesconto;
    private BigDecimal valorPorcentagemDesconto;
    private LocalDate dataValidadeCupom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodDescricao() {
        return codDescricao;
    }

    public void setCodDescricao(String codDescricao) {
        this.codDescricao = codDescricao;
    }

    public BigDecimal getValorRealDesconto() {
        return valorRealDesconto;
    }

    public void setValorRealDesconto(BigDecimal valorRealDesconto) {
        this.valorRealDesconto = valorRealDesconto;
    }

    public BigDecimal getValorPorcentagemDesconto() {
        return valorPorcentagemDesconto;
    }

    public void setValorPorcentagemDesconto(BigDecimal valorPorcentagemDesconto) {
        this.valorPorcentagemDesconto = valorPorcentagemDesconto;
    }

    public LocalDate getDataValidadeCupom() {
        return dataValidadeCupom;
    }

    public void setDataValidadeCupom(LocalDate dataValidadeCupom) {
        this.dataValidadeCupom = dataValidadeCupom;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CupomDesconto that = (CupomDesconto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
