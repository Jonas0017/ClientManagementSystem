package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import util.DatabaseUtil;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

@SuppressWarnings("serial")
public class VisualizarClientesGUI extends JFrame {
    private JTable table;    
	private Object originalNome;
	private Object originalTelefone;
	private Object originalEndereco;
	private Object originalEmail;

    // Construtor da classe, configura a janela principal
    public VisualizarClientesGUI() {
    	setTitle("Visualizar Clientes"); // Define o título da janela
        setSize(500, 300); // Define o tamanho da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Define a operação de fechamento

        // Define o layout do gerenciador e adiciona uma borda de 10px ao redor do conteúdo
        getContentPane().setLayout(new BorderLayout());
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10)); // Adiciona uma borda de 10px

        setLocationRelativeTo(null); // Centraliza a janela
                  
        initializeTable(); // Chama o método para inicializar a tabela e componentes relacionados
    }

    // Método para inicializar a tabela e seus componentes
    private void initializeTable() {
        // Vetor para os nomes das colunas da tabela
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Nome");
        columnNames.add("Telefone");
        columnNames.add("Endereço");
        columnNames.add("Email");

        // Cria o modelo da tabela com os dados e colunas especificados
        DefaultTableModel model = new DefaultTableModel(new Vector<Vector<Object>>(), columnNames);
        table = new JTable(model); // Cria a tabela com o modelo
        JScrollPane scrollPane = new JScrollPane(table); // Adiciona a tabela em um painel de rolagem
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Cria e configura os botões
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnDeletar = new JButton("Deletar");

        // Adiciona funcionalidade aos botões de atualizar e deletar
        btnAtualizar.addActionListener(e -> atualizarCliente());
        btnDeletar.addActionListener(e -> deletarCliente());

        // Cria um painel para os botões e adiciona-os ao painel
        JPanel panelButtons = new JPanel();
        panelButtons.add(btnVoltar);
        panelButtons.add(btnAtualizar);
        panelButtons.add(btnDeletar);

        // Adiciona o painel de botões à parte inferior do layout
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
        
        // Carrega os dados dos clientes na tabela
        loadClientsData();
    }

    // Método para carregar dados dos clientes na tabela
    private void loadClientsData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpa os dados existentes

        try (Connection conn = DatabaseUtil.getConnection(); // Abre uma conexão com o banco de dados
             Statement stmt = conn.createStatement(); // Cria um statement para executar SQL
             ResultSet rs = stmt.executeQuery("SELECT * FROM clientes")) { // Executa a consulta SQL

            while (rs.next()) { // Enquanto houver registros
                Vector<Object> row = new Vector<>(); // Cria um vetor para a linha
                row.add(rs.getInt("id"));
                row.add(rs.getString("nome"));
                row.add(rs.getString("telefone"));
                row.add(rs.getString("endereco"));
                row.add(rs.getString("email"));
                model.addRow(row); // Adiciona a linha ao modelo da tabela
            }
        } catch (SQLException e) { // Captura exceções de SQL
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para atualizar um cliente selecionado
    private void atualizarCliente() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para atualizar.");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        String nome = (String) table.getValueAt(row, 1);
        String telefone = (String) table.getValueAt(row, 2);
        String endereco = (String) table.getValueAt(row, 3);
        String email = (String) table.getValueAt(row, 4);

        // Supondo que você tem os valores originais armazenados (ex: originalNome, originalTelefone, etc.)
        boolean nomeChanged = !nome.equals(originalNome);
        boolean telefoneChanged = !telefone.equals(originalTelefone);
        boolean enderecoChanged = !endereco.equals(originalEndereco);
        boolean emailChanged = !email.equals(originalEmail);

        if (nomeChanged || telefoneChanged || enderecoChanged || emailChanged) {
            StringBuilder sql = new StringBuilder("UPDATE clientes SET ");
            if (nomeChanged) sql.append("nome = ?, ");
            if (telefoneChanged) sql.append("telefone = ?, ");
            if (enderecoChanged) sql.append("endereco = ?, ");
            if (emailChanged) sql.append("email = ?, ");
            sql.setLength(sql.length() - 2); // Remove a última vírgula e espaço
            sql.append(" WHERE id = ?");

            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                int index = 1;
                if (nomeChanged) stmt.setString(index++, nome);
                if (telefoneChanged) stmt.setString(index++, telefone);
                if (enderecoChanged) stmt.setString(index++, endereco);
                if (emailChanged) stmt.setString(index++, email);
                stmt.setInt(index, id);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
                    loadClientsData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma alteração detectada para atualizar.");
        }
    }


    // Método para deletar um cliente selecionado
    private void deletarCliente() {
        int row = table.getSelectedRow(); // Obtém a linha selecionada
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para deletar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este cliente?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) table.getValueAt(row, 0);

            try (Connection conn = DatabaseUtil.getConnection(); // Abre conexão
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM clientes WHERE id = ?")) {
                stmt.setInt(1, id);

                int affectedRows = stmt.executeUpdate(); // Executa a exclusão
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Cliente deletado com sucesso!");
                    loadClientsData(); // Recarrega os dados
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao deletar cliente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VisualizarClientesGUI().setVisible(true));
    }
}
