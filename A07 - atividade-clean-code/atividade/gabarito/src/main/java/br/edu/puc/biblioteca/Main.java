package br.edu.puc.biblioteca;

import br.edu.puc.biblioteca.dominio.Livro;
import br.edu.puc.biblioteca.dominio.TipoUsuario;
import br.edu.puc.biblioteca.dominio.Usuario;
import br.edu.puc.biblioteca.excecao.BibliotecaException;
import br.edu.puc.biblioteca.servico.CatalogoService;
import br.edu.puc.biblioteca.servico.EmprestimoService;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        CatalogoService  catalogo   = new CatalogoService();
        EmprestimoService emprestimos = new EmprestimoService(catalogo);

        // cadastro de usuarios
        catalogo.cadastrarUsuario(new Usuario("20230001", "Ana Silva",    "62999990001", TipoUsuario.ALUNO));
        catalogo.cadastrarUsuario(new Usuario("20230002", "Carlos Ramos", "62999990002", TipoUsuario.PROFESSOR));

        // cadastro de livros
        catalogo.cadastrarLivro(new Livro("978-0132350884", "Codigo Limpo",            "Robert C. Martin"));
        catalogo.cadastrarLivro(new Livro("978-0201633610", "Design Patterns",         "Gang of Four"));
        catalogo.cadastrarLivro(new Livro("978-0135957059", "The Pragmatic Programmer","David Thomas"));

        // emprestimo bem-sucedido
        emprestimos.emprestarLivro("978-0132350884", "20230001");
        System.out.println("Emprestimo registrado com sucesso.");

        // tentativa de emprestimo de livro ja emprestado
        try {
            emprestimos.emprestarLivro("978-0132350884", "20230002");
        } catch (BibliotecaException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        // listagem
        System.out.println("\n--- Livros ---");
        catalogo.getLivros().forEach(l ->
            System.out.printf("%s - %s [%s] %s%n",
                l.getTitulo(), l.getAutor(), l.getIsbn(),
                l.isDisponivel() ? "DISPONIVEL" : "EMPRESTADO")
        );

        System.out.println("\n--- Usuarios ---");
        catalogo.getUsuarios().forEach(u ->
            System.out.printf("%s (%s) - emprestimos ativos: %d%n",
                u.getNome(), u.getTipo(), u.getQuantidadeEmprestimos())
        );

        // devolucao
        BigDecimal multa = emprestimos.devolverLivro("978-0132350884", "20230001");
        System.out.printf("%nDevolucao concluida. Multa: R$ %.2f%n", multa);

        // busca
        var resultados = catalogo.buscarLivrosPorTituloOuAutor("Pragmatic");
        System.out.println("Busca 'Pragmatic': "
            + resultados.stream().map(Livro::getTitulo).toList());
    }
}
