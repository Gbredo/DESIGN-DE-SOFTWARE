package br.edu.puc.biblioteca.dominio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa um empréstimo ativo de um livro.
 *
 * Substitui os campos soltos uid e dt de String presentes em Livro no código original.
 * O uso de LocalDate em vez de String permite cálculo real de atraso.
 */
public class Emprestimo {

    private final String isbn;
    private final String matriculaUsuario;
    private final LocalDate dataEmprestimo;

    public Emprestimo(String isbn, String matriculaUsuario, LocalDate dataEmprestimo) {
        this.isbn             = isbn;
        this.matriculaUsuario = matriculaUsuario;
        this.dataEmprestimo   = dataEmprestimo;
    }

    /**
     * Retorna o número de dias em atraso em relação ao prazo informado.
     * Retorna 0 se o empréstimo ainda está dentro do prazo.
     */
    public long diasEmAtraso(int prazoEmDias) {
        LocalDate vencimento = dataEmprestimo.plusDays(prazoEmDias);
        if (!LocalDate.now().isAfter(vencimento)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(vencimento, LocalDate.now());
    }

    // --- getters ---

    public String getIsbn()              { return isbn; }
    public String getMatriculaUsuario()  { return matriculaUsuario; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
}
