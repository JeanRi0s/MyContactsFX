package mycontacts.utils;

public class ValidadorEmail {

    private ValidadorEmail() {}

    public static boolean validar(String email) {
        if (email == null || email.isBlank()) return false;
        return email.matches("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");
    }
}
