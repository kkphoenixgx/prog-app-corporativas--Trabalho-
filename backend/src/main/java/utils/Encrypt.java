package utils;

import org.mindrot.jbcrypt.BCrypt;

public class Encrypt {
    // Gera o hash da senha
    public static String encrypt(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Compara senha pura com hash
    public static boolean compare(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) return false;
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
