package br.edu.puc.biblioteca;

import java.util.ArrayList;

// gerencia todas as operacoes da biblioteca
public class Bib {

    public static ArrayList<Livro> livros = new ArrayList<>();
    public static ArrayList<Usr>   usrs   = new ArrayList<>();

    /**
     * Processa operacoes da biblioteca.
     * tp: 1 = emprestar livro
     *     2 = devolver livro
     *     3 = cadastrar livro
     *     4 = cadastrar usuario
     *
     * p1: ISBN (ops 1,2,3) ou nome do usuario (op 4)
     * p2: matricula do usuario (ops 1,4) ou autor (op 3)
     * p3: ISBN na op 3, nao usado nas demais
     * fl: op 2 => true indica que ha multa; op 4 => true = professor
     *
     * Retorna "ok" em sucesso, null em falha.
     */
    public static String proc(int tp, String p1, String p2, String p3, boolean fl) {

        if (tp == 1) {
            // --- EMPRESTAR ---
            Usr u = null;
            for (int i = 0; i < usrs.size(); i++) {
                if (usrs.get(i).id.equals(p2)) {
                    u = usrs.get(i);
                }
            }
            if (u == null) return null;
            if (!u.ok)     return null;
            if (u.tp == 1 && u.qtd >= 3) return null;  // aluno: max 3 livros
            if (u.tp == 2 && u.qtd >= 5) return null;  // professor: max 5 livros

            Livro l = null;
            for (int i = 0; i < livros.size(); i++) {
                if (livros.get(i).isbn.equals(p1)) {
                    l = livros.get(i);
                }
            }
            if (l == null)  return null;
            if (!l.disp)    return null;

            l.disp = false;
            l.uid  = p2;
            l.dt   = new java.util.Date().toString();
            u.qtd++;
            return "ok";

        } else if (tp == 2) {
            // --- DEVOLVER ---
            Livro l = null;
            for (int i = 0; i < livros.size(); i++) {
                if (livros.get(i).isbn.equals(p1)) {
                    l = livros.get(i);
                }
            }
            if (l == null) return null;
            if (l.disp)    return null;  // livro ja estava disponivel

            Usr u = null;
            for (int i = 0; i < usrs.size(); i++) {
                if (usrs.get(i).id.equals(l.uid)) {
                    u = usrs.get(i);
                }
            }
            if (u != null) u.qtd--;

            l.disp = true;
            l.uid  = null;
            l.dt   = null;

            if (fl) {
                double multa = 2.5 * 3;  // 3 dias fixo, deveria calcular dias reais
                return "ok:multa:" + multa;
            }
            return "ok";

        } else if (tp == 3) {
            // --- CADASTRAR LIVRO ---
            for (int i = 0; i < livros.size(); i++) {
                if (livros.get(i).isbn.equals(p3)) return null;  // isbn duplicado
            }
            Livro l = new Livro(p1, p2, p3);
            livros.add(l);
            return "ok";

        } else if (tp == 4) {
            // --- CADASTRAR USUARIO ---
            for (int i = 0; i < usrs.size(); i++) {
                if (usrs.get(i).id.equals(p2)) return null;  // matricula duplicada
            }
            // fl = true: professor; fl = false: aluno
            Usr u = new Usr(p1, p2, fl ? 2 : 1);
            usrs.add(u);
            return "ok";
        }

        return null;
    }

    // lista livros (tp=true) ou usuarios (tp=false) no console
    public static void lst(boolean tp) {
        if (tp) {
            System.out.println("=== LIVROS ===");
            for (int i = 0; i < livros.size(); i++) {
                Livro l = livros.get(i);
                System.out.println(l.t + " - " + l.a
                        + " [" + l.isbn + "] "
                        + (l.disp ? "DISPONIVEL" : "EMPRESTADO"));
            }
        } else {
            System.out.println("=== USUARIOS ===");
            for (int i = 0; i < usrs.size(); i++) {
                Usr u = usrs.get(i);
                System.out.println(u.nm
                        + " (" + (u.tp == 1 ? "Aluno" : "Professor") + ")"
                        + " - Emprestimos ativos: " + u.qtd);
            }
        }
    }

    // busca livro por titulo ou autor (retorna o primeiro resultado)
    public static Livro bsc(String q) {
        for (Livro l : livros) {
            if (l.t.contains(q) || l.a.contains(q)) return l;
        }
        return null;
    }
}
