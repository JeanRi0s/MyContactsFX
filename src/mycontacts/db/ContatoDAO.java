package mycontacts.db;

import mycontacts.model.Contato;
import mycontacts.model.ContatoComercial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContatoDAO {

    // ── Inserir ─────────────────────────────────────────────────────────────
    public void inserir(Contato contato) throws SQLException {
        String sql = "INSERT INTO contatos (nome, telefone, email, tipo, empresa) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contato.getNome());
            ps.setString(2, contato.getTelefone());
            ps.setString(3, contato.getEmail());
            ps.setString(4, contato.getTipo());
            if (contato instanceof ContatoComercial cc) {
                ps.setString(5, cc.getEmpresa());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }
            ps.executeUpdate();
        }
    }

    // ── Listar todos ────────────────────────────────────────────────────────
    public List<Contato> listarTodos() throws SQLException {
        String sql = "SELECT * FROM contatos ORDER BY nome";
        List<Contato> lista = new ArrayList<>();
        try (Connection conn = ConexaoDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── Buscar por nome (parcial, case-insensitive) ──────────────────────────
    public List<Contato> buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM contatos WHERE nome LIKE ? ORDER BY nome";
        List<Contato> lista = new ArrayList<>();
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── Atualizar ────────────────────────────────────────────────────────────
    public void atualizar(Contato contato) throws SQLException {
        String sql = "UPDATE contatos SET nome=?, telefone=?, email=?, tipo=?, empresa=? WHERE id=?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contato.getNome());
            ps.setString(2, contato.getTelefone());
            ps.setString(3, contato.getEmail());
            ps.setString(4, contato.getTipo());
            if (contato instanceof ContatoComercial cc) {
                ps.setString(5, cc.getEmpresa());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }
            ps.setInt(6, contato.getId());
            ps.executeUpdate();
        }
    }

    // ── Remover ──────────────────────────────────────────────────────────────
    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM contatos WHERE id=?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ── Mapeamento ResultSet → Contato ───────────────────────────────────────
    private Contato mapear(ResultSet rs) throws SQLException {
        int id         = rs.getInt("id");
        String nome    = rs.getString("nome");
        String tel     = rs.getString("telefone");
        String email   = rs.getString("email");
        String tipo    = rs.getString("tipo");
        String empresa = rs.getString("empresa");

        if ("Comercial".equals(tipo)) {
            return new ContatoComercial(id, nome, tel, email, empresa);
        }
        return new Contato(id, nome, tel, email);
    }
}
