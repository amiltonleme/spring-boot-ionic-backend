package com.amiltonleme.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.amiltonleme.cursomc.domain.Categoria;
import com.amiltonleme.cursomc.repositories.CategoriaRepository;
import com.amiltonleme.cursomc.services.exceptions.DataIntegrityException;
import com.amiltonleme.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
// Implementação utlilizada para Spring Boot 2.x.x em diante.
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));	
	
		/* Implementação utilizada para Spring Boot até versão anterior.	
		public Categoria find(Integer id) {
			Optional<Categoria> obj = repo.findById(id);
			if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName());	
			}
			return obj.orElse(null);} */
	}
	
	public Categoria insert(Categoria obj) {
		/* ao se setar ID com NULL se garante que será criado um novo objeto. Caso
		contrário, se houver um ID especificado será atualizado o objeto
		correspondente.*/
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		/* Se houver um ID especificado será atualizado o objeto
		correspondente.*/
		find(obj.getId());
		return repo.save(obj);
	}
	
	public void delete(Integer id){
		find(id);
		try {
		repo.deleteById(id);
		}
		//Se tentar apagar uma Categoria que contenha produto, a exception será tratada abaixo
		catch (DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possível excluir uma categoria que contenha produtos");
		}
	}
	
		public List<Categoria> findAll (){
		return repo.findAll();
	}
}
