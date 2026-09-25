package mycontacts.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import mycontacts.controller.Agenda;
import mycontacts.db.ConexaoDB;
import mycontacts.model.Contato;

import java.sql.SQLException;
import java.util.List;

public class MainView extends Application {

    private final Agenda agenda = new Agenda();
    private TableView<Contato> tabela;
    private TextField campoBusca;

    @Override
    public void start(Stage stage) {
        ConexaoDB.inicializarBanco();

        stage.setTitle("MyContacts — Agenda de Contatos");
        stage.setMinWidth(750);
        stage.setMinHeight(500);

        // ── Layout raiz ────────────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f6f9;");

        // ── Cabeçalho ──────────────────────────────────────────────────
        Label titulo = new Label("📒 MyContacts");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        HBox header = new HBox(titulo);
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dce1e7; -fx-border-width: 0 0 1 0;");
        root.setTop(header);

        // ── Barra de busca ─────────────────────────────────────────────
        campoBusca = new TextField();
        campoBusca.setPromptText("🔍 Buscar por nome...");
        campoBusca.setPrefWidth(280);
        campoBusca.setStyle("-fx-background-radius: 6; -fx-border-radius: 6; -fx-padding: 6 10;");
        campoBusca.textProperty().addListener((obs, antigo, novo) -> filtrarTabela(novo));

        Button btnNovo = criarBotao("➕ Novo Contato", "#27ae60");
        btnNovo.setOnAction(e -> abrirFormulario(null));

        HBox barraSuperior = new HBox(10, campoBusca, btnNovo);
        barraSuperior.setPadding(new Insets(14, 20, 10, 20));
        barraSuperior.setAlignment(Pos.CENTER_LEFT);

        // ── Tabela ─────────────────────────────────────────────────────
        tabela = TabelaContatos.criar(
            this::editarContato,
            this::confirmarRemocao
        );

        VBox centro = new VBox(barraSuperior, tabela);
        VBox.setVgrow(tabela, Priority.ALWAYS);
        centro.setPadding(new Insets(0, 20, 20, 20));
        root.setCenter(centro);

        carregarTabela();

        Scene scene = new Scene(root, 820, 540);
        stage.setScene(scene);
        stage.show();
    }

    // ── Operações ──────────────────────────────────────────────────────────

    void carregarTabela() {
        try {
            List<Contato> lista = agenda.listarContatos();
            tabela.getItems().setAll(lista);
        } catch (SQLException e) {
            Alertas.erro("Erro ao carregar contatos: " + e.getMessage());
        }
    }

    private void filtrarTabela(String termo) {
        try {
            if (termo == null || termo.isBlank()) {
                carregarTabela();
            } else {
                List<Contato> resultado = agenda.buscarPorNome(termo);
                tabela.getItems().setAll(resultado);
            }
        } catch (SQLException e) {
            Alertas.erro("Erro na busca: " + e.getMessage());
        }
    }

    private void abrirFormulario(Contato contatoExistente) {
        FormularioView form = new FormularioView(agenda, contatoExistente, this::carregarTabela);
        form.show();
    }

    private void editarContato(Contato contato) {
        abrirFormulario(contato);
    }

    private void confirmarRemocao(Contato contato) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remover Contato");
        confirm.setHeaderText("Deseja remover \"" + contato.getNome() + "\"?");
        confirm.setContentText("Esta ação não pode ser desfeita.");
        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try {
                    agenda.removerContato(contato.getId());
                    carregarTabela();
                    Alertas.info("Contato removido com sucesso!");
                } catch (SQLException e) {
                    Alertas.erro("Erro ao remover: " + e.getMessage());
                }
            }
        });
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private Button criarBotao(String texto, String cor) {
        Button btn = new Button(texto);
        btn.setStyle(
            "-fx-background-color: " + cor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 7 14;"
        );
        return btn;
    }
}
