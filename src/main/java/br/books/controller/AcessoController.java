package br.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.books.entity.Usuario;
import br.books.repository.UsuarioRepository;

@Controller
@RequestMapping("/acesso")
public class AcessoController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("/servicos")
	public String paginaServico(@CurrentSecurityContext(expression = "authentication.name") String login, Model model) {
		Usuario usuario = usuarioRepository.findByLogin(login);
		
		String role = usuario.getRole().getRole();
		switch (role) {
		case "USER" -> {return "private/user/servicos";}
		case "ADMIN" -> {return "private/user/servicos-admin";}
		case "BIBLIO" -> {return "private/user/servicos-biblio";}
		}
		return "private/user/servicos";
	}
}