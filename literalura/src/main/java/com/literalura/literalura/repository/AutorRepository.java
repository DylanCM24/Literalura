package com.literalura.literalura.repository;

import com.literalura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombreAutor(String nombreAutor);

    @Query("SELECT a FROM Autor a JOIN FETCH a.libros")
    List<Autor> findAllAutorWithLibros();

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros WHERE a.fechaNacimiento <= :year AND a.fechaFallecimiento >= :year")
   List<Autor> findAutorByFecha(@Param("year") int year);

}
