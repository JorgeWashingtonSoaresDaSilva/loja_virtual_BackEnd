package com.jwss.studios.loja_virtual_BackEnd.repository;

import com.jwss.studios.loja_virtual_BackEnd.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {


}
