package br.books.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	@RequestMapping("/")
	public String loginPage() {
		return "login";
	}
	
	@GetMapping("login")
	public String loginAcess() {
		return "/login";
	}
	
	@GetMapping("/home")
	public String home() {
		return "private/home";	
	}
}