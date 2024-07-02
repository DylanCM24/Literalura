package com.literalura.literalura.principal;

import com.literalura.literalura.model.*;
import com.literalura.literalura.repository.AutorRepository;
import com.literalura.literalura.repository.LibroRepository;
import com.literalura.literalura.service.ConsumoAPI;
import com.literalura.literalura.service.ConvierteDatos;

import java.util.*;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    //    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repositorioLibro;
    private AutorRepository repositorioAutor;
    private List<DatosLibros> datosLibros;
    private Optional<DatosLibros> libroBuscado;

    public Principal(LibroRepository repositorioLibro, AutorRepository repositorioAutor) {
        this.repositorioLibro = repositorioLibro;
        this.repositorioAutor = repositorioAutor;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Lista de libros registrados
                    3 - Listar Autores
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            try {
                Scanner teclado = new Scanner(System.in);
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e){
                System.out.println("Entrada invalida. Digite una opcion valida");
                teclado.nextLine();
                continue;
            }


            switch (opcion) {

                case 1:
                    buscarLibroWeb();
                    break;

                case 2:
                    mostarLibros();
                    break;

                case 3:
                    mostarAutores();
                    break;

                case 4:
                    fechasAutor();
                    break;

                case 5:
                    librosPorIdioma();
                    break;

                case 0:
                    System.out.println("Saliendo del programa");
                    break;

                default:
                    System.out.println("Opcion no valida");
            }

        }
    }


    private Optional<DatosLibros> getDatosLibros() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.libros().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println(
                    "\n---------------LIBRO--------------" +
                            "\nTitulo:" + libroBuscado.get().titulo() +
                            "\nAutor:" + libroBuscado.get().autor().get(0).nombre() +
                            "\nIdioma:" + libroBuscado.get().idiomas() +
                            "\nNúmero de descargas:" + libroBuscado.get().numeroDeDescargas() +
                            "\n-------------------------------------\n"

            );
        } else {
            System.out.println("El libro no fue encontrado");
        }
        return libroBuscado;


    }

    private void buscarLibroWeb() {
        Optional<DatosLibros> datos = getDatosLibros();
        if (datos.isPresent()) {
            DatosLibros libro = datos.get();
            var autores = libro.autor();
            if (!autores.isEmpty()) {
                String nombreAutor = autores.get(0).nombre() != null ? autores.get(0).nombre() : "Desconocido/a";
                int fechaNacimiento = autores.get(0).fechaNacimiento() != null ? autores.get(0).fechaNacimiento() : 0;
                int fechaFallecimiento = autores.get(0).fechaFallecimiento() != null ? autores.get(0).fechaFallecimiento() : 0;
                Optional<Autor> autorExiste = repositorioAutor.findByNombreAutor(nombreAutor);
                Autor autorGuardar;
                if (autorExiste.isPresent()) {
                    autorGuardar = autorExiste.get();
                    System.out.println("Autor ya registrado en la base de datos" + nombreAutor);
                } else {
                    autorGuardar = new Autor(nombreAutor, fechaNacimiento, fechaFallecimiento, null);
                    repositorioAutor.save(autorGuardar);
                    System.out.println("Nuevo autor registrado: " + nombreAutor);
                }
                String titulo = libro.titulo();
                Optional<Libros> libroExiste = repositorioLibro.findByTitulo(titulo);
                if (libroExiste.isPresent()) {
                    System.out.println("El libro ya esta registrado en la base de datos " + titulo);
                } else {
                    Libros libroGuardar = new Libros(libro);
                    libroGuardar.setAutor(autorGuardar);
                    repositorioLibro.save(libroGuardar);
                    System.out.println("Nuevo libro registrado: " + titulo);
                }
            } else {
                System.out.println("El libro no tiene autor especifico");
            }
        } else {
            System.out.println("No se encontraron datos de libros para registrar");
        }
    }

    private List<Libros> mostarLibros() {
        List<Libros> libros = repositorioLibro.findAll();
        libros.forEach(libro -> {
            System.out.println(
                    "\n---------------LIBRO--------------" +
                            "\nTitulo:" + libro.getTitulo() +
                            "\nAutor:" + libro.getAutor() +
                            "\nIdioma:" + libro.getIdiomas() +
                            "\nNúmero de descargas:" + libro.getNumerodeDescargas() +
                            "\n-------------------------------------\n");

        });
        return libros;
    }

    private void mostarAutores() {
        List<Autor> autores = repositorioAutor.findAllAutorWithLibros();
        autores.forEach(autor -> {
            System.out.println(
                    "\n--------------------informacio de autor---------------------" +
                            "\nAutor: " + autor.getNombreAutor() +
                            "\nFecha de Nacimiento: " + autor.getFechaNacimiento() +
                            "\nFecha de Fallecimiento: " + autor.getFechaFallecimiento() +
                            "\nLibros");
            List<String> titulosLibros = new ArrayList<>();
            autor.getLibrosAutor().forEach(libros -> {
                titulosLibros.add(libros.getTitulo());
            });

            System.out.println("[" + String.join(",", titulosLibros) + "]");
            System.out.println("\n------------------------------------------------\n");

        });
    }

    private List<Autor> fechasAutor() {
        System.out.println("Ingrese un año para ver los autores con vida de la época");
        List<Autor> autores = null;
        try {
            int year = teclado.nextInt();
            teclado.nextLine();

            autores = repositorioAutor.findAutorByFecha(year);

            if (autores.isEmpty()) {
                System.out.println("No hay registros en el año " + year);
            } else {
                autores.forEach(autor -> {
                    List<String> titulosLibros = new ArrayList<>();
                    autor.getLibrosAutor().forEach(libros -> {
                        titulosLibros.add(libros.getTitulo());
                    });
                    String librosConcatenados = String.join(",", titulosLibros);
                    System.out.println(
                            "\nAutor vivo en los años " + year +
                                    "\n------------------------------------" +
                                    "\nAutor: " + autor.getNombreAutor() +
                                    "\nFecha Nacimiento: " + autor.getFechaNacimiento() +
                                    "\nFecha Fallecimiento: " + autor.getFechaFallecimiento() +
                                    "\nLibros:  [" + librosConcatenados + "]" +
                                    "\n--------------------------------------"
                    );
                });

            }
        } catch (InputMismatchException e) {
            System.out.println("Año no valido");
            teclado.nextLine();
        }
        return autores;
    }

    private void librosPorIdioma() {
        System.out.println("\n Ingrese el idioma: " +
                "\n-----------------------------------" +
                "\nes - Español" +
                "\nen - Inglés " +
                "\nfr - Francés" +
                "\npt - Portugués" +
                "\n------------------------------------\n"
        );
        String idiomas = teclado.nextLine().toUpperCase();

        List<Libros> libros = repositorioLibro.findLibrosByIdiomas(idiomas);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma buscado");
        } else {
            System.out.println("\nLibros encontrados en " + idiomas);
            for (Libros libro : libros) {
                System.out.println(
                        "\n--------------Libro Encontrado--------------" +
                                "\nTitulo: " + libro.getTitulo() +
                                "\nAutor: " + libro.getAutor().getNombreAutor() +
                                "\nIdiomas: " + libro.getIdiomas() +
                                "\nNumero De Descargas: " + libro.getNumerodeDescargas() +
                                "\n-------------------------------------------\n");

            }
        }
    }


}