package com.amiltonleme.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amiltonleme.cursomc.services.DBService;

@Configuration
@Profile("dev")
public class DevConfig {
	
	@Autowired
	private DBService dbService;
	
	/*Esse Value pega o valor "spring.jpa.hibernate.ddl-auto" do application.dev e compara para 
	ver se é create ou não para instanciar a base de dados*/
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instatiateDatabase() throws ParseException {
		if(!"create".equals(strategy)) {
			return false; 
		}
		dbService.instatiateTestDatabase();
		return true;
	}

}
