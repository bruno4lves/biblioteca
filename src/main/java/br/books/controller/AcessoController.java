package br.books.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/acesso")
public class AcessoController {

	@GetMapping("/servicos")
	public String paginaServico(Model model) {
		return "private/user/servicos";
	}
}