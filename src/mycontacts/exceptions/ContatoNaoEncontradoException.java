package mycontacts.exceptions;

public class ContatoNaoEncontradoException extends Exception {
    public ContatoNaoEncontradoException(String nome) {
        super("Contato não encontrado: \"" + nome + "\"");
    }
}
