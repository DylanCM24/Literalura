package com.literalura.literalura.repository;

import com.literalura.literalura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libros, Long>{
    Optional<Libros> findByTitulo(String titulo);

    @Query("SELECT l FROM Libros l WHERE l.idiomas = :idiomas")
    List<Libros> findLibrosByIdiomas(@Param("idiomas") String idiomas);
}

//El repositorio es para usar el CRUD(Create, read, update, delete)
