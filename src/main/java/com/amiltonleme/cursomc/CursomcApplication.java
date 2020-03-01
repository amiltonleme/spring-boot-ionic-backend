package com.amiltonleme.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amiltonleme.cursomc.domain.Categoria;
import com.amiltonleme.cursomc.domain.Cidade;
import com.amiltonleme.cursomc.domain.Cliente;
import com.amiltonleme.cursomc.domain.Endereco;
import com.amiltonleme.cursomc.domain.Estado;
import com.amiltonleme.cursomc.domain.ItemPedido;
import com.amiltonleme.cursomc.domain.Pagamento;
import com.amiltonleme.cursomc.domain.PagamentoComBoleto;
import com.amiltonleme.cursomc.domain.PagamentoComCartao;
import com.amiltonleme.cursomc.domain.Pedido;
import com.amiltonleme.cursomc.domain.Produto;
import com.amiltonleme.cursomc.domain.enums.EstadoPagamento;
import com.amiltonleme.cursomc.domain.enums.TipoCliente;
import com.amiltonleme.cursomc.repositories.CategoriaRepository;
import com.amiltonleme.cursomc.repositories.CidadeRepository;
import com.amiltonleme.cursomc.repositories.ClienteRepository;
import com.amiltonleme.cursomc.repositories.EnderecoRepository;
import com.amiltonleme.cursomc.repositories.EstadoRepository;
import com.amiltonleme.cursomc.repositories.ItemPedidoRepository;
import com.amiltonleme.cursomc.repositories.PagamentoRepository;
import com.amiltonleme.cursomc.repositories.PedidoRepository;
import com.amiltonleme.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Override
	public void run(String... args) throws Exception {
		
		//Instanciação de objetos
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		//Acrescentando Categoria para testes de paginação
		Categoria cat3 = new Categoria(null, "Cama mesa e banho");
		Categoria cat4 = new Categoria(null, "Jardinagem");
		Categoria cat5 = new Categoria(null, "Eletrônicos");
		Categoria cat6 = new Categoria(null, "Decoração");
		Categoria cat7 = new Categoria(null, "Perfumaria");
		
		//Instanciação de objetos
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
		// Associações das categorias com os produtos
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		
		// Associações dos produtos com as categorias
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		//Salvar os dados no banco (Repository) Categoria / Produto
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++
		
		//Instanciação de objetos
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "Sâo Paulo");
		
		//Instanciação de objetos
		//Quando se tem muitos pra um, a associação já é feita no construtor
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		// Associações de Estado com as cidades
		est1.getCidades().addAll(Arrays.asList(c1));
		est1.getCidades().addAll(Arrays.asList(c2, c3));
		
		
		//Salvar os dados no banco (Repository) Estado / Cidade
		//Como o Estado tem várias cidades, ele será inserido primeiro.
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));

		//+++++++++++++++++++++++++++++++++++++++++++++++++++
		
		//Instanciação de objetos
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
		
		//Associação do cliente com os telefones
		cli1.getTelefones().addAll(Arrays.asList("27363326", "93839383"));
		
		//Instanciação de objetos
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 203", "Jardim", "38220834", c1, cli1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38771012", c2, cli1);
		
		//Associação do cliente com endereço.
		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1, e2));
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 06);
		//Setar o pagamento com o pedido
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		//Setar o pagamento com o pedido
		ped2.setPagamento(pagto2);
		
		//Associação do cliente com pedido
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));
	
		//+++++++++++++++++++++++++++++++++++++++++++++++++++
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
		
		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));
		
		itemPedidoRepository.saveAll(Arrays.asList(ip1,ip2,ip3));
	}

}
