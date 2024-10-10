package br.books.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.books.entity.Usuario;
import br.books.enums.Role;
import br.books.repository.UsuarioRepository;

@Controller
@RequestMapping("/admin")
public class UsuarioAdminController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	@GetMapping("/usuarios/listar")
	public String listaUsuarios(Usuario usuario, Model model) {
		model.addAttribute("usuarios", usuarioRepository.findAll());
		return "private/admin/admin-usuario-lista";
	}

	@GetMapping("/usuarios/status")
	public String selecionaPorStatusAtivo(@RequestParam(required = false) Boolean status, Model model) {
	    List<Usuario> usuarios;

	    if (status == null) {
	        usuarios = usuarioRepository.findAll();  // Retorna todos os usuários se o status não for fornecido
	    } else {
	        usuarios = usuarioRepository.findByAtivo(status);  // Retorna usuários filtrados por status
	    }

	    model.addAttribute("usuarios", usuarios);
	    model.addAttribute("status", status);  // Para manter o valor do filtro selecionado na página
	    return "private/admin/admin-usuario-lista";
	}

	@GetMapping("/usuario/cadastra")
	public String adminCadastraUsuario(Usuario usuario, Model model) {
		model.addAttribute("usuario", new Usuario());
		return "private/admin/admin-usuario-cadastra";
	}

	@PostMapping("/usuario/cadastra")
	public String adminCadastraUsuarioPost(Usuario usuario, Model model, RedirectAttributes attributes) {
		Usuario usuarioNovo = usuarioRepository.findByLogin(usuario.getLogin());

		if (usuarioNovo != null) {
			attributes.addFlashAttribute("errorMessage", "Usuário já existe");
			return "redirect:/admin/usuario/cadastra";
		} else {
			usuario.setSenha(encoder.encode(usuario.getSenha()));

			if (usuario.getRole() == null) {
				usuario.setRole(Role.USER);
			}
			usuarioRepository.save(usuario);
			attributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso");
		}
		return "redirect:/admin/usuario/cadastra";
	}

	@GetMapping("/edita/{id}")
//	 @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String editarUsuario(@PathVariable("id") long id, Model model) {

		Optional<Usuario> usuarioLogado = usuarioRepository.findById(id);
		if (!usuarioLogado.isPresent()) {
			throw new IllegalArgumentException("Usuário inválido" + id);
		}
		Usuario usuario = usuarioLogado.get();
		model.addAttribute("usuario", usuario);
		return "private/admin/admin-usuario-edita-perfil";
	}

	@PostMapping("/edita/{id}")
	// @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String editaUsuarioPost(@PathVariable("id") long id, Usuario usuario, Model model,
			RedirectAttributes attributes) {
		Optional<Usuario> usuarioAntigo = usuarioRepository.findById(id);
		Usuario userVerificaLogin = usuarioRepository.findByLogin(usuario.getLogin());

		if (usuarioAntigo == null) {
			model.addAttribute("usuario", "usuario não encontrado");
			return "private/admin/admin-usuario-edita-perfil";
		}

		if ((userVerificaLogin != null)) {
			if (userVerificaLogin.getLogin() != usuario.getLogin()) {
				System.out.println("Alterou login.");
				attributes.addAttribute("errorMessage", "Login já cadastrado.");
			}
		}

		Usuario usr = usuarioAntigo.get();
		usr.setNome(usuario.getNome());
		usr.setLogin(usuario.getLogin());
		usr.setAtivo(usuario.isAtivo());
		usr.setRole(usuario.getRole());

		usuarioRepository.save(usr);

		attributes.addFlashAttribute("mensagem", "Alteração salva");
		return "redirect:/admin/edita/{id}";
	}

	@GetMapping("/remove/usuario/{id}")
	public String removeUsuario(@PathVariable("id") long id, Model model, RedirectAttributes attributes) {
		try {
			usuarioRepository.deleteById(id);
			attributes.addFlashAttribute("mensagem", "Usuário removido.");
		} catch (Exception e) {
			attributes.addFlashAttribute("errorMessage", "Erro ao remover.");
		}
		return "redirect:/admin/usuarios/listar";
	}
}