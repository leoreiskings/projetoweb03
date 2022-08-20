package br.com.cotiinformatica.controllers;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.cotiinformatica.dtos.PostPasswordRecoverDTO;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.helpers.MD5Helper;
import br.com.cotiinformatica.messages.EmailMessage;
import br.com.cotiinformatica.repositories.UsuarioRepository;

@Controller
public class PasswordRecoverController {

	@RequestMapping(value = "/api/password-recover", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody PostPasswordRecoverDTO dto) {
	
		try {
			
			UsuarioRepository usuarioRepository = new UsuarioRepository();			
			Usuario usuario = usuarioRepository.findByEmail(dto.getEmail());
			
			if(usuario == null) {
				
				return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body("Usuário não encontrado.");
				}
			
				String novaSenha = String.valueOf(new Random().nextInt(999999999));
				
				//enviando a nova senha para o email do usuário
				EmailMessage.sendMessage(usuario.getEmail(), "Recuperação de Senha - API de Autenticação de Usuários", "Olá, " + usuario.getNome() + 
															 "\n\nSua senha foi redefinida com sucesso para "+ 
															 "que você possa acessar a API de autenticação." +
															 "\nUtilize a senha: " + novaSenha + "\n\nAtt\nEquipe API Usuários.");			
				
				//atualizando a senha no banco de dados
				usuarioRepository.updateSenha(usuario.getIdUsuario(), MD5Helper.encrypt(novaSenha));
				
				return ResponseEntity
						.status(HttpStatus.OK)
						.body("Recuperação de senha realizado com sucesso!");
				
			
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage());
		}
		
	}
}
