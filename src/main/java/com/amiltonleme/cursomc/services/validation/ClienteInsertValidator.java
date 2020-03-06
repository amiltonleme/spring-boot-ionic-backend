package com.amiltonleme.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.amiltonleme.cursomc.domain.Cliente;
import com.amiltonleme.cursomc.domain.enums.TipoCliente;
import com.amiltonleme.cursomc.dto.ClienteNewDTO;
import com.amiltonleme.cursomc.repositories.ClienteRepository;
import com.amiltonleme.cursomc.resources.exception.FieldMessage;
import com.amiltonleme.cursomc.services.validation.utils.BR;

//ClienteInsert é o nome da anotação que vai para a classe ClienteNewDTO
public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	//A lógica de validação do CPFCNPJ tem que estar dentro desse método isValid
	//Esse método é o método da interface ConstraintValidator que verifica se o tipo ClienteNewDTO será válido ou não
	//Retornará verdadeiro ou falso, que será percebido no argumento da classe ClienteResource, na requisição @Valid
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())){
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));	
		}
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())){
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));	
		}
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("email", "Email já existente!"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getFieldMessage()).addPropertyNode(e.getFieldname())
					.addConstraintViolation();
		}
		
		//Se essa lista retornar vazia será verdadeiro, senão será falso
		return list.isEmpty();
	}
}
