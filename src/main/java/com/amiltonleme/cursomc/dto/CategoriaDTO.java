package com.amiltonleme.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.amiltonleme.cursomc.domain.Categoria;

//Essa classe serve para definir os dados que se quer trafegar da classe Categoria
public class CategoriaDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	@NotEmpty(message = "Preenchimento obrigatório!")
	@Length(min = 5, max = 80, message = "O tamanho deve ter entre 5 e 80 caracteres")
	private String nome;
	
	public CategoriaDTO () {
		
	}
	
	//Construtor para pegar da classe Categoria somente os dados necessários para essa necessidade
	//Serve para instanciar a classe CategoriaDTO a partir de uma classe Categoria
	public CategoriaDTO (Categoria obj) {
		id = obj.getId();
		nome = obj.getNome();
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	
}
