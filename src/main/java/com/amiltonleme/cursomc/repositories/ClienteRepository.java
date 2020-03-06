package com.amiltonleme.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.amiltonleme.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	/**Essa anotação serve para que a transação não seja envolvida com o banco de dados,
	*  fazendo com que a transação fique mais rápida**/
	@Transactional(readOnly = true)
	/**O findBy... orienta o Spring Data a detectar o Email, nesse caso, e 
	*  implementar o método automaticamente **/ 
	Cliente findByEmail (String email);
	
}
