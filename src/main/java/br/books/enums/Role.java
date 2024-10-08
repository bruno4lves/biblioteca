package br.books.enums;

public enum Role {

	ADMIN("ADMIN"), USER("USER"), BIBLIO("BIBLIO");

	private final String role;

	Role(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
}