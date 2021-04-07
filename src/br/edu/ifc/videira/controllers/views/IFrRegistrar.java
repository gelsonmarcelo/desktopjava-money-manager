package br.edu.ifc.videira.controllers.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IFrRegistrar() {
		super("Registrar # " + (++IFuLogin.openFrameCount) + "º", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable
		getContentPane().setFont(new Font("IrisUPC", Font.PLAIN, 29));

		// ...Create the GUI and put it in the window...
		// ...Then set the window size or call pack...
		setSize(1000, 522);
		
		// Set the window's location.
		setLocation(IFuLogin.xOffset * IFuLogin.openFrameCount, IFuLogin.yOffset * IFuLogin.openFrameCount);
		getContentPane().setLayout(null);

		JLabel lblData = new JLabel("Data:");
		lblData.setFont(MainInternalFrame.fonte5);
		lblData.setBounds(260, 198, 95, 26);
		getContentPane().add(lblData);

		JLabel lbTitle = new JLabel("Registrar");
		lbTitle.setFont(MainInternalFrame.fonte1);
		lbTitle.setBounds(0, 0, 984, 55);
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitle);

		JLabel lblValor = new JLabel("Valor:");
		lblValor.setFont(MainInternalFrame.fonte5);
		lblValor.setBounds(52, 198, 73, 26);
		getContentPane().add(lblValor);

		tfValor = new JNumberFormatField();
		tfValor.setText("1000");
		tfValor.setFont(new Font("Calibri", Font.PLAIN, 20));
		tfValor.setColumns(10);
		tfValor.setBounds(52, 229, 129, 26);
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

		JLabel lblTipoDeRegistro = new JLabel("Tipo de Registro:");
		lblTipoDeRegistro.setFont(MainInternalFrame.fonte5);
		lblTipoDeRegistro.setBounds(31, 66, 150, 27);
		getContentPane().add(lblTipoDeRegistro);

		rbReceita = new JRadioButton("Receita");
		grupoTipo.add(rbReceita);
		rbReceita.setFont(MainInternalFrame.fonte5);
		rbReceita.setBounds(52, 100, 119, 23);
		getContentPane().add(rbReceita);

		rbDespesa = new JRadioButton("Despesa");
		grupoTipo.add(rbDespesa);
		rbDespesa.setFont(MainInternalFrame.fonte5);
		rbDespesa.setBounds(52, 126, 119, 23);
		getContentPane().add(rbDespesa);

		rbDevem = new JRadioButton("Me devem");
		grupoTipo.add(rbDevem);
		rbDevem.setFont(MainInternalFrame.fonte5);
		rbDevem.setBounds(190, 100, 155, 23);
		getContentPane().add(rbDevem);

		rbDevo = new JRadioButton("Estou devendo");
		grupoTipo.add(rbDevo);
		rbDevo.setFont(MainInternalFrame.fonte5);
		rbDevo.setBounds(190, 126, 155, 23);
		getContentPane().add(rbDevo);

		JSeparator separator = new JSeparator();
		separator.setBackground(Color.BLUE);
		separator.setBounds(52, 168, 363, 9);
		getContentPane().add(separator);

		JLabel lblQuem = new JLabel("Quem:");
		lblQuem.setFont(MainInternalFrame.fonte5);
		lblQuem.setBounds(52, 303, 73, 26);
		getContentPane().add(lblQuem);

		cbQuem = new JComboBox<>();
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
		//Preenchimento do comboBox com valores cadastrados no banco
		try {
			cbClassificacao.setModel(new DefaultComboBoxModel(clDao.buscarNomesClassificacoes()));
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR004'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
		}
		cbClassificacao.setFont(MainInternalFrame.fonte5);
		cbClassificacao.setBounds(628, 66, 299, 27);
		getContentPane().add(cbClassificacao);
		
		cbInstituicao = new JComboBox<>();
		//Preenchimento do comboBox com valores cadastrados no banco
		try {
			cbInstituicao.setModel(new DefaultComboBoxModel(inDao.buscarNomesInstituicao()));
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR006'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
		}
		cbInstituicao.setToolTipText("Colocar os valores do lado das institui\u00E7\u00F5es");
		cbInstituicao.setBounds(644, 335, 283, 27);
		getContentPane().add(cbInstituicao);
		cbInstituicao.setFont(MainInternalFrame.fonte5);

		JTextArea txAviso1 = new JTextArea();
		txAviso1.setBackground(getBackground());
		txAviso1.setEnabled(false);
		txAviso1.setWrapStyleWord(true);
		txAviso1.setFont(MainInternalFrame.fonte6);
		txAviso1.setLineWrap(true);
		txAviso1.setText("N\u00E3o precisa colocar os valores negativos!");
		txAviso1.setBounds(64, 172, 320, 26);
		getContentPane().add(txAviso1);

		JLabel lbAvImg1 = new JLabel("Warning Img");
		lbAvImg1.setBounds(21, 168, 33, 26);
		getContentPane().add(lbAvImg1);

		JLabel lblDescrio = new JLabel("Descri\u00E7\u00E3o:");
		lblDescrio.setFont(MainInternalFrame.fonte5);
		lblDescrio.setBounds(480, 126, 95, 26);
		getContentPane().add(lblDescrio);

		JScrollPane spDescricao = new JScrollPane();
		spDescricao.setBounds(480, 161, 447, 138);
		getContentPane().add(spDescricao);

		txDescricao = new JTextArea();
		txDescricao.setText("oiii");
		txDescricao.setFont(MainInternalFrame.fonte5);
		spDescricao.setViewportView(txDescricao);
		txDescricao.setWrapStyleWord(true);
		txDescricao.setLineWrap(true);

		JLabel lblClassificao = new JLabel("Classifica\u00E7\u00E3o:");
		lblClassificao.setFont(MainInternalFrame.fonte5);
		lblClassificao.setBounds(480, 66, 138, 26);
		getContentPane().add(lblClassificao);

		JLabel lblLocal = new JLabel("Institui\u00E7\u00E3o/Local:");
		lblLocal.setFont(MainInternalFrame.fonte5);
		lblLocal.setBounds(480, 336, 155, 26);
		getContentPane().add(lblLocal);

		JButton btLimpar = new JButton("Limpar");
		btLimpar.setToolTipText("Bot\u00E3o com imagem de limpeza, maybe it wont be a botton");
		btLimpar.setFont(MainInternalFrame.fonte5);
		btLimpar.setBounds(579, 426, 56, 41);
		getContentPane().add(btLimpar);

		JTextArea txAviso2 = new JTextArea();
		txAviso2.setToolTipText("HIDDEN");
		txAviso2.setWrapStyleWord(true);
		txAviso2.setText("O registro ser\u00E1 calculado para X que ficar\u00E1 com R$ XX .");
		txAviso2.setLineWrap(true);
		txAviso2.setFont(MainInternalFrame.fonte6);
		txAviso2.setEnabled(false);
		txAviso2.setBackground(SystemColor.menu);
		txAviso2.setBounds(64, 381, 399, 19);
		getContentPane().add(txAviso2);

		JLabel lbAvImg2 = new JLabel("Warning Img");
		lbAvImg2.setBounds(21, 374, 33, 26);
		getContentPane().add(lbAvImg2);

		JLabel lbInf2 = new JLabel("Bot\u00E3o que mostra as informa\u00E7\u00F5es tooltip");
		lbInf2.setToolTipText("tooltip");
		lbInf2.setBounds(447, 67, 33, 26);
		getContentPane().add(lbInf2);

		JLabel lbInf1 = new JLabel("Bot\u00E3o que mostra as informa\u00E7\u00F5es tooltip /\\");
		lbInf1.setToolTipText("tooltip");
		lbInf1.setBounds(21, 307, 33, 26);
		getContentPane().add(lbInf1);

		dcData = new JDateChooser();
		//Pega o campo de edição do DateChooser para trabalhá-lo 
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dcData.getDateEditor();
		//Deixa máscara do editor visível e não permite o usuário digitar letras ou caracteres que não sejam números
		editor.setMaskVisible(true);
		dcData.setBounds(254, 229, 161, 26);
		dcData.setFont(new Font("Calibri", Font.PLAIN, 20));
		getContentPane().add(dcData);
		
		cbSub = new JComboBox<Object>();
		cbSub.setEnabled(false);
		cbSub.setToolTipText("Indispon\u00EDvel");
		cbSub.setFont(MainInternalFrame.fonte5);
		cbSub.setBounds(644, 373, 283, 27);
		getContentPane().add(cbSub);
		
		JLabel lbSub = new JLabel("Sub Local:");
		lbSub.setFont(MainInternalFrame.fonte5);
		lbSub.setBounds(480, 373, 155, 26);
		getContentPane().add(lbSub);
		
		//### - Se for selecionado preciso mudar o campo da data para apenas dia do vencimento e verificar informações que precisa ocultar
		JRadioButton rbConta = new JRadioButton("Conta Mensal");
		rbConta.setEnabled(false);
		rbConta.setToolTipText("Registra uma conta para lembretes mensais");
		rbConta.setFont(MainInternalFrame.fonte5);
		rbConta.setBounds(190, 68, 155, 23);
		getContentPane().add(rbConta);
				
		JButton btRegistrar = new JButton("Concluir Registro");
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
		btRegistrar.setBounds(346, 426, 229, 41);
		getContentPane().add(btRegistrar);
		
		tfCodigoEdicao = new JTextField("-1");
		tfCodigoEdicao.setEditable(false);
		tfCodigoEdicao.setHorizontalAlignment(SwingConstants.CENTER);
		tfCodigoEdicao.setToolTipText("Clique no bot\u00E3o de limpar, do lado direito, para deixar de editar esse registro");
		tfCodigoEdicao.setEnabled(false);
		tfCodigoEdicao.setForeground(Color.GRAY);
		tfCodigoEdicao.setFont(new Font("Calibri", Font.PLAIN, 20));
		tfCodigoEdicao.setColumns(10);
		tfCodigoEdicao.setBounds(289, 430, 56, 32);
		getContentPane().add(tfCodigoEdicao);
		
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