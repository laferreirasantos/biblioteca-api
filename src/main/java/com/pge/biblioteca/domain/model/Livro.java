package com.pge.biblioteca.domain.model;

import jakarta.persistence.*;


@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(name = "quantidadeDisponivel", nullable = false)
    private Integer quantidadeDisponivel; 


    // Construtor padrão exigido pelo JPA
    public Livro() {
    }

    public Livro(String titulo, String autor, String isbn, Integer quantidadeDisponivel) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }
 // Getters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getQuantidadeDisponivel() {
		return quantidadeDisponivel;
	}

	public void setQuantidadeDisponivel(Integer quantidadeDisponivel) {
		this.quantidadeDisponivel = quantidadeDisponivel;
	}

	public boolean possuiDisponibilidade() {
        return this.quantidadeDisponivel > 0;
    }

    public void reduzirQuantidadeDisponivel() {
        if (!possuiDisponibilidade()) {
            throw new IllegalStateException("Não há unidades disponíveis para empréstimo.");
        }
        this.quantidadeDisponivel--;
    }

    public void aumentarQuantidadeDisponivel() {
        this.quantidadeDisponivel++;
    }
    
    

	
}
