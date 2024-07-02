package com.literalura.literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "autor_Id")
    private Long autorId;
    private String nombreAutor;
    @Column(name = "fecha_nacimiento")
    private Integer fechaNacimiento;
    @Column(name = "fecha_fallecimiento")
    private Integer fechaFallecimiento;
    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY)
    private List<Libros> libros;

    public Autor(String nombre, int fechaNacimiento, int fechaFallecimiento, List<Libros> libros) {
        this.nombreAutor = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaFallecimiento = fechaFallecimiento;
        this.libros = libros;
    }

    public Autor(List<DatosAutor> infoAutor) {
    }

    public Autor() {

    }
//    public Autor(String nombreAutor, Integer fechaNacimiento, Integer fechaFallecimiento, Object o) {
//    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(Integer fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public List<Libros> getLibrosAutor() {
        return libros;
    }

    public void setLibrosAutor(List<Libros> librosAutor) {
        this.libros = librosAutor;
    }


}
