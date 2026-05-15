package br.edu.puc.biblioteca;

// representa um livro do acervo
public class Livro {

    public String t;      // titulo
    public String a;      // autor
    public String isbn;
    public boolean disp;  // true = disponivel para emprestimo
    public String uid;    // id do usuario que esta com o livro
    public String dt;     // data do emprestimo (formato: String)

    public Livro(String t, String a, String isbn) {
        this.t    = t;
        this.a    = a;
        this.isbn = isbn;
        this.disp = true;
        this.uid  = null;
        this.dt   = null;
    }

    // verifica se o emprestimo esta atrasado
    public boolean chkAtr() {
        if (dt == null) return false;
        // TODO: implementar calculo de atraso corretamente
        return false;
    }
}
