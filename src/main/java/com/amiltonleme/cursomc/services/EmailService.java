package com.amiltonleme.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.amiltonleme.cursomc.domain.Pedido;

public interface EmailService {
	
	void senderOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg); 

}
