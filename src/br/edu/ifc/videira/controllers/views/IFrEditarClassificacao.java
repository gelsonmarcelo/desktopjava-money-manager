package br.edu.ifc.videira.controllers.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;

import br.edu.ifc.videira.DAOs.ClassificacaoDao;
import br.edu.ifc.videira.DAOs.UsuarioDao;
import br.edu.ifc.videira.beans.Classificacao;
import javax.swing.ImageIcon;
import javax.swing.Box;
import java.awt.Component;

public class IFrEditarClassificacao extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField tfClassificacao;
	private JTable table;
	private List<Object> registros = new ArrayList<Object>();
	ClassificacaoDao cfDao = new ClassificacaoDao();
	Classificacao cf = new Classificacao();
	private JTextField tfCod;
	private JButton btSalvar;
	private JButton btExcluir;

	public IFrEditarClassificacao() {
		super("Editar classificação # " + (++IFuLogin.openFrameCount) + "º", false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable

		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(496, 595);

		// Set the window's location.
		setLocation(IFuLogin.xOffset * IFuLogin.openFrameCount, IFuLogin.yOffset * IFuLogin.openFrameCount);
		getContentPane().setLayout(null);

		JLabel lbTitle = new JLabel("Editar Classifica\u00E7\u00E3o");
		lbTitle.setFont(MainInternalFrame.fonte3);
		lbTitle.setBounds(10, 0, 454, 55);
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitle);

		JLabel lblClassificacao = new JLabel("Classifica\u00E7\u00E3o:");
		lblClassificacao.setFont(MainInternalFrame.fonte4);
		lblClassificacao.setBounds(10, 464, 164, 34);
		getContentPane().add(lblClassificacao);

		tfClassificacao = new JTextField();
		tfClassificacao.setFont(MainInternalFrame.fonte4);
		tfClassificacao.setBounds(184, 464, 286, 34);
		getContentPane().add(tfClassificacao);
		tfClassificacao.setColumns(10);

		JScrollPane spClassificacao = new JScrollPane();
		spClassificacao.setBounds(10, 56, 460, 396);
		getContentPane().add(spClassificacao);

		tfCod = new JTextField("");
		tfCod.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				if (btSalvar.getText().equals("Cadastrar")) {
					btSalvar.setText("Atualizar");
					btSalvar.setIcon(new ImageIcon(
							IFiEditarInstituicao.class.getResource("/br/edu/ifc/videira/imgs/atualizar.png")));
				} else {
					btSalvar.setText("Cadastrar");
					btSalvar.setIcon(new ImageIcon(
							IFiEditarInstituicao.class.getResource("/br/edu/ifc/videira/imgs/salvar.png")));
				}
			}
		});
		tfCod.setEnabled(false);
		tfCod.setColumns(10);
		tfCod.setBounds(0, 545, 20, 20);
		tfCod.setVisible(false);
		getContentPane().add(tfCod);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				tfCod.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
				tfClassificacao.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));

				btExcluir.setEnabled(true);
				btExcluir.setToolTipText("Excluir classificação selecionada");
			}
		});
		table.setFont(MainInternalFrame.fonteTabela);
		spClassificacao.setViewportView(table);
		// Impede movimentação das colunas pelo usuário
		table.getTableHeader().setReorderingAllowed(false);
		table.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "C\u00F3digo", "Classifica\u00E7\u00E3o" }));
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBounds(0, 508, 480, 43);
		getContentPane().add(horizontalBox);
						
						Component horizontalGlue_1 = Box.createHorizontalGlue();
						horizontalBox.add(horizontalGlue_1);
				
						btSalvar = new JButton("Cadastrar");
						horizontalBox.add(btSalvar);
						btSalvar.setIcon(
								new ImageIcon(IFrEditarClassificacao.class.getResource("/br/edu/ifc/videira/imgs/salvar.png")));
						btSalvar.addActionListener(new AcaoSalvar());
						btSalvar.setToolTipText(
								"Preencha os dados para cadastrar uma nova classificação ou selecione uma linha da tabela para atualizar.");
						btSalvar.setFont(MainInternalFrame.fonteBotoes);
								
								Component horizontalStrut = Box.createHorizontalStrut(20);
								horizontalBox.add(horizontalStrut);
						
								JButton btLimpar = new JButton("Limpar");
								horizontalBox.add(btLimpar);
								btLimpar.setIcon(
										new ImageIcon(IFrEditarClassificacao.class.getResource("/br/edu/ifc/videira/imgs/apagador.png")));
								btLimpar.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										limpar();
									}
								});
								btLimpar.setToolTipText("Limpar sele\u00E7\u00E3o/valores");
								btLimpar.setFont(MainInternalFrame.fonteBotoes);
										
										Component horizontalStrut_1 = Box.createHorizontalStrut(20);
										horizontalBox.add(horizontalStrut_1);
								
										btExcluir = new JButton("Excluir");
										horizontalBox.add(btExcluir);
										btExcluir.setIcon(
												new ImageIcon(IFrEditarClassificacao.class.getResource("/br/edu/ifc/videira/imgs/lixeira.png")));
										btExcluir.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent arg0) {
												// Se voltarValor é diferente de 2 significa que o usuário não cancelou a
												// operação e faz a autenticação do usuário
												if (UsuarioDao.validar(true, false, null, null)) {
													// Definindo o código da linha selecionada
													cf.setCodigo(Integer.parseInt(String.valueOf(table.getValueAt(table.getSelectedRow(), 0))));
													// Enviando para o método responsável na classe RegistroDao
													cfDao.deletarClassificacao(cf);

													limpar();
													atualizarTabela();
												} // Se não validar a autenticação o próprio método vai devolver a mensagem.
											}
										});
										btExcluir.setEnabled(false);
										btExcluir.setToolTipText("Selecione uma classificação da tabela para excluir");
										btExcluir.setFont(MainInternalFrame.fonteBotoes);
										
										Component horizontalGlue = Box.createHorizontalGlue();
										horizontalBox.add(horizontalGlue);
		table.getColumnModel().getColumn(0).setPreferredWidth(15);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);

		atualizarTabela();
	}

	/**
	 * Limpar campos
	 */
	public void limpar() {
		tfCod.setText("");
		tfClassificacao.setText("");
		btExcluir.setEnabled(false);
		btExcluir.setToolTipText("Selecione uma classificação da tabela para excluir");
		table.clearSelection();
	}

	/**
	 * Pega informações do banco e atualiza a tabela
	 */
	public void atualizarTabela() {
		try {
			registros = cfDao.buscarTodos();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int x = 0; x != registros.size(); x++) {
				model.addRow((Object[]) registros.get(x));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Definição da ação do botão de salvar, se tiver seleção na tabela ele
	 * atualiza, senao cadastra
	 * 
	 * @author Gelson
	 *
	 */
	private class AcaoSalvar implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Se nao foi selecionado nada da tabela
			if (tfCod.getText().isEmpty()) {
				// Validação para campo em branco
				if (!tfClassificacao.getText().isEmpty()) {
					cf.setNome(tfClassificacao.getText());
					try {
						cfDao.cadastrarClassificacao(cf);
						JOptionPane.showMessageDialog(null, "Classificação cadastrada com sucesso!");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,
								"Ocorreu um erro, o cadastro da classificação não foi executado corretamente, contate o desenvolvedor e informe o código 'CL002'!",
								"Erro inesperado", JOptionPane.ERROR_MESSAGE);
					}
					atualizarTabela();
					limpar();
				} else {
					JOptionPane.showMessageDialog(null, "Informe o nome da classificação!",
							"Campo obrigatório em branco", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				// Se tem seleção o usuário vai atualizar
				if (!tfClassificacao.getText().equals("")) {
					if (JOptionPane.showConfirmDialog(null,
							"Se você editar uma classificação, todos os registros que estão com sua referência serão atualizados, deseja continuar?",
							"Confirmar a ação", JOptionPane.YES_NO_OPTION) == 0) {
						cf.setCodigo(Integer.parseInt(tfCod.getText()));
						cf.setNome(tfClassificacao.getText());
						try {
							cfDao.atualizarClassificacao(cf);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null,
									"Ocorreu um erro, a atualização da classificação não foi executada corretamente, contate o desenvolvedor e informe o código 'CL003'!",
									"Erro inesperado", JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
						atualizarTabela();
						limpar();
					} else {
						JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Informe o nome da classificação!",
							"Campo obrigatório em branco", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}
}