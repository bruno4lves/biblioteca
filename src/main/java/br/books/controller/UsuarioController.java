package br.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.books.entity.Usuario;
import br.books.enums.Role;
import br.books.repository.UsuarioRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	@GetMapping("/cadastro")
	public String cadastroUsuario(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "/public/usuario-cadastro";
	}

	@PostMapping("/cadastro")
	public String cadastraUsuarioPost(@Valid Usuario usuario, Model model, RedirectAttributes attributes) {
	    Usuario usuarioNovo = usuarioRepository.findByLogin(usuario.getLogin());
	    
	    if (usuarioNovo != null) {
	        attributes.addFlashAttribute("errorMessage", "Usuário já existe");
	        return "redirect:/usuario/cadastro";
	    } else {
	    	usuario.setSenha(encoder.encode(usuario.getSenha()));

	        if (usuario.getRole() == null) {
	            usuario.setRole(Role.USER);
	        }
	        usuarioRepository.save(usuario);
	    }
	    attributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso");
	    return "redirect:/usuario/cadastro";
	}

	@GetMapping("/edita/perfil")
	public String editaPerfil(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String login = authentication.getName();

		Usuario usuarioAntigo = usuarioRepository.findByLogin(login);

		if (usuarioAntigo == null) {
			throw new IllegalArgumentException("Usuário inválido");
		}

		Usuario usuario = new Usuario();
		usuario.setNome(usuarioAntigo.getNome());
		usuario.setLogin(usuarioAntigo.getLogin());
		model.addAttribute("usuario", usuario);

		System.out.println(usuarioAntigo.getRole());
		return "private/user/usuario-edita-perfil";
	}

	@PostMapping("/edita/perfil")
	public String informacoesPessoaisPost(Usuario usuario, Model model, RedirectAttributes attributes) {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentUsername = authentication.getName();

	    // Verifica se o novo login já está em uso
	    Usuario usrVerifica = usuarioRepository.findByLogin(usuario.getLogin());
	    if (usrVerifica != null && !usrVerifica.getLogin().equals(currentUsername)) {
	        attributes.addFlashAttribute("errorMessage", "Login já existe cadastrado");
	        return "redirect:/usuario/edita/perfil";
	    }

	    // Obtém o usuário logado
	    Usuario usuarioLogado = usuarioRepository.findByLogin(currentUsername);
	    
	    // Atualiza as informações
	    usuarioLogado.setNome(usuario.getNome());
	    usuarioLogado.setLogin(usuario.getLogin());
	    usuarioRepository.save(usuarioLogado);

	    // Atualiza o contexto de segurança com o novo login
	    Authentication newAuth = new UsernamePasswordAuthenticationToken(
	            usuarioLogado.getLogin(), // o novo login
	            authentication.getCredentials(),
	            authentication.getAuthorities()
	    );
	    SecurityContextHolder.getContext().setAuthentication(newAuth);

	    model.addAttribute("usuario", usuarioLogado);

	    attributes.addFlashAttribute("mensagem", "Alteração salva com sucesso!");
	    return "redirect:/usuario/edita/perfil";
	}
	
	@GetMapping("/altera/senha")
	public String alteraSenha(Usuario usuario, Model model, RedirectAttributes attributes) {
		
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String username = authentication.getName();
    	Usuario usuarioLogado = usuarioRepository.findByLogin(username);

    	model.addAttribute("usuario", usuarioLogado);
		return "private/user/usuario-edita-senha";
	}
	
	@PostMapping("/altera/senha")
	public String alteraSenhaPost(Usuario usuario, Model model, RedirectAttributes attributes) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		Usuario usuarioAntigo = usuarioRepository.findByLogin(username);
		
		if(usuarioAntigo == null) {
			attributes.addFlashAttribute("usuario", "Usuário inválido");
		} else {
			usuario.setSenha(encoder.encode(usuario.getSenha()));
			usuarioAntigo.setSenha(usuario.getSenha());
			usuarioRepository.save(usuarioAntigo);

			attributes.addFlashAttribute("mensagem", "Alteração feita.");
		}
		return "redirect:/usuario/altera/senha";
	}
}