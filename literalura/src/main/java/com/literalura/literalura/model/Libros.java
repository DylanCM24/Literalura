package com.literalura.literalura.model;

import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "Libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_Id")
    private Long bookId;
    @Column(unique = true)
    private String titulo;
    private String idiomas;
    private Double numerodeDescargas;
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libros(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.idiomas = datosLibros.idiomas().get(0).toUpperCase();
        this.numerodeDescargas = datosLibros.numeroDeDescargas();
    }

    public Libros(){

    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumerodeDescargas() {
        return numerodeDescargas;
    }

    public void setNumerodeDescargas(Double numerodeDescargas) {
        this.numerodeDescargas = numerodeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }


}
