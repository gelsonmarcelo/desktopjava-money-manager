package br.edu.ifc.videira.controllers.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class IFuLogin extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	static int openFrameCount = 0;
	static final int xOffset = 10, yOffset = 0;

	private JTextField tfLogin;
	private JTextField psSenha;

	public IFuLogin() {
		super("Login", 
				false, // resizable
				false, // closable
				false, // maximizable
				false);// iconifiable

		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...

		setSize(527, 352);

		// Set the window's location.
		setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
		getContentPane().setLayout(null);

		JLabel lbTitle = new JLabel("Login");
		lbTitle.setFont(MainInternalFrame.fonte1);
		lbTitle.setBounds(0, 0, 511, 55);
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitle);

		JLabel lblNewLabel = new JLabel("Usu\u00E1rio:");
		lblNewLabel.setFont(MainInternalFrame.fonte3);
		lblNewLabel.setBounds(48, 98, 115, 34);
		getContentPane().add(lblNewLabel);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(MainInternalFrame.fonte3);
		lblSenha.setBounds(48, 151, 100, 34);
		getContentPane().add(lblSenha);

		tfLogin = new JTextField();
		tfLogin.setFont(MainInternalFrame.fonte4);
		tfLogin.setBounds(162, 98, 253, 34);
		getContentPane().add(tfLogin);
		tfLogin.setColumns(10);

		psSenha = new JPasswordField();
		psSenha.setFont(new Font("Arial", Font.PLAIN, 20));
		psSenha.setColumns(10);
		psSenha.setBounds(162, 151, 253, 34);
		getContentPane().add(psSenha);
		
		JButton btAcessar = new JButton("Acessar");
		btAcessar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Login bem sucedido
				 */
				/**
				 * ###Acessar banco e pegar id do banco para o usuário
				 */
//	###Remover coment			if(UsuarioDao.validar(true, tfLogin.getText(), psSenha.getText())) {
					MainInternalFrame.menuBar.setVisible(true);
//					JOptionPane.showMessageDialog(null, "Id do usuário: " + UsuarioDao.idUser);
					dispose();
//	###Remover coment			}else {
//	###Remover coment				JOptionPane.showMessageDialog(null,"A autenticação de usuário falhou. Login ou senha incorretos!", "Falha", JOptionPane.WARNING_MESSAGE);
//				}
			}
		});
		btAcessar.setFont(MainInternalFrame.fonte4);
		btAcessar.setBounds(52, 262, 155, 34);
		getContentPane().add(btAcessar);
		
		JButton btSair = new JButton("Sair");
		btSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btSair.setFont(MainInternalFrame.fonte4);
		btSair.setBounds(260, 262, 155, 34);
		getContentPane().add(btSair);
		
		JLabel lbSenhaPerdida = new JLabel("Perdi a senha!");
		lbSenhaPerdida.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				/**
				 * ###
				 */
				JOptionPane.showMessageDialog(null, "HAHAHAHA Fudeu-se! Isto ainda não está pronto, contate o desenvolvedor, ele sabe o que fazer!");
			}
		});
		lbSenhaPerdida.setFont(MainInternalFrame.fonte6);
		lbSenhaPerdida.setBounds(162, 188, 123, 14);
		getContentPane().add(lbSenhaPerdida);
		
		JButton btCadastrar = new JButton("Cadastrar");
		btCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FuCadastrarUsuario frame = new FuCadastrarUsuario();
				frame.setVisible(true);
			}
		});
		btCadastrar.setFont(MainInternalFrame.fonte4);
		btCadastrar.setBounds(162, 213, 155, 34);
		getContentPane().add(btCadastrar);
	}
	/**
	 * Centraliza janela
	 */
	public void centralizarJanela() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		setLocation(x, y);
	}
}