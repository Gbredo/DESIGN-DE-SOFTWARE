package br.edu.puc.biblioteca.servico;

import br.edu.puc.biblioteca.dominio.Livro;
import br.edu.puc.biblioteca.dominio.Usuario;
import br.edu.puc.biblioteca.excecao.LimiteEmprestimosException;
import br.edu.puc.biblioteca.excecao.LivroIndisponivelException;

import java.math.BigDecimal;

/**
 * Gerencia as operacoes de emprestimo e devolucao de livros.
 *
 * Responsabilidade unica: orquestrar o fluxo de emprestimo entre Livro e Usuario.
 *
 * Diferenças em relacao ao codigo original (parte do Bib.proc()):
 *   - Dois metodos focados (emprestarLivro, devolverLivro) em vez de um metodo
 *     unico controlado por parametro inteiro.
 *   - Sem parametros booleanos: devolverLivro calcula a multa internamente.
 *   - Constantes nomeadas substituem numeros magicos (2.5, 3, 5, 14).
 *   - Calculo de multa correto usando LocalDate (via Emprestimo.diasEmAtraso).
 *   - Excecoes especificas substituem retorno null.
 *   - Validacao extraida em metodo privado (responsabilidade unica por metodo).
 *   - Estado de instancia recebido por injecao de dependencia (testavel).
 */
public class EmprestimoService {

    private static final BigDecimal MULTA_POR_DIA_DE_ATRASO = new BigDecimal("2.50");
    private static final int        PRAZO_EMPRESTIMO_EM_DIAS = 14;

    private final CatalogoService catalogo;

    public EmprestimoService(CatalogoService catalogo) {
        this.catalogo = catalogo;
    }

    /**
     * Registra o emprestimo de um livro para um usuario.
     *
     * @throws LivroIndisponivelException  se o livro nao estiver disponivel.
     * @throws LimiteEmprestimosException  se o usuario atingiu seu limite.
     */
    public void emprestarLivro(String isbn, String matriculaUsuario) {
        Livro   livro   = catalogo.buscarLivroOuLancarExcecao(isbn);
        Usuario usuario = catalogo.buscarUsuarioOuLancarExcecao(matriculaUsuario);

        validarEmprestimo(livro, usuario);

        livro.emprestar(matriculaUsuario);
        usuario.registrarEmprestimo();
    }

    /**
     * Registra a devolucao de um livro e retorna o valor da multa por atraso.
     * Retorna BigDecimal.ZERO quando nao ha atraso.
     */
    public BigDecimal devolverLivro(String isbn, String matriculaUsuario) {
        Livro   livro   = catalogo.buscarLivroOuLancarExcecao(isbn);
        Usuario usuario = catalogo.buscarUsuarioOuLancarExcecao(matriculaUsuario);

        BigDecimal multa = calcularMulta(livro);

        livro.devolver();
        usuario.registrarDevolucao();

        return multa;
    }

    private void validarEmprestimo(Livro livro, Usuario usuario) {
        if (!livro.isDisponivel()) {
            throw new LivroIndisponivelException(livro.getIsbn());
        }
        if (!usuario.podeEmprestar()) {
            throw new LimiteEmprestimosException(usuario.getMatricula());
        }
    }

    private BigDecimal calcularMulta(Livro livro) {
        return livro.getEmprestimoAtivo()
                .map(emprestimo -> {
                    long dias = emprestimo.diasEmAtraso(PRAZO_EMPRESTIMO_EM_DIAS);
                    return MULTA_POR_DIA_DE_ATRASO.multiply(BigDecimal.valueOf(dias));
                })
                .orElse(BigDecimal.ZERO);
    }
}
