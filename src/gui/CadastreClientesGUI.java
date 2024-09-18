package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.border.Border;

import dao.ClienteDAO;
import model.Cliente;
import util.DatabaseSetup;

@SuppressWarnings("serial")
public class CadastreClientesGUI extends JFrame {
    private JTextField txtNome, txtTelefone, txtEndereco, txtEmail;
    private JButton btnSalvar, btnLimpar;
    private JPanel panel;

    public CadastreClientesGUI() {
        inicializaGUI();
        inicializaMenu();
    }

    private void inicializaMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem menuItemVisualizar = new JMenuItem("Visualizar Clientes");

        menuItemVisualizar.addActionListener(e -> {
            VisualizarClientesGUI visualizarGUI = new VisualizarClientesGUI();
            visualizarGUI.setVisible(true);
        });

        menu.add(menuItemVisualizar);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }
    
    private void inicializaGUI() {
        setTitle("Cadastro de Clientes");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel(new GridLayout(5, 2, 5, 5)); // Layout Grid
        //panel.setBackground(Color.LIGHT_GRAY); // Configura a cor de fundo do painel

        // Adicionando borda ao painel
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(padding);

        txtNome = new JTextField();
        txtTelefone = new JTextField();
        txtEndereco = new JTextField();
        txtEmail = new JTextField();

        btnSalvar = new JButton("Salvar");
        btnLimpar = new JButton("Limpar");

        panel.add(new JLabel("Nome:"));
        panel.add(txtNome);
        panel.add(new JLabel("Telefone:"));
        panel.add(txtTelefone);
        panel.add(new JLabel("Endereço:"));
        panel.add(txtEndereco);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(btnSalvar);
        panel.add(btnLimpar);

        add(panel, BorderLayout.CENTER);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					salvarCliente();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        btnLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
    }

    private void salvarCliente() throws SQLException {
        Cliente cliente = new Cliente(
            txtNome.getText(),
            txtTelefone.getText(),
            txtEndereco.getText(),
            txtEmail.getText()
        );

        ClienteDAO dao = new ClienteDAO();
        dao.adicionarCliente(cliente);
        JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
    }


    private void limparCampos() {
        txtNome.setText("");
        txtTelefone.setText("");
        txtEndereco.setText("");
        txtEmail.setText("");
    }

    public static void main(String[] args) {
    	// Inicializa o banco de dados
        DatabaseSetup.initializeDatabase();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CadastreClientesGUI().setVisible(true);
            }
        });
    }
}
