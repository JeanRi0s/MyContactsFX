package mycontacts.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoDB {

    // O banco será criado automaticamente na pasta do projeto
    private static final String URL = "jdbc:sqlite:mycontacts.db";

    private ConexaoDB() {}

    public static Connection conectar() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        conn.createStatement().execute("PRAGMA foreign_keys = ON");
        return conn;
    }

    public static void inicializarBanco() {
        String sql = """
                CREATE TABLE IF NOT EXISTS contatos (
                    id       INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome     TEXT    NOT NULL,
                    telefone TEXT    NOT NULL,
                    email    TEXT,
                    tipo     TEXT    NOT NULL DEFAULT 'Pessoal',
                    empresa  TEXT
                );
                """;
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("[DB] Banco inicializado com sucesso.");
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao inicializar banco: " + e.getMessage());
        }
    }
}
