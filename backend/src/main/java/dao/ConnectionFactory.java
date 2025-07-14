package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // private static final String URL = "jdbc:mysql://db:3306/meu_banco?useSSL=false&serverTimezone=UTC";
    private static final String URL = "jdbc:mysql://db:3306/meu_banco?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC não encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados.");
            e.printStackTrace();
        }

        return null;
    }

    public static Connection getTestConnection() {
        String testUrl = "jdbc:mysql://localhost:3306/meu_banco?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String testUser = "root";
        String testPassword = "root";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(testUrl, testUser, testPassword);
        } catch (ClassNotFoundException e) {
            System.err.println("[TEST] Driver JDBC não encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[TEST] Erro ao conectar ao banco de dados.");
            e.printStackTrace();
        }
        return null;
    }

}
