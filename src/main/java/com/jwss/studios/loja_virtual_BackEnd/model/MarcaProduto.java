package com.jwss.studios.loja_virtual_BackEnd.model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
@Entity
@Table(name = "marca_produto")
@SequenceGenerator(name = "seq_marca_produto")
public class MarcaProduto  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_produto_marca")
    private Long id;

    private  String nomeDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDesc() {
        return nomeDesc;
    }

    public void setNomeDesc(String nomeDesc) {
        this.nomeDesc = nomeDesc;
    }
}
