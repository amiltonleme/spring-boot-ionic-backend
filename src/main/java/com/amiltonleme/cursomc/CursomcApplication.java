package com.amiltonleme.cursomc;

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
import com.amiltonleme.cursomc.domain.Produto;
import com.amiltonleme.cursomc.domain.enums.TipoCliente;
import com.amiltonleme.cursomc.repositories.CategoriaRepository;
import com.amiltonleme.cursomc.repositories.CidadeRepository;
import com.amiltonleme.cursomc.repositories.ClienteRepository;
import com.amiltonleme.cursomc.repositories.EnderecoRepository;
import com.amiltonleme.cursomc.repositories.EstadoRepository;
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
	
	
	@Override
	public void run(String... args) throws Exception {
		
		//Instanciação de objetos
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		
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
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
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
		
	}

}
