package com.amiltonleme.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amiltonleme.cursomc.domain.Produto;

// Classe que faz comunicação com o banco de dados. (Repository).

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
	
}
