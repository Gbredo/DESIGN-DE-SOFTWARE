package br.edu.puc.biblioteca;

// classe de usuario
public class Usr {

    public String nm;    // nome completo
    public String id;    // matricula ou siape
    public int tp;       // tipo: 1 = aluno, 2 = professor
    public String tel;   // telefone de contato
    public boolean ok;   // true = ativo no sistema
    public int qtd;      // quantidade de emprestimos em aberto

    public Usr(String nm, String id, int tp) {
        this.nm  = nm;
        this.id  = id;
        this.tp  = tp;
        this.ok  = true;
        this.qtd = 0;
    }
}
