package br.books.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.books.entity.Usuario;
import br.books.repository.UsuarioRepository;

@Service // Essa anotação registra a classe como um bean no Spring
//@Transactional
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	public UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByLogin(username);
		if(usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado: " + username);
		}

		return org.springframework.security.core.userdetails.User
	            .withUsername(usuario.getLogin())
	            .password(usuario.getSenha()) 		// As senhas devem ser criptografadas
	            .roles(usuario.getRole().getRole()) // Adiciona os papéis do usuário
	            .build();
	}
	
//	@Override
//	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
//	    Usuario usuario = usuarioService.findByLogin(login);
//	    if (usuario == null) {
//	        throw new UsernameNotFoundException("Usuário não encontrado");
//	    }
//
//	    return org.springframework.security.core.userdetails.User
//	            .withUsername(usuario.getLogin())
//	            .password(usuario.getPassword())
//	            .roles(usuario.getRole().getRole())
//	            .accountExpired(false)
//	            .accountLocked(false)
//	            .credentialsExpired(false)
//	            .disabled(!usuario.isAtivo())
//	            .build();
//	}
}