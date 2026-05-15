package br.edu.puc.biblioteca.excecao;

public class UsuarioJaCadastradoException extends BibliotecaException {
    public UsuarioJaCadastradoException(String matricula) {
        super("Usuario ja cadastrado: matricula " + matricula);
    }
}
