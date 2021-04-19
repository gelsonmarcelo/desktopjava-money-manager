package br.edu.ifc.videira.controllers.views;

import java.awt.Dimension;
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

import br.edu.ifc.videira.DAOs.UsuarioDao;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class IFuLogin extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	static int openFrameCount = 0;
	static final int xOffset = 10, yOffset = 0;
	UsuarioDao usDao = new UsuarioDao();

	private JTextField tfLogin;
	private JTextField psSenha;
	private JButton btAcessar;

	public IFuLogin() {
		super("Login", false, // resizable
				false, // closable
				false, // maximizable
				false);// iconifiable

		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...

		setSize(360, 292);

		// Set the window's location.
		setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
		getContentPane().setLayout(null);

		JLabel lbTitle = new JLabel("Login");
		lbTitle.setFont(MainInternalFrame.fonte4);
		lbTitle.setBounds(0, 0, 326, 34);
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitle);

		JLabel lbUsuario = new JLabel("Usu\u00E1rio:");
		lbUsuario.setFont(MainInternalFrame.fonte5);
		lbUsuario.setBounds(29, 45, 115, 32);
		getContentPane().add(lbUsuario);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(MainInternalFrame.fonte5);
		lblSenha.setBounds(27, 92, 100, 32);
		getContentPane().add(lblSenha);

		tfLogin = new JTextField();
		/*
		 * Quando o foco está nessa campo de Login e pressionar enter o foco vai para o próximo campo
		 */
		tfLogin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					psSenha.grabFocus();
				}
			}
		});
		tfLogin.setFont(MainInternalFrame.fonte5);
		tfLogin.setBounds(127, 44, 197, 34);
		getContentPane().add(tfLogin);
		tfLogin.setColumns(10);

		psSenha = new JPasswordField();
		psSenha.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btAcessar.setText("Aguarde...");
					btAcessar.doClick();
				}
			}
		});
		psSenha.setFont(MainInternalFrame.fonte5);
		psSenha.setColumns(10);
		psSenha.setBounds(127, 92, 197, 32);
		getContentPane().add(psSenha);

		btAcessar = new JButton("Acessar");
		btAcessar.setIcon(new ImageIcon(IFuLogin.class.getResource("/br/edu/ifc/videira/imgs/entrar.png")));
		btAcessar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Verifica o login
				 */
				if (UsuarioDao.validar(false, true, tfLogin.getText(), psSenha.getText())) {
					MainInternalFrame.menuBar.setVisible(true);
					JOptionPane.showMessageDialog(null, "Id do usuário: " + UsuarioDao.idUser);
					//Define o tema trazido do banco com base no usuário
					MainInternalFrame.cbTema.setSelectedItem(usDao.recuperarTema());
					
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "A autenticação de usuário falhou. Login ou senha incorretos!",
							"Falha", JOptionPane.WARNING_MESSAGE);
				}
				btAcessar.setText("Acessar");
			}
		});
		btAcessar.setFont(MainInternalFrame.fonteBotoes);
		btAcessar.setBounds(27, 163, 139, 34);
		getContentPane().add(btAcessar);

		JButton btSair = new JButton("Sair");
		btSair.setIcon(new ImageIcon(IFuLogin.class.getResource("/br/edu/ifc/videira/imgs/cancelar.png")));
		btSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btSair.setFont(MainInternalFrame.fonteBotoes);
		btSair.setBounds(185, 163, 139, 34);
		getContentPane().add(btSair);

		JLabel lbSenhaPerdida = new JLabel("Perdi a senha!");
		lbSenhaPerdida.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				/**
				 * ###
				 */
				JOptionPane.showMessageDialog(null,
						"Isto ainda não está pronto, contate o desenvolvedor, ele sabe o que fazer!");
			}
		});
		lbSenhaPerdida.setFont(MainInternalFrame.fonte6);
		lbSenhaPerdida.setBounds(127, 127, 123, 14);
		getContentPane().add(lbSenhaPerdida);

		JButton btCadastrar = new JButton("Cadastrar");
		btCadastrar.setIcon(new ImageIcon(IFuLogin.class.getResource("/br/edu/ifc/videira/imgs/cadastro.png")));
		btCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FuCadastrarUsuario frame = new FuCadastrarUsuario();
				frame.setVisible(true);
			}
		});
		btCadastrar.setFont(MainInternalFrame.fonteBotoes);
		btCadastrar.setBounds(86, 208, 164, 34);
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