package com.amiltonleme.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amiltonleme.cursomc.domain.Pedido;
import com.amiltonleme.cursomc.repositories.PedidoRepository;
import com.amiltonleme.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
// Implementação utlilizada para Spring Boot 2.x.x em diante.
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));	
	

// Implementação utilizada para Spring Boot até versão anterior.	
//	public Pedido find(Integer id) {
//		Optional<Pedido> obj = repo.findById(id);
//		if (obj == null) {
//			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName());	
//		}
//		
//		return obj.orElse(null);
	}
}
