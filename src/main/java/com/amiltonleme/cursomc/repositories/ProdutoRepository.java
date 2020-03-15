package com.amiltonleme.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.amiltonleme.cursomc.domain.Categoria;
import com.amiltonleme.cursomc.domain.Produto;

// Classe que faz comunicação com o banco de dados. (Repository).

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
	
	@Transactional(readOnly = true)
	//**@Param("nome") serve para jogar o "nome" no select acima no lugar de %:nome%
	//**@Param("categorias") serve para jogar o "categorias" no select acima no lugar de %categorias%
	//@Query ("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	//Page <Produto> search(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageRequest);
	
	//Seguindo o padrão de nome do spring data, pode trocar  todo esse método acima por esse abaixo
	Page <Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
}
