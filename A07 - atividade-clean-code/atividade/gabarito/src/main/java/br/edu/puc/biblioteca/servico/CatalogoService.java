package br.edu.puc.biblioteca.servico;

import br.edu.puc.biblioteca.dominio.Livro;
import br.edu.puc.biblioteca.dominio.Usuario;
import br.edu.puc.biblioteca.excecao.LivroJaCadastradoException;
import br.edu.puc.biblioteca.excecao.LivroNaoEncontradoException;
import br.edu.puc.biblioteca.excecao.UsuarioJaCadastradoException;
import br.edu.puc.biblioteca.excecao.UsuarioNaoEncontradoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Gerencia o catalogo de livros e o cadastro de usuarios.
 *
 * Responsabilidade unica: manter e consultar as colecoes de livros e usuarios.
 * A logica de emprestimo foi extraida para EmprestimoService.
 *
 * Diferenças em relacao ao codigo original:
 *   - Estado de instancia (nao estatico), o que permite teste isolado.
 *   - Metodos com nomes expressivos em vez de parametros booleanos.
 *   - Optional no lugar de null.
 *   - Excecoes especificas no lugar de retorno null.
 *   - Loop de busca unificado em metodo privado (elimina DRY violation).
 */
public class CatalogoService {

    private final List<Livro>   livros   = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();

    public void cadastrarLivro(Livro livro) {
        if (buscarLivroPorIsbn(livro.getIsbn()).isPresent()) {
            throw new LivroJaCadastradoException(livro.getIsbn());
        }
        livros.add(livro);
    }

    public void cadastrarUsuario(Usuario usuario) {
        if (buscarUsuarioPorMatricula(usuario.getMatricula()).isPresent()) {
            throw new UsuarioJaCadastradoException(usuario.getMatricula());
        }
        usuarios.add(usuario);
    }

    /** Busca ou lanca LivroNaoEncontradoException. Elimina checagem de null no chamador. */
    public Livro buscarLivroOuLancarExcecao(String isbn) {
        return buscarLivroPorIsbn(isbn)
                .orElseThrow(() -> new LivroNaoEncontradoException(isbn));
    }

    /** Busca ou lanca UsuarioNaoEncontradoException. Elimina checagem de null no chamador. */
    public Usuario buscarUsuarioOuLancarExcecao(String matricula) {
        return buscarUsuarioPorMatricula(matricula)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(matricula));
    }

    public Optional<Livro> buscarLivroPorIsbn(String isbn) {
        return livros.stream()
                .filter(livro -> livro.getIsbn().equals(isbn))
                .findFirst();
    }

    public Optional<Usuario> buscarUsuarioPorMatricula(String matricula) {
        return usuarios.stream()
                .filter(usuario -> usuario.getMatricula().equals(matricula))
                .findFirst();
    }

    public List<Livro> buscarLivrosPorTituloOuAutor(String termo) {
        return livros.stream()
                .filter(livro -> livro.getTitulo().contains(termo)
                              || livro.getAutor().contains(termo))
                .toList();
    }

    public List<Livro>   getLivros()   { return List.copyOf(livros); }
    public List<Usuario> getUsuarios() { return List.copyOf(usuarios); }
}
