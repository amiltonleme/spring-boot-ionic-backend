package com.amiltonleme.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amiltonleme.cursomc.domain.Cidade;
import com.amiltonleme.cursomc.domain.Cliente;
import com.amiltonleme.cursomc.domain.Endereco;
import com.amiltonleme.cursomc.domain.enums.Perfil;
import com.amiltonleme.cursomc.domain.enums.TipoCliente;
import com.amiltonleme.cursomc.dto.ClienteDTO;
import com.amiltonleme.cursomc.dto.ClienteNewDTO;
import com.amiltonleme.cursomc.repositories.ClienteRepository;
import com.amiltonleme.cursomc.repositories.EnderecoRepository;
import com.amiltonleme.cursomc.security.UserSS;
import com.amiltonleme.cursomc.services.exceptions.AuthorizationException;
import com.amiltonleme.cursomc.services.exceptions.DataIntegrityException;
import com.amiltonleme.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;
	
// Implementação utlilizada para Spring Boot 2.x.x em diante.
	public Cliente find(Integer id) {
		UserSS user = UserService.authenticated();
			if(user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
				throw new AuthorizationException("Acesso negado");
			}
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));	
	
// Implementação utilizada para Spring Boot até versão anterior.	
//	public Cliente find(Integer id) {
//		Optional<Cliente> obj = repo.findById(id);
//		if (obj == null) {
//			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName());	
//		}
//		
//		return obj.orElse(null);
	}
	
		@Transactional
	public Cliente insert(Cliente obj) {
		/* ao se setar ID com NULL se garante que será criado um novo objeto. Caso
		contrário, se houver um ID especificado será atualizado o objeto
		correspondente.*/
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return repo.save(obj);
	}
	
	public Cliente update(Cliente obj) {
		/* Se houver um ID especificado será atualizado o objeto
		correspondente.*/
		//Instancia um objeto a partir do banco de dados e esse objeto estará monitorado pelo JPA
		Cliente newObj = find(obj.getId());
		//método auxiliar para atualizar os dados do novo objeto newObj com base no objeto que veio como argumento
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id){
		find(id);
		try {
		repo.deleteById(id);
		}
		//Se tentar apagar uma Cliente que contenha produto, a exception será tratada abaixo
		catch (DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possível excluir um cliente que contenha pedidos relacionados");
		}
	}
	
	public List<Cliente> findAll (){
		return repo.findAll();
	}
		
	//Método que retorna o cliente por email
	public Cliente findByEmail(String email) {
		//Procura o usuário que está autenticado
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}

		//Retorna os dados do cliente pelo email
		Cliente obj = repo.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
	}

	public Page<Cliente> findPage (Integer page, Integer linesPerPage, String orderBy, String direction){
		//PageRequest Está no pacote Spring Data
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
		
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()),pe.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cid, cli);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}
	
	//Private porque é um método auxiliar dentro da classe
	//Atualiza os dados do objeto newObj como os novos dados que vieram do objeto obj
	private void updateData (Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public URI uploadProfilePicture (MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (user==null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		//recebe a imagem extraida do MultipartFile multipartFile
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		//Recorta a imagem para que fique quadrada
		jpgImage = imageService.cropSquare(jpgImage);
		//Redimensiona a imagem
		jpgImage = imageService.resize(jpgImage, size);
		//Monta o nome do arquivo personalizado
		String fileName = prefix + user.getId() + ".jpg";
		
		//InputStream = imageService.getInputStream(jpgImage, "jpg")
		//fileName = fileName
		//contentType = "image"
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}

}
