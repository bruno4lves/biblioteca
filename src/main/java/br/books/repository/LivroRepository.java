package br.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.books.entity.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long>{

	//Livro finById(Long id);
	Livro findByNomeLivro(String nomeLivro);
}