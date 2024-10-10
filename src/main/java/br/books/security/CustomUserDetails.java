package br.books.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.books.entity.Usuario;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final Usuario usuario;
	private final Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		this.usuario = usuario;
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		return usuario.getLogin();
	}

	public String getNome() {
		return usuario.getNome();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return usuario.getSenha();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return usuario.isAtivo();
	}

	public Usuario getUsuario() {
		return this.usuario;
	}
}