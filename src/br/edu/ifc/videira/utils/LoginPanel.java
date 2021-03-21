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
		lbUsuario.setFont(new Font("IrisUPC", Font.PLAIN, 27));
		lbUsuario.setBounds(10, 26, 138, 27);
		add(lbUsuario);
		
		tfUsuario = new JTextField();
		tfUsuario.setFont(new Font("Calibri", Font.PLAIN, 20));
		tfUsuario.setColumns(10);
		tfUsuario.setBounds(10, 49, 215, 26);
		add(tfUsuario);
		
		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("IrisUPC", Font.PLAIN, 27));
		lblSenha.setBounds(10, 86, 138, 27);
		add(lblSenha);
		
		psSenha = new JPasswordField();
		psSenha.setFont(new Font("Calibri", Font.PLAIN, 20));
		psSenha.setColumns(10);
		psSenha.setBounds(10, 109, 215, 26);
		add(psSenha);
	}
}
