package mycontacts.controller;

import mycontacts.db.ContatoDAO;
import mycontacts.exceptions.ContatoNaoEncontradoException;
import mycontacts.model.Contato;

import java.sql.SQLException;
import java.util.List;

public class Agenda implements Buscavel {

    private final ContatoDAO dao = new ContatoDAO();

    public void adicionarContato(Contato contato) throws SQLException {
        dao.inserir(contato);
    }

    public List<Contato> listarContatos() throws SQLException {
        return dao.listarTodos();
    }

    public List<Contato> buscarPorNome(String nome) throws SQLException {
        return dao.buscarPorNome(nome);
    }

    public Contato buscarPorNomeExato(String nome) throws ContatoNaoEncontradoException {
        try {
            List<Contato> resultado = dao.buscarPorNome(nome);
            return resultado.stream()
                    .filter(c -> c.getNome().equalsIgnoreCase(nome))
                    .findFirst()
                    .orElseThrow(() -> new ContatoNaoEncontradoException(nome));
        } catch (java.sql.SQLException e) {
            throw new ContatoNaoEncontradoException(nome);
        }
    }

    public void atualizarContato(Contato contato) throws SQLException {
        dao.atualizar(contato);
    }

    public void removerContato(int id) throws SQLException {
        dao.remover(id);
    }
}