package br.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.books.entity.Usuario;
import br.books.repository.UsuarioRepository;

@Controller
public class LoginController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@RequestMapping("/")
	public String loginPage() {
		return "login";
	}

	@GetMapping("login")
	public String loginAcess(@RequestParam(value = "error", required = false) String error, Model model) {
		System.out.println("login");
		if (error != null) {
			model.addAttribute("errorMessage", "Usuário ou senha inválido.");
		}
		return "/login";
	}

	@GetMapping("/home")
	public String home(@CurrentSecurityContext(expression = "authentication.name") String login,
			RedirectAttributes attributes, Model model) {

		Usuario usuario = usuarioRepository.findByLogin(login);

		if (isAdmin(usuario)) {
			return "private/home-admin";
		} else if (usuario.getRole().getRole().equals("BIBLIO")) {
			return "private/home-biblio";
		} else if (usuario.getRole().getRole().equals("USER")) {
			return "private/home";
		}
		return "acesso-negado";
	}

	public boolean isAdmin(Usuario usuario) {
		return usuario.getRole().getRole().equals("ADMIN");
	}
}