package br.edu.puc.biblioteca.servico;

import br.edu.puc.biblioteca.dominio.Livro;
import br.edu.puc.biblioteca.dominio.TipoUsuario;
import br.edu.puc.biblioteca.dominio.Usuario;
import br.edu.puc.biblioteca.excecao.LimiteEmprestimosException;
import br.edu.puc.biblioteca.excecao.LivroIndisponivelException;
import br.edu.puc.biblioteca.excecao.LivroNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoServiceTest {

    private CatalogoService  catalogo;
    private EmprestimoService emprestimos;

    @BeforeEach
    void setUp() {
        catalogo    = new CatalogoService();
        emprestimos = new EmprestimoService(catalogo);

        catalogo.cadastrarLivro(new Livro("ISBN-LIMPO", "Codigo Limpo", "Robert C. Martin"));
        catalogo.cadastrarUsuario(new Usuario("ANA-001", "Ana Silva", "62999990001", TipoUsuario.ALUNO));
        catalogo.cadastrarUsuario(new Usuario("PROF-001", "Carlos Ramos", "62999990002", TipoUsuario.PROFESSOR));
    }

    @Test
    void deveMarcarLivroComoIndisponivelAposEmprestimo() {
        emprestimos.emprestarLivro("ISBN-LIMPO", "ANA-001");

        Livro livro = catalogo.buscarLivroOuLancarExcecao("ISBN-LIMPO");
        assertFalse(livro.isDisponivel());
    }

    @Test
    void deveIncrementarContadorDeEmprestimosDoUsuario() {
        emprestimos.emprestarLivro("ISBN-LIMPO", "ANA-001");

        Usuario usuario = catalogo.buscarUsuarioOuLancarExcecao("ANA-001");
        assertEquals(1, usuario.getQuantidadeEmprestimos());
    }

    @Test
    void deveLancarExcecaoQuandoLivroIndisponivel() {
        emprestimos.emprestarLivro("ISBN-LIMPO", "ANA-001");

        assertThrows(LivroIndisponivelException.class,
                () -> emprestimos.emprestarLivro("ISBN-LIMPO", "PROF-001"));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoAtingeLimiteDeTresEmprestimos() {
        catalogo.cadastrarLivro(new Livro("ISBN-A", "Livro A", "Autor A"));
        catalogo.cadastrarLivro(new Livro("ISBN-B", "Livro B", "Autor B"));
        catalogo.cadastrarLivro(new Livro("ISBN-C", "Livro C", "Autor C"));

        emprestimos.emprestarLivro("ISBN-LIMPO", "ANA-001");
        emprestimos.emprestarLivro("ISBN-A", "ANA-001");
        emprestimos.emprestarLivro("ISBN-B", "ANA-001");

        assertThrows(LimiteEmprestimosException.class,
                () -> emprestimos.emprestarLivro("ISBN-C", "ANA-001"));
    }

    @Test
    void deveProfessorEmprestarAteCincoLivros() {
        for (int i = 1; i <= 4; i++) {
            catalogo.cadastrarLivro(new Livro("ISBN-" + i, "Livro " + i, "Autor"));
        }

        emprestimos.emprestarLivro("ISBN-LIMPO", "PROF-001");
        emprestimos.emprestarLivro("ISBN-1", "PROF-001");
        emprestimos.emprestarLivro("ISBN-2", "PROF-001");
        emprestimos.emprestarLivro("ISBN-3", "PROF-001");
        emprestimos.emprestarLivro("ISBN-4", "PROF-001");

        Usuario professor = catalogo.buscarUsuarioOuLancarExcecao("PROF-001");
        assertEquals(5, professor.getQuantidadeEmprestimos());
    }

    @Test
    void deveDevolverLivroETornarDisponivel() {
        emprestimos.emprestarLivro("ISBN-LIMPO", "ANA-001");
        emprestimos.devolverLivro("ISBN-LIMPO", "ANA-001");

        Livro livro = catalogo.buscarLivroOuLancarExcecao("ISBN-LIMPO");
        assertTrue(livro.isDisponivel());
    }

    @Test
    void deveDevolverLivroEDecrementarContadorDoUsuario() {
        emprestimos.emprestarLivro("ISBN-LIMPO", "ANA-001");
        emprestimos.devolverLivro("ISBN-LIMPO", "ANA-001");

        Usuario usuario = catalogo.buscarUsuarioOuLancarExcecao("ANA-001");
        assertEquals(0, usuario.getQuantidadeEmprestimos());
    }

    @Test
    void deveDevolverSemMultaQuandoDentroDoPrazo() {
        emprestimos.emprestarLivro("ISBN-LIMPO", "ANA-001");

        BigDecimal multa = emprestimos.devolverLivro("ISBN-LIMPO", "ANA-001");

        assertEquals(BigDecimal.ZERO, multa);
    }

    @Test
    void deveLancarExcecaoParaIsbnInexistente() {
        assertThrows(LivroNaoEncontradoException.class,
                () -> emprestimos.emprestarLivro("ISBN-INEXISTENTE", "ANA-001"));
    }
}
