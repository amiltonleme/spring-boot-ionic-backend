package com.amiltonleme.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amiltonleme.cursomc.services.DBService;
import com.amiltonleme.cursomc.services.EmailService;
import com.amiltonleme.cursomc.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instatiateDatabase() throws ParseException {
		dbService.instatiateTestDatabase();
		return true;
	}
	
	
	//Quando faz um método com anotação @Bean, esse método ficará disponível no nosso sistema
	/*Se em outra classe eu faço uma injeção de dependência, como na classe PedidoService, o Spring vai 
	procurar um componente que pode ser um @Bean*/
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}

}
