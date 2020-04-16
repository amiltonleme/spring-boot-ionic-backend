package com.amiltonleme.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.amiltonleme.cursomc.domain.Cliente;
import com.amiltonleme.cursomc.repositories.ClienteRepository;
import com.amiltonleme.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));
		
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	
	}

	private String newPassword() {
		char[] vet = new char[10];
		//Para gerar um valor randômico
		for (int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);
		if (opt == 0) { //gera um dígito
			//10 porque são dez dígitos de 0 até 9
			//48 porque é o código do 0
			//(char) está fazendo casting
			return (char) (rand.nextInt(10) + 48);
		}
		else if (opt == 1) { //gera letra maiúscula
			//26 porque são 26 letras de A até Z
			//65 porque é o código do A
			return (char) (rand.nextInt(26) + 65);
		}
		else { //gera letra minúscula
			//26 porque são 26 letras de a até z
			//97 porque é o código do a
			return (char) (rand.nextInt(26) + 97);
			
		}
	}
}