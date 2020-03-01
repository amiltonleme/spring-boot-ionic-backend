package com.amiltonleme.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.amiltonleme.cursomc.domain.Cliente;
import com.amiltonleme.cursomc.dto.ClienteDTO;
import com.amiltonleme.cursomc.repositories.ClienteRepository;
import com.amiltonleme.cursomc.services.exceptions.DataIntegrityException;
import com.amiltonleme.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
// Implementação utlilizada para Spring Boot 2.x.x em diante.
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));	
	

// Implementação utilizada para Spring Boot até versão anterior.	
//	public Cliente find(Integer id) {
//		Optional<Cliente> obj = repo.findById(id);
//		if (obj == null) {
//			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName());	
//		}
//		
//		return obj.orElse(null);
	}
	
	public Cliente update(Cliente obj) {
		/* Se houver um ID especificado será atualizado o objeto
		correspondente.*/
		//Instancia um cliente a partir do banco de dados
		Cliente newObj = find(obj.getId());
		//método auxiliar para atiualizar os dados do novo objeto newObj com base no objeto que veio como argumento
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id){
		find(id);
		try {
		repo.deleteById(id);
		}
		//Se tentar apagar uma Cliente que contenha produto, a exception será tratada abaixo
		catch (DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possível excluir um cliente que contenha entidades relacionadas");
		}
	}
	
	public List<Cliente> findAll (){
		return repo.findAll();
	}
		
	public Page<Cliente> findPage (Integer page, Integer linesPerPage, String orderBy, String direction){
		//PageRequest Está no pacote Spring Data
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
		
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}
	
	//Private porque é um método auxiliar dentro da classe
	//Atualiza os dados do objeto newObj como os novos dados que vieram do objeto obj
	private void updateData (Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

}
