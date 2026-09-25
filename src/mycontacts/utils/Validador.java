package mycontacts.utils;

public class Validador {

    private Validador() {}

    public static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) return "O nome não pode ser vazio.";
        if (nome.trim().length() < 2) return "O nome deve ter pelo menos 2 caracteres.";
        if (!nome.trim().matches("[\\p{L} .'-]+")) return "O nome contém caracteres inválidos.";
        return null; // válido
    }

    public static String validarTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) return "O telefone não pode ser vazio.";
        String digitos = telefone.replaceAll("[()\\- ]", "");
        if (!digitos.matches("\\d{8,11}")) return "Telefone inválido. Use entre 8 e 11 dígitos.";
        return null; // válido
    }

    public static String validarEmail(String email) {
        if (email == null || email.isBlank()) return null; // email é opcional
        if (!ValidadorEmail.validar(email)) return "E-mail inválido.";
        return null; // válido
    }

    public static String validarEmpresa(String empresa) {
        if (empresa == null || empresa.isBlank()) return "O nome da empresa não pode ser vazio.";
        return null; // válido
    }
}
