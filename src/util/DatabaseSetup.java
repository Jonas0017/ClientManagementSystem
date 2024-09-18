package util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSetup {

    public static void initializeDatabase() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS clientes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "telefone TEXT," +
                "endereco TEXT," +
                "email TEXT)";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            // Executa o comando SQL para criar a tabela se não existir
            stmt.execute(sqlCreate);
            System.out.println("Banco de dados inicializado com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao criar o banco de dados: " + e.getMessage());
        }
    }
}
