package mycontacts.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mycontacts.controller.Agenda;
import mycontacts.model.Contato;
import mycontacts.model.ContatoComercial;
import mycontacts.utils.Validador;

import java.sql.SQLException;

public class FormularioView {

    private final Agenda agenda;
    private final Contato contatoEdicao;   // null = novo contato
    private final Runnable aoSalvar;

    // Campos do formulário
    private TextField campoNome;
    private TextField campoTelefone;
    private TextField campoEmail;
    private TextField campoEmpresa;
    private ToggleGroup grupoTipo;
    private Label erroNome, erroTelefone, erroEmail, erroEmpresa;
    private VBox boxEmpresa;

    private Stage stage;

    public FormularioView(Agenda agenda, Contato contatoEdicao, Runnable aoSalvar) {
        this.agenda = agenda;
        this.contatoEdicao = contatoEdicao;
        this.aoSalvar = aoSalvar;
    }

    public void show() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(contatoEdicao == null ? "Novo Contato" : "Editar Contato");
        stage.setResizable(false);

        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: #ffffff;");
        root.setPrefWidth(400);

        // Título
        Label titulo = new Label(contatoEdicao == null ? "➕ Novo Contato" : "✏️ Editar Contato");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Tipo de contato (RadioButton)
        RadioButton rbPessoal   = new RadioButton("Pessoal");
        RadioButton rbComercial = new RadioButton("Comercial");
        grupoTipo = new ToggleGroup();
        rbPessoal.setToggleGroup(grupoTipo);
        rbComercial.setToggleGroup(grupoTipo);
        rbPessoal.setSelected(true);

        HBox boxTipo = new HBox(16, new Label("Tipo:"), rbPessoal, rbComercial);
        boxTipo.setAlignment(Pos.CENTER_LEFT);

        // Campos
        campoNome      = campo("Nome *");
        campoTelefone  = campo("Telefone *");
        campoEmail     = campo("E-mail (opcional)");
        campoEmpresa   = campo("Empresa *");

        erroNome      = labelErro();
        erroTelefone  = labelErro();
        erroEmail     = labelErro();
        erroEmpresa   = labelErro();

        boxEmpresa = new VBox(4, new Label("Empresa *"), campoEmpresa, erroEmpresa);
        boxEmpresa.setVisible(false);
        boxEmpresa.setManaged(false);

        rbComercial.selectedProperty().addListener((obs, ant, comercial) -> {
            boxEmpresa.setVisible(comercial);
            boxEmpresa.setManaged(comercial);
        });

        // Pré-preencher se for edição
        if (contatoEdicao != null) {
            campoNome.setText(contatoEdicao.getNome());
            campoTelefone.setText(contatoEdicao.getTelefone());
            campoEmail.setText(contatoEdicao.getEmail());
            if (contatoEdicao instanceof ContatoComercial cc) {
                rbComercial.setSelected(true);
                campoEmpresa.setText(cc.getEmpresa());
            }
        }

        // Botões
        Button btnSalvar   = new Button("💾 Salvar");
        Button btnCancelar = new Button("Cancelar");

        btnSalvar.setStyle(
            "-fx-background-color: #27ae60; -fx-text-fill: white;" +
            "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 20;");
        btnCancelar.setStyle(
            "-fx-background-color: #95a5a6; -fx-text-fill: white;" +
            "-fx-background-radius: 6; -fx-padding: 8 14;");

        btnSalvar.setOnAction(e -> salvar(rbComercial.isSelected()));
        btnCancelar.setOnAction(e -> stage.close());

        HBox botoes = new HBox(10, btnSalvar, btnCancelar);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        botoes.setPadding(new Insets(8, 0, 0, 0));

        root.getChildren().addAll(
            titulo,
            new Separator(),
            boxTipo,
            labelCampo("Nome *"), campoNome, erroNome,
            labelCampo("Telefone *"), campoTelefone, erroTelefone,
            labelCampo("E-mail (opcional)"), campoEmail, erroEmail,
            boxEmpresa,
            new Separator(),
            botoes
        );

        stage.setScene(new Scene(root));
        stage.show();
    }

    // ── Lógica de salvar com validação ──────────────────────────────────────

    private void salvar(boolean isComercial) {
        // Limpar erros anteriores
        erroNome.setText("");
        erroTelefone.setText("");
        erroEmail.setText("");
        erroEmpresa.setText("");

        String nome     = campoNome.getText().trim();
        String telefone = campoTelefone.getText().trim();
        String email    = campoEmail.getText().trim();
        String empresa  = campoEmpresa.getText().trim();

        boolean valido = true;

        String msgNome = Validador.validarNome(nome);
        if (msgNome != null) { erroNome.setText(msgNome); valido = false; }

        String msgTel = Validador.validarTelefone(telefone);
        if (msgTel != null) { erroTelefone.setText(msgTel); valido = false; }

        String msgEmail = Validador.validarEmail(email);
        if (msgEmail != null) { erroEmail.setText(msgEmail); valido = false; }

        if (isComercial) {
            String msgEmp = Validador.validarEmpresa(empresa);
            if (msgEmp != null) { erroEmpresa.setText(msgEmp); valido = false; }
        }

        if (!valido) return;

        try {
            Contato contato;
            if (isComercial) {
                contato = new ContatoComercial(nome, telefone, email, empresa);
            } else {
                contato = new Contato(nome, telefone, email);
            }

            if (contatoEdicao == null) {
                agenda.adicionarContato(contato);
                Alertas.info("Contato adicionado com sucesso!");
            } else {
                contato.setId(contatoEdicao.getId());
                agenda.atualizarContato(contato);
                Alertas.info("Contato atualizado com sucesso!");
            }

            aoSalvar.run();
            stage.close();

        } catch (SQLException e) {
            Alertas.erro("Erro ao salvar contato: " + e.getMessage());
        }
    }

    // ── Helpers de UI ───────────────────────────────────────────────────────

    private TextField campo(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 6 10;");
        return tf;
    }

    private Label labelCampo(String texto) {
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");
        return lbl;
    }

    private Label labelErro() {
        Label lbl = new Label();
        lbl.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px;");
        return lbl;
    }
}
