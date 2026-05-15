package br.edu.puc.biblioteca.dominio;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Representa um livro do acervo da biblioteca.
 *
 * Diferenças em relação ao código original:
 *   - Campos privados com nomes descritivos (titulo, autor, disponivel).
 *   - O empréstimo ativo é encapsulado em um objeto Emprestimo, não em dois campos soltos.
 *   - Retorna Optional<Emprestimo> em vez de null.
 *   - Métodos emprestar() e devolver() expressam comportamento, não acesso a campo.
 *   - O cálculo de atraso é delegado ao objeto Emprestimo, que usa LocalDate.
 */
public class Livro {

    private final String isbn;
    private final String titulo;
    private final String autor;
    private boolean disponivel;
    private Emprestimo emprestimoAtivo;

    public Livro(String isbn, String titulo, String autor) {
        this.isbn           = isbn;
        this.titulo         = titulo;
        this.autor          = autor;
        this.disponivel     = true;
        this.emprestimoAtivo = null;
    }

    public void emprestar(String matriculaUsuario) {
        this.disponivel      = false;
        this.emprestimoAtivo = new Emprestimo(isbn, matriculaUsuario, LocalDate.now());
    }

    public void devolver() {
        this.disponivel      = true;
        this.emprestimoAtivo = null;
    }

    /** Retorna o empréstimo ativo, ou Optional.empty() se o livro está disponível. */
    public Optional<Emprestimo> getEmprestimoAtivo() {
        return Optional.ofNullable(emprestimoAtivo);
    }

    // --- getters ---

    public String getIsbn()    { return isbn; }
    public String getTitulo()  { return titulo; }
    public String getAutor()   { return autor; }
    public boolean isDisponivel() { return disponivel; }
}
