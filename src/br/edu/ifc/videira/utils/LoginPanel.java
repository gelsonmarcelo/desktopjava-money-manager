package br.edu.ifc.videira.utils;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JPasswordField;

public class LoginPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JPasswordField psSenha;
	public JTextField tfUsuario;

	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		setLayout(null);
		
		JLabel lbUsuario = new JLabel("Usu\u00E1rio:");
		lbUsuario.setFont(new Font("Sitka Subheading", Font.PLAIN, 20));
		lbUsuario.setBounds(10, 22, 82, 27);
		add(lbUsuario);
		
		tfUsuario = new JTextField();
		tfUsuario.setFont(new Font("Sitka Subheading", Font.PLAIN, 15));
		tfUsuario.setColumns(10);
		tfUsuario.setBounds(10, 45, 219, 26);
		add(tfUsuario);
		
		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("Sitka Subheading", Font.PLAIN, 20));
		lblSenha.setBounds(10, 82, 82, 27);
		add(lblSenha);
		
		psSenha = new JPasswordField();
		psSenha.setFont(new Font("Sitka Subheading", Font.PLAIN, 15));
		psSenha.setColumns(10);
		psSenha.setBounds(10, 108, 219, 26);
		add(psSenha);
	}
}
