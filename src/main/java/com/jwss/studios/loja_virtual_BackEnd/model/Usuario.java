package com.jwss.studios.loja_virtual_BackEnd.model;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "seq_usuario",sequenceName = "seq_usuario",initialValue = 1,allocationSize = 1)
public class Usuario  implements UserDetails {
    @Serial
    private static final  long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_usuario")
    private long id;
    @Column(nullable = false)
    private String login;
    @Column(nullable = false)
    private String senha;
    @ManyToOne(targetEntity = Pessoa.class)
    @JoinColumn(name = "pessoa_id",nullable = false,foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT,name = "pessoa_fk"))
    private Pessoa pessoa;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    private LocalDate dataAtualSenha;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuarios_acesso",uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id","acesso_id"},
    name = "unique_acesso_user"),joinColumns = @JoinColumn(name = "usuario_id",referencedColumnName = "id",
            table = "usuario", unique = false, foreignKey = @ForeignKey(name = "usuario_fk", value = ConstraintMode.CONSTRAINT))
    ,inverseJoinColumns = @JoinColumn(name = "acesso_id",unique = false, referencedColumnName = "id", table = "acesso",
            foreignKey = @ForeignKey(name ="acesso_fk", value = ConstraintMode.CONSTRAINT)))
    private List<Acesso> acessos;

    // Autoriades = São os acesso ou seja ROLE_ADMIN, ROLE_SECRETARIO
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return acessos;
    }

    @Override
    public @Nullable String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
