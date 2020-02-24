package com.amiltonleme.cursomc.domain.enums;

public enum TipoCliente {
	
	PESSOAFISICA (1, "Pessoa fisíca"),
	PESSOAJURIDICA (2, "Pessoa jurídica");
	
	private Integer cod;
	private String descricao;
	
	private TipoCliente () {
	}

	private TipoCliente(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public int getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}

	//Esse método serve para transformar o TipoCliente para numérico 
	public static TipoCliente toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		//Esse for quer dizer: para cada valor x do TipoCliente nos valores TipoCliente...
		for (TipoCliente x : TipoCliente.values()) {
			if(cod.equals(x.getCod())) {
				return x;	
			}
		}
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
}
	

