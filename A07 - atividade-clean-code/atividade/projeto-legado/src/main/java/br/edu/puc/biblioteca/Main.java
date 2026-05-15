package br.edu.puc.biblioteca;

public class Main {

    public static void main(String[] args) {

        // cadastra usuarios
        Bib.proc(4, "Ana Silva",     "20230001", null, false);  // aluno
        Bib.proc(4, "Carlos Ramos",  "20230002", null, true);   // professor

        // cadastra livros
        Bib.proc(3, "Codigo Limpo",           "Robert C. Martin", "978-0132350884", false);
        Bib.proc(3, "Design Patterns",        "Gang of Four",     "978-0201633610", false);
        Bib.proc(3, "The Pragmatic Programmer","David Thomas",     "978-0135957059", false);

        // emprestimos
        String r1 = Bib.proc(1, "978-0132350884", "20230001", null, false);
        System.out.println("Emprestimo 1: " + r1);

        String r2 = Bib.proc(1, "978-0132350884", "20230002", null, false);
        System.out.println("Emprestimo 2 (deve falhar - livro ja emprestado): " + r2);

        // listagem
        Bib.lst(true);
        Bib.lst(false);

        // devolucao sem multa
        String r3 = Bib.proc(2, "978-0132350884", null, null, false);
        System.out.println("Devolucao: " + r3);

        // devolucao com multa
        String r4 = Bib.proc(2, "978-0201633610", null, null, true);
        System.out.println("Devolucao com multa: " + r4);

        // busca
        Livro l = Bib.bsc("Pragmatic");
        System.out.println("Busca: " + (l != null ? l.t : "nao encontrado"));
    }
}
