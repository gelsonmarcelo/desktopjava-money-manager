package br.edu.ifc.videira.controllers.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import br.edu.ifc.videira.DAOs.ClassificacaoDao;
import br.edu.ifc.videira.DAOs.InstituicaoDao;
import br.edu.ifc.videira.DAOs.QuemDao;
import br.edu.ifc.videira.DAOs.RegistroDao;
import br.edu.ifc.videira.beans.Registro;
import br.edu.ifc.videira.utils.JNumberFormatField;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class IFrRegistrar extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	public JNumberFormatField tfValor;
	public String valorOriginalRegistro;
	public JDateChooser dcData;
	public JTextArea txDescricao;
	public final ButtonGroup grupoTipo = new ButtonGroup();
	private QuemDao qmDao = new QuemDao();
	public ClassificacaoDao clDao = new ClassificacaoDao();
	public InstituicaoDao inDao = new InstituicaoDao();
	private Registro rg = new Registro();
	private RegistroDao rgDao = new RegistroDao();
	
	public JComboBox<Object> cbQuem; 
	public JComboBox<Object> cbClassificacao;
	public JComboBox<Object> cbInstituicao;
	public JComboBox<Object> cbSub;
	public JTextField tfCodigoEdicao;
	public JRadioButton rbReceita;
	public JRadioButton rbDespesa;
	public JRadioButton rbDevem;
	public JRadioButton rbDevo;
	private JButton btRegistrar;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IFrRegistrar() {
		super("Registrar # " + (++IFuLogin.openFrameCount) + "º", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable

		// ...Create the GUI and put it in the window...
		// ...Then set the window size or call pack...
		setSize(1000, 522);
		
		// Set the window's location.
		setLocation(IFuLogin.xOffset * IFuLogin.openFrameCount, IFuLogin.yOffset * IFuLogin.openFrameCount);
		getContentPane().setLayout(null);

		JLabel lblData = new JLabel("*Data:");
		lblData.setFont(MainInternalFrame.fonte5);
		lblData.setBounds(260, 208, 95, 26);
		getContentPane().add(lblData);

		JLabel lbTitle = new JLabel("Lan\u00E7ar Opera\u00E7\u00E3o");
		lbTitle.setFont(MainInternalFrame.fonte1);
		lbTitle.setBounds(0, 0, 984, 55);
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitle);

		JLabel lblValor = new JLabel("*Valor:");
		lblValor.setFont(MainInternalFrame.fonte5);
		lblValor.setBounds(52, 208, 73, 26);
		getContentPane().add(lblValor);

		tfValor = new JNumberFormatField();
		tfValor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					dcData.getCalendarButton().doClick();
				}
			}
		});
		tfValor.setText("");
		tfValor.setFont(MainInternalFrame.FonteJNumberFormatField);
		tfValor.setColumns(10);
		tfValor.setBounds(52, 239, 129, 26);
		getContentPane().add(tfValor);
		

		JButton btEditarQuem = new JButton("Editar pessoas");
		btEditarQuem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IFpEditarPessoas frame = new IFpEditarPessoas();
				frame.setVisible(true);
				MainInternalFrame.desktop.add(frame);
				try {
					frame.setSelected(true);
				} catch (java.beans.PropertyVetoException e1) {
					e1.printStackTrace();
				}
			}
		});
		btEditarQuem.setFont(MainInternalFrame.fonte5);
		btEditarQuem.setBounds(254, 303, 161, 26);
		getContentPane().add(btEditarQuem);

		JLabel lblTipoDeRegistro = new JLabel("*Tipo de Registro:");
		lblTipoDeRegistro.setFont(MainInternalFrame.fonte5);
		lblTipoDeRegistro.setBounds(52, 66, 161, 27);
		getContentPane().add(lblTipoDeRegistro);

		rbReceita = new JRadioButton("Receita");
		grupoTipo.add(rbReceita);
		rbReceita.setFont(MainInternalFrame.fonte5);
		rbReceita.setBounds(52, 126, 119, 23);
		getContentPane().add(rbReceita);

		rbDespesa = new JRadioButton("Despesa");
		grupoTipo.add(rbDespesa);
		rbDespesa.setFont(MainInternalFrame.fonte5);
		rbDespesa.setBounds(52, 152, 119, 23);
		getContentPane().add(rbDespesa);

		rbDevem = new JRadioButton("Me devem");
		grupoTipo.add(rbDevem);
		rbDevem.setFont(MainInternalFrame.fonte5);
		rbDevem.setBounds(200, 126, 155, 23);
		getContentPane().add(rbDevem);

		rbDevo = new JRadioButton("Estou devendo");
		grupoTipo.add(rbDevo);
		rbDevo.setFont(MainInternalFrame.fonte5);
		rbDevo.setBounds(200, 152, 155, 23);
		getContentPane().add(rbDevo);

		JSeparator separator = new JSeparator();
		separator.setBackground(Color.BLUE);
		separator.setBounds(52, 188, 363, 9);
		getContentPane().add(separator);

		JLabel lblQuem = new JLabel("*Quem:");
		lblQuem.setFont(MainInternalFrame.fonte5);
		lblQuem.setBounds(52, 303, 73, 26);
		getContentPane().add(lblQuem);

		cbQuem = new JComboBox<>();
		cbQuem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					cbClassificacao.showPopup();
					cbClassificacao.grabFocus();
				}
			}
		});
		//Preenchimento do comboBox com valores cadastrados no banco
		try {
			cbQuem.setModel(new DefaultComboBoxModel(qmDao.buscarNomesQuem(false)));
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR001'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
		}
		cbQuem.setFont(MainInternalFrame.fonte5);
		cbQuem.setBounds(52, 335, 363, 26);
		getContentPane().add(cbQuem);
		
		cbClassificacao = new JComboBox<>();
		cbClassificacao.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					txDescricao.grabFocus();
				}
			}
		});
		//Preenchimento do comboBox com valores cadastrados no banco
		try {
			cbClassificacao.setModel(new DefaultComboBoxModel(clDao.buscarNomesClassificacoes()));
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR004'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
		}
		cbClassificacao.setFont(MainInternalFrame.fonte5);
		cbClassificacao.setBounds(656, 66, 271, 27);
		getContentPane().add(cbClassificacao);
		
		cbInstituicao = new JComboBox<>();
		cbInstituicao.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btRegistrar.doClick();
				}
			}
		});
		//Preenchimento do comboBox com valores cadastrados no banco
		try {
			cbInstituicao.setModel(new DefaultComboBoxModel(inDao.buscarNomesInstituicao()));
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR006'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
		}
		cbInstituicao.setToolTipText("");
		cbInstituicao.setBounds(656, 335, 271, 27);
		getContentPane().add(cbInstituicao);
		cbInstituicao.setFont(MainInternalFrame.fonte5);

		JLabel lblDescrio = new JLabel("Descri\u00E7\u00E3o:");
		lblDescrio.setFont(MainInternalFrame.fonte5);
		lblDescrio.setBounds(480, 126, 95, 26);
		getContentPane().add(lblDescrio);

		JScrollPane spDescricao = new JScrollPane();
		spDescricao.setBounds(480, 161, 447, 138);
		getContentPane().add(spDescricao);

		txDescricao = new JTextArea();
		txDescricao.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					cbInstituicao.showPopup();
					cbInstituicao.grabFocus();
				}
			}
		});
		txDescricao.setFont(MainInternalFrame.fonte5);
		spDescricao.setViewportView(txDescricao);
		txDescricao.setWrapStyleWord(true);
		txDescricao.setLineWrap(true);

		JLabel lblClassificao = new JLabel("*Classifica\u00E7\u00E3o:");
		lblClassificao.setFont(MainInternalFrame.fonte5);
		lblClassificao.setBounds(480, 66, 138, 26);
		getContentPane().add(lblClassificao);

		JLabel lblLocal = new JLabel("*Institui\u00E7\u00E3o/Local:");
		lblLocal.setFont(MainInternalFrame.fonte5);
		lblLocal.setBounds(480, 336, 170, 26);
		getContentPane().add(lblLocal);

		JButton btLimpar = new JButton("Limpar");
		btLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpar();
			}
		});
		btLimpar.setToolTipText("Limpar todos os dados");
		btLimpar.setFont(MainInternalFrame.fonte4);
		btLimpar.setBounds(463, 427, 155, 41);
		getContentPane().add(btLimpar);

		dcData = new JDateChooser();
		dcData.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					cbQuem.showPopup();
					cbQuem.grabFocus();
				}
			}
		});
		//Pega o campo de edição do DateChooser para trabalhá-lo 
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dcData.getDateEditor();
		//Deixa máscara do editor visível e não permite o usuário digitar letras ou caracteres que não sejam números
		editor.setMaskVisible(true);
		dcData.setBounds(254, 239, 161, 26);
		dcData.setFont(new Font("Calibri", Font.PLAIN, 20));
		getContentPane().add(dcData);
		
		cbSub = new JComboBox<Object>();
		cbSub.setEnabled(false);
		cbSub.setToolTipText("Indispon\u00EDvel");
		cbSub.setFont(MainInternalFrame.fonte5);
		cbSub.setBounds(656, 373, 271, 27);
		getContentPane().add(cbSub);
		cbSub.setVisible(false);
		
		JLabel lbSub = new JLabel("Sub Local:");
		lbSub.setFont(MainInternalFrame.fonte5);
		lbSub.setBounds(480, 373, 155, 26);
		lbSub.setEnabled(false);
		getContentPane().add(lbSub);
		lbSub.setVisible(false);
		
		//### - Se for selecionado preciso mudar o campo da data para apenas dia do vencimento e verificar informações que precisa ocultar
		JRadioButton rbConta = new JRadioButton("Conta Mensal");
		rbConta.setEnabled(false);
		rbConta.setToolTipText("Registra uma conta para lembretes mensais");
		rbConta.setFont(MainInternalFrame.fonte5);
		rbConta.setBounds(108, 100, 155, 23);
		getContentPane().add(rbConta);
				
		btRegistrar = new JButton("Finalizar");
		btRegistrar.setToolTipText("Terminar lan\u00E7amento");
		btRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Validação de campos em branco
				if (grupoTipo.getSelection() != null && !tfValor.getText().equals("R$ 0,00") && dcData.getDate() != null && cbQuem.getSelectedIndex() != 0 && cbClassificacao.getSelectedIndex() != 0 && cbInstituicao.getSelectedIndex() != 0) {
				// ### - fazer validações específicas conforme check selecionado, conforme descreve no artefato.	
					//try geral
					try {
						//Pegar/verificar tipo de registro
						if(rbReceita.isSelected()) {
							rg.setIdTipo(1);
						}else if (rbDespesa.isSelected()) {
							rg.setIdTipo(2);
						}else if (rbDevem.isSelected()) {
							rg.setIdTipo(3);
						}else if (rbDevo.isSelected()) {
							rg.setIdTipo(4);
						}else {
							//### - Fazer validação de campos preenchidos, se chegar nessa parte deverá ser outro problema e não falta de preenchimento
							JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR002'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
						}
						
						//Pegar valor
						try {
							rg.setValor(tfValor.getText());
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "O programa não conseguiu converter o valor em números, verifique os dados digitados", "Falha de conversão", JOptionPane.WARNING_MESSAGE);
							e2.printStackTrace();
							//Limpa campo incorreto
							tfValor.setText("");
						}
						
						//Pegar data
						rg.setData(dcData.getDate());
						
						//Pegar pessoa "quem"
						try {
							//Formatar para pegar apenas o ID a partir da opção selecionada.
							rg.setIdQuem(Integer.parseInt(String.valueOf(cbQuem.getSelectedItem()).split("\\*")[0]));
							//Formatar para pegar apenas o nome a partir da opção selecionada.
							rg.setNomeQuem(String.valueOf(cbQuem.getSelectedItem()).split("\\*")[1]);	
							//Formatar para pegar apenas o saldo a partir da opção selecionada.
							rg.setSaldoQuem(Double.parseDouble(String.valueOf(cbQuem.getSelectedItem()).split("\\*")[2]));	
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "Ocorreu um erro, não foi possível obter adequadamente os dados da pessoa selecionada, contate o desenvolvedor e informe o código 'VR000'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
							e2.printStackTrace();
						}
						
						//Pegar classificação
						try {
							//Formatar para pegar apenas o ID a partir do nome selecionado.
							rg.setIdClassificacao(Integer.parseInt(String.valueOf(cbClassificacao.getSelectedItem()).split("\\*")[0]));	
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR005'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
							e2.printStackTrace();
						}
						
						//Pegar descrição
						rg.setDescricao(txDescricao.getText());
						
						//Pegar instituição
						try {
							//Formatar para pegar apenas o ID a partir da opção selecionada.
							rg.setIdInstituicao(Integer.parseInt(String.valueOf(cbInstituicao.getSelectedItem()).split("\\*")[0]));
							//Formatar para pegar apenas o nome a partir da opção selecionada.
							rg.setNomeInstituicao(String.valueOf(cbInstituicao.getSelectedItem()).split("\\*")[1]);	
							//Formatar para pegar apenas o saldo a partir da opção selecionada.
							rg.setSaldoInstituicao(Double.parseDouble(String.valueOf(cbInstituicao.getSelectedItem()).split("\\*")[2]));
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR007'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
							e2.printStackTrace();
						}
						
						//Chamada do método de cadastro na classe Dao
						rgDao.registrar(rg, Integer.parseInt(tfCodigoEdicao.getText()), valorOriginalRegistro);
						limpar();

					}catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR008'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				} else { //Nesse caso foi deixado algo sem preencher.
					JOptionPane.showMessageDialog(null, "Por favor preencha todas as informações necessárias antes de prosseguir.", "Aviso", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btRegistrar.setFont(MainInternalFrame.fonte4);
		btRegistrar.setBounds(296, 426, 155, 41);
		getContentPane().add(btRegistrar);
		
		tfCodigoEdicao = new JTextField("-1");
		tfCodigoEdicao.setEditable(false);
		tfCodigoEdicao.setToolTipText("Clique no bot\u00E3o de limpar, do lado direito, para deixar de editar esse registro");
		tfCodigoEdicao.setEnabled(false);
		tfCodigoEdicao.setBounds(0, 460, 56, 32);
		tfCodigoEdicao.setVisible(false);
		getContentPane().add(tfCodigoEdicao);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBackground(Color.BLUE);
		separator_1.setBounds(52, 286, 363, 9);
		getContentPane().add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBackground(Color.BLUE);
		separator_2.setBounds(480, 106, 447, 9);
		getContentPane().add(separator_2);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBackground(Color.BLUE);
		separator_3.setBounds(480, 310, 447, 9);
		getContentPane().add(separator_3);
		
	}
	
	/**
	 * Limpa os campos
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void limpar() {
		grupoTipo.clearSelection();
		tfValor.setText(null);
		dcData.setDate(null);
		cbQuem.setSelectedIndex(-1);
		cbClassificacao.setSelectedIndex(-1);
		txDescricao.setText(null);
		cbInstituicao.setSelectedIndex(-1);
		cbSub.setSelectedIndex(-1);
		tfCodigoEdicao.setText("-1");
		rbReceita.setEnabled(true);
		rbDespesa.setEnabled(true);
		rbDevo.setEnabled(true);
		rbDevem.setEnabled(true);
		//Atualização no preenchimento do comboBox com valores cadastrados no banco
		try {
			cbQuem.setModel(new DefaultComboBoxModel(qmDao.buscarNomesQuem(false)));
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR001'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		//Atualização no preenchimento do comboBox com valores cadastrados no banco
		try {
			cbInstituicao.setModel(new DefaultComboBoxModel(inDao.buscarNomesInstituicao()));
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR006'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
}