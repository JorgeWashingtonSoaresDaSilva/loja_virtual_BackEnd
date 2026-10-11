package com.jwss.studios.loja_virtual_BackEnd.repository;


import com.jwss.studios.loja_virtual_BackEnd.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<PessoaJuridica,Long> {
    @Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
    public PessoaJuridica existeCNPJCadastrado(String cnpj);

}
