package br.books.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.books.entity.Livro;
import br.books.entity.Usuario;
import br.books.repository.LivroRepository;
import br.books.repository.UsuarioRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/livro")
public class LivroController {
	
	@Autowired
	private LivroRepository livroRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/cadastro")
	public String cadastraLivro(Model model) {
		model.addAttribute("livro", new Livro());
		return "private/biblio/cadastro-livro";
	}
	
	@PostMapping("/cadastro")
	public String cadastraLivroPost(@Valid Livro livro, @RequestParam("imagemCapaBase64") String imagemCapaBase64,
	                                 BindingResult result,
	                                 RedirectAttributes attributes) {
	    
	    // Verifica erros de validação para os campos do livro
	    if (result.hasErrors()) {
	        attributes.addFlashAttribute("errorMessage", "Erros de validação: " + result.getAllErrors());
	        return "redirect:/livro/cadastro";  // Redireciona de volta para o formulário
	    }

	    Livro novoLivro = livroRepository.findByNomeLivro(livro.getNomeLivro());

	    if (novoLivro != null) {
	        attributes.addFlashAttribute("errorMessage", "Livro já cadastrado.");
	    } else {
	        try {
	            // Decodifica a string Base64
	            byte[] imageBytes = Base64.getDecoder().decode(imagemCapaBase64);
	            String imagemCapaNome = livro.getNomeLivro() + ".png"; // Nome do arquivo (pode personalizar)
	            File dest = new File("C:\\Users\\Bruno\\Pictures\\" + imagemCapaNome);
	            try (FileOutputStream fos = new FileOutputStream(dest)) {
	                fos.write(imageBytes);
	            }
	            livro.setImagemCapa(imagemCapaNome);  // Atualiza o campo imagemCapa do objeto Livro
	            livroRepository.save(livro);
	            attributes.addFlashAttribute("mensagem", "Livro cadastrado com sucesso!");
	        } catch (IOException e) {
	            attributes.addFlashAttribute("errorMessage", "Falha ao enviar a imagem: " + e.getMessage());
	        }
	    }
	    return "redirect:/livro/cadastro";
	}
	
	@GetMapping("/listar")
	public String listaDeLivros(Usuario usuario, Livro livro, Model model) {
		model.addAttribute("livros", livroRepository.findAll());
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String login = authentication.getName();
		
		usuario = usuarioRepository.findByLogin(login);
		
		String role = usuario.getRole().getRole();
		switch (role) {
		case "ADMIN" -> {return "private/biblio/lista-de-livros-admin";}
		case "BIBLIO" -> {return "private/biblio/lista-de-livros";}
		}
		return "private/biblio/lista-de-livros";
		
	}
	
	@GetMapping("/edita/{id}")
	public String editaLivro(@PathVariable("id") long id, Model model, RedirectAttributes attributes) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String login = authentication.getName();
		
		Optional<Livro> livroAntigo = livroRepository.findById(id);

		Livro livro = livroAntigo.get();
		model.addAttribute("livro", livro);
		
		Usuario usuario = usuarioRepository.findByLogin(login);

		if (isAdmin(usuario)) {
			return "private/biblio/edita-livros-admin";
		} else if (usuario.getRole().getRole().equals("BIBLIO")) {
			return "private/biblio/edita-livros";
		} else if (usuario.getRole().getRole().equals("USER")) {
			return "private/biblio/edita-livros";
		}
		return "acesso-negado";
	}
	
	@PostMapping("/edita/{id}")
	public String editaLivroPost(@PathVariable("id") long id, Livro livro, Model model, RedirectAttributes attributes) {
		
		Optional<Livro> livroAntigo = livroRepository.findById(id);
		if(livroAntigo == null) {
			System.out.println("Livro cadastrado no banco de dados");
		}
		Livro livroAlteracoes = livroAntigo.get();
		livroAlteracoes.setNomeLivro(livro.getNomeLivro());
		livroAlteracoes.setTituloLivro(livro.getTituloLivro());
		
		livroRepository.save(livroAlteracoes);
		
		attributes.addFlashAttribute("mensagem", "Alterações salvas.");
		return "redirect:/livro/edita/{id}";
		//return "private/biblio/edita-livros";
	}
	
	@GetMapping("/remove/{id}")
	public String removeUsuario(@PathVariable("id") long id, RedirectAttributes attributes) {
		livroRepository.deleteById(id);
		attributes.addFlashAttribute("mensagem", "Usuário removido");
		return "redirect:/livro/listar";
	}
	
	public boolean isAdmin(Usuario usuario) {
	    return usuario.getRole().getRole().equals("ADMIN");
	}
}