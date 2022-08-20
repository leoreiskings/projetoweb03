package br.com.cotiinformatica.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.cotiinformatica.dtos.PostRegistrationDTO;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.helpers.MD5Helper;
import br.com.cotiinformatica.repositories.UsuarioRepository;

@Controller
public class RegistrationController {

	@RequestMapping(value = "/api/registration", method = RequestMethod.POST)	
	public ResponseEntity<String> post(@RequestBody PostRegistrationDTO dto) {
		
		try {
			// verificando se as senhas informadas são iguais
			// se senha enviada no dto nao for igual a senhaConfirmação tambem vinda do dto, retorne a mensagem "Erro: Senhas não conferem." 
			if (!dto.getSenha().equals(dto.getSenhaConfirmacao())) {
				
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Erro: Senhas não conferem."); // retorna um erro 400. O usuario mandou dados que nao conferem 
			}
			
			// verificando se o email informado já está cadastrado no banco de dados
			UsuarioRepository usuarioRepository = new UsuarioRepository();
			
			if (usuarioRepository.findByEmail(dto.getEmail()) != null) { // se o metodo retorna algo diferente de null, significa que achou o email, 
																		//portanto ele já está cadastrado no banco de dados e nao dá para cadastrar novamente
																		// já que email é unico por usuario. retorna a msg "Erro: O email informado já está cadastrado, tente outro."
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Erro: O email informado já está cadastrado, tente outro.");
			}
			
			// cadastrando o usuário no banco de dados 
			Usuario usuario = new Usuario();
			
			usuario.setNome(dto.getNome()); // seta o "nome" vindo pelo dto e seta no objeto que será mandando para criação de usuario
			usuario.setEmail(dto.getEmail()); // seta o "email" vindo pelo dto e seta no objeto que será mandando para criação de usuario
			usuario.setSenha(MD5Helper.encrypt(dto.getSenha())); // seta a "senha" vinda pelo dto e passa a senha criptograda para ser gravada no Banco de dados 
			
			usuarioRepository.create(usuario);

			return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage());
		}
	}
}
