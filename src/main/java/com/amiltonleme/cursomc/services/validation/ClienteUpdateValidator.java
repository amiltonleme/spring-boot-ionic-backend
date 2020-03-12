package com.amiltonleme.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.amiltonleme.cursomc.domain.Cliente;
import com.amiltonleme.cursomc.dto.ClienteDTO;
import com.amiltonleme.cursomc.repositories.ClienteRepository;
import com.amiltonleme.cursomc.resources.exception.FieldMessage;

//ClienteUpdate é o nome da anotação que vai para a classe ClienteDTO
public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	//Objeto para pegar o id dentro da URI
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	//A lógica de validação do CPFCNPJ tem que estar dentro desse método isValid
	//Esse método é o método da interface ConstraintValidator que verifica se o tipo ClienteNewDTO será válido ou não
	//Retornará verdadeiro ou falso, que será percebido no argumento da classe ClienteResource, na requisição @Valid
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		//Map é uma coleção de chaves pares/valor
		//Esse método serve para pegar os atributos que são armazenados dentro de um Map
		//(Map <String, String>) é pra fazer o casting para transformar o request no tipo map
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map <String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null && !aux.getId().equals(uriId)) {
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
