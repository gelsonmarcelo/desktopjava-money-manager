package br.edu.ifc.videira.controllers.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import br.edu.ifc.videira.DAOs.UsuarioDao;
import br.edu.ifc.videira.beans.Usuario;

public class FuCadastrarUsuario extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfLogin;
	private JTextField tfSalario;
	private JTextField tfNovaSenha;
	private JTextField tfConfirmaSenha;

	/**
	 * Preciso pedir confirmação de senha antiga
	 * 
	 */
	public FuCadastrarUsuario() {
		super("Cadastrar Usuário");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// ...Then set the window size or call pack...
		setSize(527, 391);
		getContentPane().setLayout(null);
		
		JLabel lbTitulo = new JLabel("Cadastrar Usu\u00E1rio");
		lbTitulo.setFont(MainInternalFrame.fonte1);
		lbTitulo.setBounds(0, 0, 511, 55);
		lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitulo);

		JLabel lbLogin = new JLabel("Login:");
		lbLogin.setHorizontalAlignment(SwingConstants.RIGHT);
		lbLogin.setFont(MainInternalFrame.fonte4);
		lbLogin.setBounds(10, 87, 197, 34);
		getContentPane().add(lbLogin);

		JLabel lbSalario = new JLabel("Sal\u00E1rio:");
		lbSalario.setHorizontalAlignment(SwingConstants.RIGHT);
		lbSalario.setFont(MainInternalFrame.fonte4);
		lbSalario.setBounds(10, 222, 197, 34);
		getContentPane().add(lbSalario);

		tfLogin = new JTextField();
		tfLogin.setFont(MainInternalFrame.fonte4);
		tfLogin.setBounds(216, 87, 253, 34);
		getContentPane().add(tfLogin);
		tfLogin.setColumns(10);

		// ### Colocar mascara de numero neste campo
		tfSalario = new JTextField();
		tfSalario.setFont(MainInternalFrame.fonte4);
		tfSalario.setToolTipText(
				"Seu sal\u00E1rio ser\u00E1 usado para calcular porcentagens de gastos e realizar estat\u00EDsticas");
		tfSalario.setColumns(10);
		tfSalario.setBounds(216, 222, 181, 34);
		getContentPane().add(tfSalario);
		
		tfNovaSenha = new JTextField();
		tfNovaSenha.setFont(MainInternalFrame.fonte4);
		tfNovaSenha.setColumns(10);
		tfNovaSenha.setBounds(216, 133, 253, 34);
		getContentPane().add(tfNovaSenha);
		
		JLabel lbNovaSenha = new JLabel("Nova senha:");
		lbNovaSenha.setHorizontalAlignment(SwingConstants.RIGHT);
		lbNovaSenha.setFont(MainInternalFrame.fonte4);
		lbNovaSenha.setBounds(10, 132, 197, 34);
		getContentPane().add(lbNovaSenha);
		
		tfConfirmaSenha = new JTextField();
		tfConfirmaSenha.setFont(MainInternalFrame.fonte4);
		tfConfirmaSenha.setColumns(10);
		tfConfirmaSenha.setBounds(216, 178, 253, 34);
		getContentPane().add(tfConfirmaSenha);
		
		JLabel lblConfirmarSenha = new JLabel("Confirmar senha:");
		lblConfirmarSenha.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConfirmarSenha.setFont(MainInternalFrame.fonte4);
		lblConfirmarSenha.setBounds(10, 178, 197, 34);
		getContentPane().add(lblConfirmarSenha);
		
		JButton btSalvar = new JButton("Salvar");
		btSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (!tfLogin.getText().equals("") &&
						!tfNovaSenha.getText().equals("") &&
						!tfSalario.getText().equals("")) {
					if (tfNovaSenha.getText().equals(tfConfirmaSenha.getText())) {
						Usuario us = new Usuario();
						UsuarioDao uDao = new UsuarioDao();
						us.setLogin(tfLogin.getText());
						us.setSenha(tfNovaSenha.getText());
						us.setSalario(Double.parseDouble(tfSalario.getText()));
						try {
							if (uDao.cadastrarUsuario(us)) {
								JOptionPane.showMessageDialog(null, "Cadastrado com sucesso!");
								dispose();
							} else {
								JOptionPane.showMessageDialog(null, "Falha ao cadastrar!");
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}else {
						JOptionPane.showMessageDialog(null, "Senhas não coincidem!");
					}
				}else {
					JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
				}
			}
		});
		btSalvar.setFont(MainInternalFrame.fonte4);
		btSalvar.setBounds(52, 284, 155, 34);
		getContentPane().add(btSalvar);
		
		JButton btCancelar = new JButton("Cancelar");
		btCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btCancelar.setFont(MainInternalFrame.fonte4);
		btCancelar.setBounds(301, 284, 155, 34);
		getContentPane().add(btCancelar);
	}
}