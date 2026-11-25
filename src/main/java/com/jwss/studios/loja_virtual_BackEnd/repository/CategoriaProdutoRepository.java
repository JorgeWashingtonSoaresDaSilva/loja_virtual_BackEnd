package com.jwss.studios.loja_virtual_BackEnd.repository;

import com.jwss.studios.loja_virtual_BackEnd.model.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    @Query(nativeQuery = true,value = "select count(1) > 0 from categoria_produto where  upper(trim(nome_desc)) = upper(trim(?1));")
    public boolean existeCategoria(String nomeCategoria);

}
