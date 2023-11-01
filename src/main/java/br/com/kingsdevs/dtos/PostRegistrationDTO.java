package br.com.kingsdevs.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRegistrationDTO {

	private String nome;
	private String email;
	private String senha;
	private String senhaConfirmacao;
	
}
