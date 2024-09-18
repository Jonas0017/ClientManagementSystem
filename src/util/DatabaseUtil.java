package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static String url = "jdbc:sqlite:JDBC/sqlite-jdbc-3.42.0.0.db"; // Atualize com o caminho correto

    // Carrega o driver JDBC e estabelece uma conexão
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}

