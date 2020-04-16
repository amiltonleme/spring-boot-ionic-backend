package com.amiltonleme.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amiltonleme.cursomc.domain.Cliente;
import com.amiltonleme.cursomc.domain.ItemPedido;
import com.amiltonleme.cursomc.domain.PagamentoComBoleto;
import com.amiltonleme.cursomc.domain.Pedido;
import com.amiltonleme.cursomc.domain.enums.EstadoPagamento;
import com.amiltonleme.cursomc.repositories.ItemPedidoRepository;
import com.amiltonleme.cursomc.repositories.PagamentoRepository;
import com.amiltonleme.cursomc.repositories.PedidoRepository;
import com.amiltonleme.cursomc.security.UserSS;
import com.amiltonleme.cursomc.services.exceptions.AuthorizationException;
import com.amiltonleme.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
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
	@Transactional
	public Pedido insert (Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			//Setar como produto do item
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
//	//Esses atributos servem para paginação
//	public Page<Pedido> findPage (Integer page, Integer linesPerPage, String orderBy, String direction){
//		UserSS user = UserService.authenticated(); 
//		if (user==null) {
//			throw new AuthorizationException("Acesso negado!");
//		}
//		//PageRequest Está no pacote Spring Data
//		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
//		Cliente cliente = clienteService.find(user.getId());	
//   		return repo.findByCliente(cliente, pageRequest);
//	}

	public Page<Pedido> findPage (Integer page, Integer linesPerPage, String orderBy, String direction){
		//PageRequest Está no pacote Spring Data
		UserSS user = UserService.authenticated(); 
		if (user==null) {
			throw new AuthorizationException("Acesso negado");
		}
		Cliente cliente = clienteService.find(user.getId());
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findByCliente(cliente, pageRequest);
	}


}
