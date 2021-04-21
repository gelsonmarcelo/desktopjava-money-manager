package br.edu.ifc.videira.controllers.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import br.edu.ifc.videira.DAOs.ClassificacaoDao;
import br.edu.ifc.videira.DAOs.InstituicaoDao;
import br.edu.ifc.videira.DAOs.QuemDao;
import br.edu.ifc.videira.DAOs.RegistroDao;
import br.edu.ifc.videira.DAOs.UsuarioDao;
import br.edu.ifc.videira.beans.Registro;
import br.edu.ifc.videira.utils.ComboBoxModel;
import br.edu.ifc.videira.utils.JNumberFormatField;
import javax.swing.ImageIcon;

public class IFrVerRegistros extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfBusca;
	private JTable table;
	private List<Object> registros = new ArrayList<Object>();
	public JComboBox<Object> cbTipoRegistro;
	UsuarioDao usDao = new UsuarioDao();
	RegistroDao rgDao = new RegistroDao();
	QuemDao qmDao = new QuemDao();
	Registro rg = new Registro();
	ClassificacaoDao clDao = new ClassificacaoDao();
	InstituicaoDao inDao = new InstituicaoDao();
	protected JButton btExcluirRegistro;
	protected TableRowSorter<TableModel> filtro = null;
	protected JLabel lbAvisoSelecao;
	final SimpleDateFormat formatoAmericano = new SimpleDateFormat("yyyy-MM-dd");
	private JNumberFormatField tfSaldo;
	private JButton btEditarRegistro;
	// final SimpleDateFormat formatoBrasileiro = new
	// SimpleDateFormat("dd-MM-yyyy");

	public IFrVerRegistros() {
		super("Ver Registros # " + (++IFuLogin.openFrameCount) + "º", false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable

		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(1000, 650);

		// Set the window's location.
		setLocation(IFuLogin.xOffset * IFuLogin.openFrameCount, IFuLogin.yOffset * IFuLogin.openFrameCount);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Buscar:");
		lblNewLabel.setFont(MainInternalFrame.fonte4);
		lblNewLabel.setBounds(625, 60, 100, 34);
		getContentPane().add(lblNewLabel);

		tfBusca = new JTextField("digite o que procura");
		tfBusca.setForeground(Color.GRAY);
		tfBusca.setFont(new Font("Calibri", Font.PLAIN, 20));
		/**
		 * Evento para alterar a descrição temporária do campo quando ganha/perde foco
		 */
		tfBusca.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// Limpa placeHolder
				tfBusca.setText("");
				// Define a cor do texto
				tfBusca.setForeground(Color.BLACK);
				// Se há seleção na tabela e nesse evento o usuário vai iniciar a digitar
				if (table.getSelectedRow() != -1) {
					// Já avisa que precisa limpar a seleção para conseguir pesquisar
					lbAvisoSelecao.setVisible(true);
				}
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				tfBusca.setText("digite o que procura");
				tfBusca.setForeground(Color.GRAY);
				// Apaga o aviso quando sai do campo de digitação
				lbAvisoSelecao.setVisible(false);
			}
		});
		/**
		 * Evento que ativa quando o cursor de escrita do mouse troca de posição
		 */
		tfBusca.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				// Se o campo não está preenchido com a frase "digite o que procura" E não tem
				// linha selecionada, ele faz a atualização dos dados (se tiver linha
				// selecionada não quero que remova o filtro, pois o usuário perderá a seleção)
				if (!tfBusca.getText().equals("digite o que procura") && table.getSelectedRow() == -1) {
					// Atualizar a tabela apenas com valores correspondentes aos digitados no campo
					// de busca por codigo
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					filtro = new TableRowSorter<TableModel>(model);
					// Define o filtro para a tabela
					table.setRowSorter(filtro);
					// Se o campo está em branco
					if (tfBusca.getText().length() == 0) {
						// Remove as definições do filtro
						filtro.setRowFilter(null);
					} else {
						// Seleciona comparando as colunas 0,1,2... com o texto do campo. "(?i)"
						// significa que ignora se a letra é maiúscula ou minúscula
						filtro.setRowFilter(RowFilter.regexFilter("(?i)" + tfBusca.getText(), 0, 1, 2, 3, 4, 5, 6, 7));
					}
				}
			}
		});
		tfBusca.setBounds(724, 61, 197, 34);
		getContentPane().add(tfBusca);
		tfBusca.setColumns(10);

		JLabel lblDataEspecfica = new JLabel("De");
		lblDataEspecfica.setFont(MainInternalFrame.fonte4);
		lblDataEspecfica.setBounds(39, 66, 54, 34);
		getContentPane().add(lblDataEspecfica);

		JDateChooser dcInicio = new JDateChooser();
		// Pega campo de digitação para validar
		JTextFieldDateEditor dcInicioEditor = (JTextFieldDateEditor) dcInicio.getDateEditor();
		// Deixa máscara do campo visível
		dcInicioEditor.setMaskVisible(true);
		dcInicio.setBounds(76, 60, 128, 34);
		dcInicio.setFont(new Font("Calibri", Font.PLAIN, 20));
		getContentPane().add(dcInicio);

		JDateChooser dcFim = new JDateChooser();
		// Pega campo de digitação para validar
		JTextFieldDateEditor dcFimEditor = (JTextFieldDateEditor) dcFim.getDateEditor();
		// Deixa máscara do campo visível
		dcFimEditor.setMaskVisible(true);
		dcFim.setFont(new Font("Calibri", Font.PLAIN, 20));
		dcFim.setBounds(254, 60, 128, 32);
		getContentPane().add(dcFim);

		JLabel lblFim = new JLabel("at\u00E9");
		lblFim.setFont(MainInternalFrame.fonte4);
		lblFim.setBounds(214, 65, 54, 34);
		getContentPane().add(lblFim);

		cbTipoRegistro = new JComboBox<>();
		// Preenchimento do comboBox com valores cadastrados no banco
		try {
			cbTipoRegistro.setModel(new ComboBoxModel(qmDao.buscarNomesQuem(true)));
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR013'!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
		}
		cbTipoRegistro.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				// Verifica se ocorreram mudanças no item selecionado dentro do Combobox
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// Pegando o texto do item selecionado
					atualizarTabela(e.getItem().toString(), null);
					// Limpa campos caso estejam preenchidos, pois agora não correspondem mais aos
					// resultados
					dcInicio.setDate(null);
					dcFim.setDate(null);
					// Alterna habilitaçaõ do botão de editar registro, pois em caso de ser apenas
					// dívida não pode editar
					if (cbTipoRegistro.getSelectedIndex() > 3) {
						btEditarRegistro.setEnabled(false);
						btEditarRegistro.setToolTipText("No momento não é possível editar registros em dívidas.");
					} else {
						btEditarRegistro.setEnabled(true);
						btEditarRegistro.setToolTipText("");
					}
				}
			}
		});
		cbTipoRegistro.setToolTipText("Selecione o tipo de registro que deseja listar");
		cbTipoRegistro.setFont(new Font("Calibri Light", Font.PLAIN, 20));
		cbTipoRegistro.setBounds(584, 11, 337, 32);
		getContentPane().add(cbTipoRegistro);

		JScrollPane spRegistros = new JScrollPane();
		spRegistros.setBounds(10, 124, 964, 433);
		getContentPane().add(spRegistros);

		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(MainInternalFrame.fonteTabela);
		table.setAutoCreateRowSorter(true);
		spRegistros.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"C\u00F3digo", "Registro", "Valor", "Quem", "Descri\u00E7\u00E3o", "Data", "Institui\u00E7\u00E3o", "Classifica\u00E7\u00E3o"
			}
		)
		);
		table.getColumnModel().getColumn(0).setPreferredWidth(15);
		table.getColumnModel().getColumn(1).setPreferredWidth(50);
		table.getColumnModel().getColumn(2).setPreferredWidth(25);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		table.getColumnModel().getColumn(5).setPreferredWidth(25);
		// Impede movimentação das colunas pelo usuário
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(20);

		btExcluirRegistro = new JButton("Excluir");
		btExcluirRegistro.setIcon(new ImageIcon(IFrVerRegistros.class.getResource("/br/edu/ifc/videira/imgs/lixeira.png")));
		btExcluirRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int voltarValor;
				// Se o retorno do método getSelectedRow tem valor diferente de -1, significa
				// que tem linha selecionada
				if (table.getSelectedRow() != -1) {
					if (String.valueOf(table.getValueAt(table.getSelectedRow(), 1)).equals("Receita")
							|| String.valueOf(table.getValueAt(table.getSelectedRow(), 1)).equals("Despesa")) {
						voltarValor = JOptionPane.showConfirmDialog(null,
								"Deseja reverter o valor desse registro para a instituição de origem/destino?",
								"Reverter valor", JOptionPane.YES_NO_CANCEL_OPTION);
					} else {
						// Nem mostra a confirmDialog, considera que o usuário nao quer voltar valores
						voltarValor = 1;
					}
					// Se voltarValor é diferente de 2 significa que o usuário não cancelou a
					// operação e faz a autenticação do usuário
					if (voltarValor != 2 && UsuarioDao.validar(true, false, null, null)) {
						// Definindo o código da linha selecionada
						rg.setCodigo(Integer.parseInt(String.valueOf(table.getValueAt(table.getSelectedRow(), 0))));
						// Enviando para o método responsável na classe RegistroDao
						rgDao.deletarRegistro(rg, voltarValor,
								String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
						// Limpar dados pesquisados
						atualizarTabela(String.valueOf(cbTipoRegistro.getSelectedItem()), null);
						if (filtro != null) {
							filtro.setRowFilter(null);
						}
					} else if (voltarValor == 2) {
						// Nesse caso o usuário pressionou o botão de cancelar
						JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.", "Aviso",
								JOptionPane.WARNING_MESSAGE);
					}

				} else {
					JOptionPane.showMessageDialog(null, "É necessário selecionar uma linha da tabela para excluir.",
							"Não há seleção", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btExcluirRegistro.setToolTipText("Eliminar o registro");
		btExcluirRegistro.setFont(MainInternalFrame.fonteBotoes);
		btExcluirRegistro.setBounds(552, 568, 173, 41);
		getContentPane().add(btExcluirRegistro);

		btEditarRegistro = new JButton("Editar");
		btEditarRegistro.setIcon(new ImageIcon(IFrVerRegistros.class.getResource("/br/edu/ifc/videira/imgs/editar.png")));
		btEditarRegistro.setToolTipText("Alterar o registro");
		btEditarRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Se o retorno do método getSelectedRow tem valor diferente de -1, significa
				// que tem linha selecionada
				if (table.getSelectedRow() != -1) {
					// Instanciar frame
					IFrRegistrar frame = new IFrRegistrar();
					// Deixar frame visível
					frame.setVisible(true);
					// Inserir frame dentro de "desktop" que é a tela principal
					MainInternalFrame.desktop.add(frame);
					try {
						// Selecionar esse frame, dar o foco
						frame.setSelected(true);
						/**
						 * Pré definir valores a partir do registro que se está editando 0-Código
						 * 1-Registro 2-Valor 3-Quem 4-Descricao 5-Data 6-Instituicao 7-Classificacao
						 */
						// Escolhe qual tipo de registro a pessoa esta editando para marcar no checkbox
						switch (String.valueOf(table.getValueAt(table.getSelectedRow(), 1))) {
						case "Receita":
							frame.rbReceita.setSelected(true);
							break;
						case "Despesa":
							frame.rbDespesa.setSelected(true);
							break;
						case "Estou devendo":
							frame.rbDevo.setSelected(true);
							break;
						case "Está me devendo":
							frame.rbDevem.setSelected(true);
							break;
						default:
							JOptionPane.showMessageDialog(null,
									"Ocorreu um erro, não foi possível definir qual o tipo de registro, contate o desenvolvedor e informe o código 'VR012'!",
									"Erro inesperado", JOptionPane.ERROR_MESSAGE);
							break;
						}
						// Desabilita edição dos campos para o usuário não achar que pode alterar essa
						// informação
						frame.rbReceita.setEnabled(false);
						frame.rbDespesa.setEnabled(false);
						frame.rbDevem.setEnabled(false);
						frame.rbDevo.setEnabled(false);

						// Define o código do registro que se está editando para que eu consiga fazer o
						// controle se o usuário ainda está usando a tela para editar ou quer fazer um
						// registro novo (clicando no botão de limpar)
						int idRegistro = Integer.parseInt(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
						frame.tfCodigoEdicao.setText(String.valueOf(idRegistro));
						// Define o valor a partir da tabela, no campo de valor do registro
						frame.tfValor.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
						frame.valorOriginalRegistro = String.valueOf(table.getValueAt(table.getSelectedRow(), 2));
						// Converte data da linha da tabela para formato compatível com o Date Chooser e
						// define
						java.util.Date formatoBrasileiro = new SimpleDateFormat("dd-MM-yyy")
								.parse(String.valueOf(table.getValueAt(table.getSelectedRow(), 5)));
						frame.dcData.setDate(formatoBrasileiro);
						// Chama método que busca "quem" a partir do id do registro selecionado e monta
						// String como no comboBox para pré-selecioná-la.
						frame.cbQuem.setSelectedItem(qmDao.RecuperarQuem(idRegistro));
						// Chama método que busca "classificacao" e monta String como no comboBox para
						// pré-selecioná-la.
						frame.cbClassificacao.setSelectedItem(clDao.recuperarClassificacao(idRegistro));
						// Define a descrição na tela de registro
						frame.txDescricao.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 4)));
						// Chama método que busca "instituicao" e monta String como no comboBox para
						// pré-selecioná-la.
						frame.cbInstituicao.setSelectedItem(inDao.RecuperarInstituicao(idRegistro));
					} catch (java.beans.PropertyVetoException e) {
						e.printStackTrace();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"Ocorreu um erro, não foi possível completar os campos para edição do registro, contate o desenvolvedor e informe o código 'VR011'!",
								"Erro inesperado", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "É necessário selecionar uma linha da tabela para editar.",
							"Não há seleção", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btEditarRegistro.setFont(MainInternalFrame.fonteBotoes);
		btEditarRegistro.setBounds(296, 568, 173, 41);
		getContentPane().add(btEditarRegistro);

		JLabel lbTitulo = new JLabel("Ver Registros         ");
		lbTitulo.setFont(MainInternalFrame.fonte1);
		lbTitulo.setBounds(0, 0, 984, 55);
		lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitulo);

		JButton btBuscaPeriodo = new JButton("");
		btBuscaPeriodo.setIcon(new ImageIcon(IFrVerRegistros.class.getResource("/br/edu/ifc/videira/imgs/procurar-data.png")));
		btBuscaPeriodo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dcInicio.getDate() != null && dcFim.getDate() != null) {
					SimpleDateFormat formatoBrasileiro = new SimpleDateFormat("dd/MM/yyyy");
					// Validando se a data de início é anterior ou igual à data final informada
					try {
						if (formatoBrasileiro.parse(dcInicioEditor.getText())
								.before(formatoBrasileiro.parse(dcFimEditor.getText()))
								|| formatoBrasileiro.parse(dcInicioEditor.getText())
										.equals(formatoBrasileiro.parse(dcFimEditor.getText()))) {
							// Caso positivo a pesquisa pode ir adiante, definindo valores na classe
							// atributo
							rg.setDataInicio(dcInicio.getDate());
							rg.setDataFim(dcFim.getDate());
							// Chamando método que conecta com o banco e atualiza a tabela passando
							// parametros de 'tipoRegistro' e Objeto rg
							atualizarTabela(String.valueOf(cbTipoRegistro.getSelectedItem()), rg);
						} else {
							JOptionPane.showMessageDialog(null,
									"A data de início deve ser anterior à data final da consulta!",
									"Data inserida inválida", JOptionPane.WARNING_MESSAGE);
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"Ocorreu um erro, não foi possível validar as datas, contate o desenvolvedor e informe o código 'VR010'!",
								"Erro inesperado", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Favor corrigir as datas informadas!",
							"Data vazia/inválida", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btBuscaPeriodo.setToolTipText("Buscar registros no per\u00EDodo selecionado");
		btBuscaPeriodo.setFont(new Font("IrisUPC", Font.PLAIN, 20));
		btBuscaPeriodo.setBounds(401, 60, 42, 32);
		getContentPane().add(btBuscaPeriodo);

		JButton btLimparFiltro = new JButton("");
		btLimparFiltro.setIcon(new ImageIcon(IFrVerRegistros.class.getResource("/br/edu/ifc/videira/imgs/apagador.png")));
		btLimparFiltro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Preciso dessa ação para forçar a desceleção da linha na tabela, pois se
				// estiver selecionada o campo perde o foco mas não limpa a pesquisa
				atualizarTabela(String.valueOf(cbTipoRegistro.getSelectedItem()), null);
				dcInicioEditor.setText(null);
				dcFimEditor.setText(null);
				// Validar se o filtro já for nulo não precisa definir nulo novamente (retorna
				// erro se faze-lo)
				if (filtro != null) {
					filtro.setRowFilter(null);
				}
			}
		});
		btLimparFiltro.setToolTipText("Limpar sele\u00E7\u00E3o da tabela");
		btLimparFiltro.setFont(MainInternalFrame.fonteBotoes);
		btLimparFiltro.setBounds(932, 60, 42, 32);
		getContentPane().add(btLimparFiltro);

		lbAvisoSelecao = new JLabel("Limpe a sele\u00E7\u00E3o para pesquisar novamente!");
		lbAvisoSelecao.setVisible(false);
		lbAvisoSelecao.setForeground(Color.RED);
		lbAvisoSelecao.setFont(MainInternalFrame.fonte6);
		lbAvisoSelecao.setBounds(635, 99, 339, 27);
		getContentPane().add(lbAvisoSelecao);

		JLabel lbSaldo = new JLabel("Saldo:");
		lbSaldo.setFont(MainInternalFrame.fonte5);
		lbSaldo.setBounds(10, 568, 54, 32);
		getContentPane().add(lbSaldo);

		tfSaldo = new JNumberFormatField();
		tfSaldo.setBackground(Color.WHITE);
		tfSaldo.setEditable(false);
		tfSaldo.setFont(MainInternalFrame.FonteJNumberFormatField);
		tfSaldo.setColumns(10);
		tfSaldo.setBounds(68, 571, 173, 27);
		getContentPane().add(tfSaldo);

		// Insere os valores na tabela chamando o método que preenche e, passando por
		// default o parâmetro 'Todos', para buscar as despesas e receitas do usuário
		atualizarTabela("Todos", null);
	}

	/**
	 * Pega informações do banco e atualiza a tabela
	 * 
	 * @param tipoRegistro:
	 *            tipo de registro selecionado pelo usuário
	 * @param rg:
	 *            objeto do tipo Registro
	 * @param pesquisaIntervaloDatas:
	 *            se a consulta será para retornar valores levando em consideração o
	 *            intervalo de datas passado no objeto
	 */
	public void atualizarTabela(String tipoRegistro, Registro rg) {
		try {
			// Verifica se a atualização da tabela será puxando intervalos de data
			// específicos ou não (acionamento a partir do botão)
			if (rg == null) {
				// Traz os dados executando o método buscarRegistros na classe DAO e atribui na
				// variável registros
				registros = rgDao.buscarRegistros(tipoRegistro);
			} else {
				// Traz os dados executando o método buscarRegistrosPeriodo na classe DAO e
				// atribui na variável registros
				registros = rgDao.buscarRegistrosPeriodo(tipoRegistro, rg.getDataInicio(), rg.getDataFim());
			}
			// Define o tamanho da lista de registros (quantas linhas), coloquei em variável
			// antes do for pra não precisar chamar um método size() pra descobrir o tamanho
			// da lista toda vez que passar pelo for.
			int qntLinhas = registros.size();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int i = 0; i != qntLinhas; i++) {
				// Vai incluindo linhas conforme passa pelos objetos na lista
				model.addRow((Object[]) registros.get(i));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, não foi possível preencher a tabela adequadamente, contate o desenvolvedor e informe o código 'VR009'!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		// Colocar o saldo no textfield, se o index for maior que 3 já serão pessoas e
		// terão saldo vinculado no nome
		if (cbTipoRegistro.getSelectedIndex() > 3) {
			tfSaldo.setText(String.valueOf(tipoRegistro.split("\\*")[2]));
		}
	}
}