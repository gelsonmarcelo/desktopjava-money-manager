package br.edu.ifc.videira.controllers.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import br.edu.ifc.videira.DAOs.UsuarioDao;
import br.edu.ifc.videira.beans.Usuario;
import javax.swing.ImageIcon;

public class IFuEditarUsuario extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfLogin;
	private JTextField tfSenha;
	private JTextField tfSalario;
	private JTextField tfNovaSenha;
	private JTextField tfConfirmaSenha;
	
	private String[] dadosUsuario = UsuarioDao.buscarUsuario();
	
	/**
	 * Preciso pedir confirmação de senha antiga
	 * @throws Exception 
	 * @throws SQLException 
	 * 
	 */
	public IFuEditarUsuario() throws SQLException, Exception {
		super("Editar Usuário # " + (++IFuLogin.openFrameCount) + "º", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable

		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(427, 374);

		// Set the window's location.
		setLocation(IFuLogin.xOffset * IFuLogin.openFrameCount, IFuLogin.yOffset * IFuLogin.openFrameCount);
		getContentPane().setLayout(null);

		JLabel lbTitle = new JLabel("Editar Usu\u00E1rio");
		lbTitle.setFont(MainInternalFrame.fonte3);
		lbTitle.setBounds(0, 0, 397, 55);
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitle);

		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLogin.setFont(MainInternalFrame.fonte5);
		lblLogin.setBounds(10, 62, 160, 34);
		getContentPane().add(lblLogin);

		JLabel lblSenha = new JLabel("*Senha atual:");
		lblSenha.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSenha.setFont(MainInternalFrame.fonte5);
		lblSenha.setBounds(10, 107, 160, 34);
		getContentPane().add(lblSenha);

		JLabel lblSalario = new JLabel("*Sal\u00E1rio:");
		lblSalario.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSalario.setFont(MainInternalFrame.fonte5);
		lblSalario.setBounds(10, 242, 160, 34);
		getContentPane().add(lblSalario);

		tfLogin = new JTextField(dadosUsuario[0]);
		tfLogin.setEnabled(false);
		
		tfLogin.setFont(MainInternalFrame.fonte5);
		tfLogin.setBounds(179, 62, 197, 34);
		getContentPane().add(tfLogin);
		tfLogin.setColumns(10);

		tfSenha = new JPasswordField();
		tfSenha.setFont(MainInternalFrame.fonte5);
		tfSenha.setColumns(10);
		tfSenha.setBounds(179, 107, 197, 34);
		getContentPane().add(tfSenha);

		tfSalario = new JTextField(dadosUsuario[1]);
		tfSalario.setFont(MainInternalFrame.fonte5);
		tfSalario.setToolTipText(
				"Seu sal\u00E1rio ser\u00E1 usado para calcular porcentagens de gastos e realizar estat\u00EDsticas");
		tfSalario.setColumns(10);
		tfSalario.setBounds(180, 242, 130, 34);
		getContentPane().add(tfSalario);
		
		tfNovaSenha = new JPasswordField();
		tfNovaSenha.setFont(MainInternalFrame.fonte5);
		tfNovaSenha.setColumns(10);
		tfNovaSenha.setBounds(179, 152, 197, 34);
		getContentPane().add(tfNovaSenha);
		
		JLabel lbNovaSenha = new JLabel("*Nova senha:");
		lbNovaSenha.setHorizontalAlignment(SwingConstants.RIGHT);
		lbNovaSenha.setFont(MainInternalFrame.fonte5);
		lbNovaSenha.setBounds(10, 152, 160, 34);
		getContentPane().add(lbNovaSenha);
		
		tfConfirmaSenha = new JPasswordField();
		tfConfirmaSenha.setFont(MainInternalFrame.fonte5);
		tfConfirmaSenha.setColumns(10);
		tfConfirmaSenha.setBounds(180, 197, 196, 34);
		getContentPane().add(tfConfirmaSenha);
		
		JLabel lblConfirmarSenha = new JLabel("*Confirmar senha:");
		lblConfirmarSenha.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConfirmarSenha.setFont(MainInternalFrame.fonte5);
		lblConfirmarSenha.setBounds(0, 197, 170, 34);
		getContentPane().add(lblConfirmarSenha);
		
		JButton btSalvar = new JButton("Salvar");
		btSalvar.setIcon(new ImageIcon(IFuEditarUsuario.class.getResource("/br/edu/ifc/videira/imgs/salvar.png")));
		btSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Se não tem campos vazios
				if (!tfLogin.getText().equals("") && 
						!tfSenha.getText().equals("") &&
						!tfNovaSenha.getText().equals("") &&
						!tfConfirmaSenha.getText().equals("") &&
						!tfSalario.getText().equals("")) {
					//Se login e senha estao corretos
					if(UsuarioDao.validar(false, false, tfLogin.getText(), tfSenha.getText())) {
						if (tfNovaSenha.getText().equals(tfConfirmaSenha.getText())) {
							try {
								// Atribuição dos valores dos campos para o objeto
								Usuario us = new Usuario();
								UsuarioDao usDao = new UsuarioDao();

								us.setSenha(tfNovaSenha.getText());
								us.setSalario(Double.parseDouble(tfSalario.getText()));

								// Chamada do método de cadastro na classe Dao
								usDao.atualizarUsuario(us);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null, e.getMessage());
							}
							//atualizarTabela();
							//limpar();
							dispose();
						}else{
							JOptionPane.showMessageDialog(null, "As senhas não coincidem!", "Verifique", JOptionPane.WARNING_MESSAGE);
						}
					}else {
						JOptionPane.showMessageDialog(null, "Usuario e/ou senha incorreto(s)!", "Erro", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Por favor preencha todas as informações necessárias", "Aviso",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btSalvar.setFont(MainInternalFrame.fonteBotoes);
		btSalvar.setBounds(58, 287, 155, 34);
		getContentPane().add(btSalvar);
		
		JButton btCancelar = new JButton("Cancelar");
		btCancelar.setIcon(new ImageIcon(IFuEditarUsuario.class.getResource("/br/edu/ifc/videira/imgs/cancelar.png")));
		btCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btCancelar.setFont(MainInternalFrame.fonteBotoes);
		btCancelar.setBounds(221, 287, 155, 34);
		getContentPane().add(btCancelar);
	}
}