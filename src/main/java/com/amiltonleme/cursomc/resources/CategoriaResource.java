package com.amiltonleme.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amiltonleme.cursomc.domain.Categoria;
import com.amiltonleme.cursomc.dto.CategoriaDTO;
import com.amiltonleme.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value= "/{id}", method=RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		Categoria obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	/**
	 * @RequestBody Instrui o sistema a criar o objeto Categoria a partir do objeto Json enviado
	 * @return Retorna a URI da nova categoria
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@RequestBody Categoria obj) {
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	//Método para alterar uma categoia por id
	@RequestMapping(value= "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody Categoria obj, @PathVariable Integer id) {
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	//Método para deletar uma Categoria por id
	@RequestMapping(value= "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	//Método para retornar uma lista de categoriaDTO, buscando a lista de Categoria e converte para categoriaDTO
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		//Percorrer essa lista e para cada elemento dessa lista instanciar o DTO correspondente
		List<Categoria> list = service.findAll();
		//Esse comando abaixo serve para converter uma lista de Categoria em uma lista de CategoriaDTO
		//O list.stream serve para percorrer a lista
		//O map efetua uma operação para cada elemento da lista que nesse caso tem o apelido de "obj" como argumento
		//O -> Arrow Function cria uma função anônima que recebe o obj
		//O new CategoriaDTO passando o obj como argumento
		//O Collect(Collectors.list volta o stream de objetos para o tipo lista
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

}
