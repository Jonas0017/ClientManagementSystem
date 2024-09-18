package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Cliente;
import util.DatabaseUtil;

public class ClienteDAO {
    private Connection connection;

    // Construtor da classe DAO que estabelece conexão com o banco de dados.
    public ClienteDAO() throws SQLException {
        this.connection = DatabaseUtil.getConnection();
    }

    /**
     * Adiciona um novo cliente ao banco de dados.
     * 
     * @param cliente O objeto Cliente contendo as informações do cliente a ser adicionado.
     * @return true se o cliente foi adicionado com sucesso, false se o cliente já existe ou ocorreu um erro.
     */
    public boolean adicionarCliente(Cliente cliente) {
        // Verifica se já existe um cliente com o mesmo email no banco de dados.
        if (clienteExiste(cliente.getEmail())) {
            // Se o cliente já existe, retorna false para indicar falha na adição.
            return false;
        }

        // Prepara a string SQL para inserir um novo cliente no banco de dados.
        String sql = "INSERT INTO clientes (nome, telefone, endereco, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Configura os valores dos parâmetros com base nos atributos do objeto cliente.
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEndereco());
            stmt.setString(4, cliente.getEmail());

            // Executa a instrução SQL e verifica se foi bem-sucedida.
            stmt.executeUpdate();
            return true;  // Retorna true indicando sucesso na adição do cliente.
        } catch (SQLException e) {
            // Imprime o stack trace do erro se ocorrer uma exceção SQL.
            e.printStackTrace();
            return false;  // Retorna false indicando falha na adição do cliente.
        }
    }

    
    /**
     * Verifica se um cliente com o email especificado já existe no banco de dados.
     * 
     * @param email O email do cliente a ser verificado.
     * @return true se um cliente com o mesmo email já existe, false caso contrário.
     */
    private boolean clienteExiste(String email) {
        // Prepara a string SQL para consultar a existência de um cliente com o email especificado.
        String sql = "SELECT COUNT(*) FROM clientes WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Configura o valor do parâmetro email na instrução SQL.
            stmt.setString(1, email);

            // Executa a consulta e processa o resultado.
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Retorna true se a contagem for maior que 0, indicando que o cliente existe.
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            // Imprime o stack trace do erro se ocorrer uma exceção SQL.
            e.printStackTrace();
        }
        // Retorna false indicando que o cliente não existe se a consulta falhar ou a contagem for 0.
        return false;
    }


    // Método para buscar um cliente pelo nome. Retorna um objeto Cliente se encontrado.
    public Cliente buscarClientePorNome(String nome) {
        String sql = "SELECT * FROM clientes WHERE nome = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cliente(
                    rs.getString("nome"),
                    rs.getString("telefone"),
                    rs.getString("endereco"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para atualizar os dados de um cliente existente. Retorna true se a atualização foi bem-sucedida.
    public boolean atualizarCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET nome = ?, telefone = ?, endereco = ?, email = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEndereco());
            stmt.setString(4, cliente.getEmail());
            stmt.setInt(5, cliente.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Método para deletar um cliente pelo ID. Retorna true se a deleção foi bem-sucedida.
    public boolean deletarCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
