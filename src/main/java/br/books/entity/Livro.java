package br.books.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_livro")
public class Livro {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "O nome do livro é obrigatório")
	@Size(min = 2, max = 100, message = "O nome do livro deve ter entre 2 e 100 caracteres")
	private String nomeLivro;

	@NotBlank(message = "O título do livro é obrigatório")
	@Size(min = 2, max = 150, message = "O título do livro deve ter entre 2 e 150 caracteres")
	private String tituloLivro;

	private String livroDescricao;

	private String imagemCapa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeLivro() {
		return nomeLivro;
	}

	public void setNomeLivro(String nomeLivro) {
		this.nomeLivro = nomeLivro;
	}

	public String getTituloLivro() {
		return tituloLivro;
	}

	public void setTituloLivro(String tituloLivro) {
		this.tituloLivro = tituloLivro;
	}

	public String getImagemCapa() {
		return imagemCapa;
	}

	public void setImagemCapa(String imagemCapa) {
		this.imagemCapa = imagemCapa;
	}

	public String getLivroDescricao() {
		return livroDescricao;
	}

	public void setLivroDescricao(String livroDescricao) {
		this.livroDescricao = livroDescricao;
	}
}