package com.jwss.studios.loja_virtual_BackEnd.model;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;


import java.io.Serial;
import java.util.Objects;

@Entity
@Table(name = "acesso")
@SequenceGenerator(name = "seq_acesso", sequenceName = "seq_acesso", allocationSize = 1, initialValue = 1)
public class Acesso implements GrantedAuthority {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_acesso")
    private Long id;
    @Column(nullable = false)
    private String descricao;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Acesso acesso = (Acesso) o;
        return id == acesso.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public @Nullable String getAuthority() {
        return this.descricao;
    }
}
