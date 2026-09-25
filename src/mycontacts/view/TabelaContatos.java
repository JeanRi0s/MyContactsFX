package mycontacts.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import mycontacts.model.Contato;
import mycontacts.model.ContatoComercial;

import java.util.function.Consumer;

public class TabelaContatos {

    private TabelaContatos() {}

    public static TableView<Contato> criar(
            Consumer<Contato> aoEditar,
            Consumer<Contato> aoRemover) {

        TableView<Contato> tabela = new TableView<>();
        tabela.setPlaceholder(new Label("Nenhum contato encontrado."));
        tabela.setStyle("-fx-background-radius: 8;");

        // Colunas
        tabela.getColumns().addAll(
            coluna("ID",       "id",       60),
            coluna("Nome",     "nome",     180),
            coluna("Telefone", "telefone", 130),
            coluna("E-mail",   "email",    200),
            colunaEmpresa(),
            colunaTipo(),
            colunaAcoes(aoEditar, aoRemover)
        );

        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tabela;
    }

    private static <T> TableColumn<Contato, T> coluna(String titulo, String propriedade, double largura) {
        TableColumn<Contato, T> col = new TableColumn<>(titulo);
        col.setCellValueFactory(new PropertyValueFactory<>(propriedade));
        col.setPrefWidth(largura);
        return col;
    }

    private static TableColumn<Contato, String> colunaEmpresa() {
        TableColumn<Contato, String> col = new TableColumn<>("Empresa");
        col.setCellValueFactory(data -> {
            Contato c = data.getValue();
            String empresa = (c instanceof ContatoComercial cc) ? cc.getEmpresa() : "—";
            return new javafx.beans.property.SimpleStringProperty(empresa);
        });
        col.setPrefWidth(150);
        return col;
    }

    private static TableColumn<Contato, String> colunaTipo() {
        TableColumn<Contato, String> col = new TableColumn<>("Tipo");
        col.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
        col.setPrefWidth(90);
        col.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String tipo, boolean vazio) {
                super.updateItem(tipo, vazio);
                if (vazio || tipo == null) { setText(null); setStyle(""); return; }
                setText(tipo);
                setStyle("Comercial".equals(tipo)
                    ? "-fx-text-fill: #2980b9; -fx-font-weight: bold;"
                    : "-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            }
        });
        return col;
    }

    private static TableColumn<Contato, Void> colunaAcoes(
            Consumer<Contato> aoEditar, Consumer<Contato> aoRemover) {

        TableColumn<Contato, Void> col = new TableColumn<>("Ações");
        col.setPrefWidth(130);
        col.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar  = new Button("✏️ Editar");
            private final Button btnRemover = new Button("🗑️ Remover");
            private final HBox box = new HBox(6, btnEditar, btnRemover);

            {
                box.setAlignment(Pos.CENTER);
                btnEditar.setStyle(
                    "-fx-background-color: #3498db; -fx-text-fill: white;" +
                    "-fx-background-radius: 4; -fx-font-size: 11px; -fx-padding: 3 8;");
                btnRemover.setStyle(
                    "-fx-background-color: #e74c3c; -fx-text-fill: white;" +
                    "-fx-background-radius: 4; -fx-font-size: 11px; -fx-padding: 3 8;");
                btnEditar.setOnAction(e -> {
                    Contato c = getTableView().getItems().get(getIndex());
                    aoEditar.accept(c);
                });
                btnRemover.setOnAction(e -> {
                    Contato c = getTableView().getItems().get(getIndex());
                    aoRemover.accept(c);
                });
            }

            @Override
            protected void updateItem(Void item, boolean vazio) {
                super.updateItem(item, vazio);
                setGraphic(vazio ? null : box);
            }
        });
        return col;
    }
}
