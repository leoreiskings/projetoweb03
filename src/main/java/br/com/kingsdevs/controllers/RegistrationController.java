package br.com.kingsdevs.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.kingsdevs.dtos.PostRegistrationDTO;
import br.com.kingsdevs.entities.Usuario;
import br.com.kingsdevs.helpers.MD5Helper;
import br.com.kingsdevs.repositories.UsuarioRepository;

@Controller
public class RegistrationController {

	@RequestMapping(value = "/api/registration", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody PostRegistrationDTO dto) {
		try {
			// verificar se as senhas informadas são iguais
			if (!dto.getSenha().equals(dto.getSenhaConfirmacao())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Senhas não conferem.");
			}

			// verificar se o email informado já está cadastrado no banco de dados
			UsuarioRepository usuarioRepository = new UsuarioRepository();

			if (usuarioRepository.findByEmail(dto.getEmail()) != null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Erro: O email informado já está cadastrado, tente outro.");
			}
			
			// cadastrando o usuário no banco de dados
			Usuario usuario = new Usuario();
			usuario.setNome(dto.getNome());
			usuario.setEmail(dto.getEmail());
			usuario.setSenha(MD5Helper.encrypt(dto.getSenha()));
			
			usuarioRepository.create(usuario);
			
			return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso");
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}