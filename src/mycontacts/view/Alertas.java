package mycontacts.view;

import javafx.scene.control.Alert;

public class Alertas {

    private Alertas() {}

    public static void info(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("MyContacts");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public static void erro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro — MyContacts");
        alerta.setHeaderText("Ocorreu um erro");
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
