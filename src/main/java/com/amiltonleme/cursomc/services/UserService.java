package com.amiltonleme.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.amiltonleme.cursomc.security.UserSS;

public class UserService {
	
	//Esse método é static porque pode ser chamado sem te que instanciar a classe
	public static UserSS authenticated() {
		
		try {
			//O (UserSS) é pra fazer um casting
			/*Funçaõ do Spring Security "SecurityContextHolder.getContext().getAuthentication().getPrincipal()" 
			para pegar o usuário logado.*/
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch (Exception e){
			return null;
		}
	
	}

}
