package br.edu.ifc.videira.controllers.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
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

import br.edu.ifc.videira.DAOs.LogDao;
import br.edu.ifc.videira.beans.Log;

public class IFlLogs extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfBusca;
	private JTable table;
	private List<Object> logs = new ArrayList<Object>();
	LogDao lgDao = new LogDao();
	Log lg = new Log();
	
	protected TableRowSorter<TableModel> filtro = null;
	protected JLabel lbAvisoSelecao;

	public IFlLogs() {
		super("Ver Logs de Operações # " + (++IFuLogin.openFrameCount) + "º", false, // resizable
				true, 
				false, 
				true);
		setSize(1000, 650);

		setLocation(IFuLogin.xOffset * IFuLogin.openFrameCount, IFuLogin.yOffset * IFuLogin.openFrameCount);
		getContentPane().setLayout(null);

		JLabel lbBuscar = new JLabel("Buscar:");
		lbBuscar.setFont(MainInternalFrame.fonte4);
		lbBuscar.setBounds(625, 65, 100, 34);
		getContentPane().add(lbBuscar);

		tfBusca = new JTextField("digite o que procura");
		tfBusca.setForeground(Color.GRAY);
		tfBusca.setFont(new Font("Calibri", Font.PLAIN, 20));
		/**
		 * Evento para alterar a descrição temporária do campo quando ganha/perde foco
		 */
		tfBusca.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfBusca.setText("");
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
		tfBusca.setBounds(724, 66, 197, 34);
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
		dcInicio.setBounds(76, 65, 128, 27);
		dcInicio.setFont(new Font("Calibri", Font.PLAIN, 20));
		getContentPane().add(dcInicio);

		JDateChooser dcFim = new JDateChooser();
		// Pega campo de digitação para validar
		JTextFieldDateEditor dcFimEditor = (JTextFieldDateEditor) dcFim.getDateEditor();
		// Deixa máscara do campo visível
		dcFimEditor.setMaskVisible(true);
		dcFim.setFont(new Font("Calibri", Font.PLAIN, 20));
		dcFim.setBounds(254, 65, 128, 27);
		getContentPane().add(dcFim);

		JLabel lblFim = new JLabel("at\u00E9");
		lblFim.setFont(MainInternalFrame.fonte4);
		lblFim.setBounds(214, 65, 54, 34);
		getContentPane().add(lblFim);
		// Preenchimento do comboBox com valores cadastrados no banco
		try {
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código ''!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
		}

		JScrollPane spRegistros = new JScrollPane();
		spRegistros.setBounds(10, 124, 964, 485);
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
				"C\u00F3digo", "Opera\u00E7\u00E3o", "Momento", "Descri\u00E7\u00E3o", "Usu\u00E1rio"
			}
		) 
		);
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(70);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(500);
		table.getColumnModel().getColumn(4).setPreferredWidth(20);
		
		// Impede movimentação das colunas pelo usuário
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(20);

		JLabel lbTitulo = new JLabel("Log de Opera\u00E7\u00F5es");
		lbTitulo.setFont(MainInternalFrame.fonte1);
		lbTitulo.setBounds(0, 0, 984, 55);
		lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitulo);

		JButton btBuscaPeriodo = new JButton("Lupa");
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
							lg.setDataInicio(dcInicio.getDate());
							lg.setDataFim(dcFim.getDate());
							// Chamando método que conecta com o banco e atualiza a tabela passando
							// parametros de 'tipoRegistro' e Objeto rg
							atualizarTabela(lg);
						} else {
							JOptionPane.showMessageDialog(null,
									"A data de início deve ser anterior à data final da consulta!",
									"Data inserida inválida", JOptionPane.WARNING_MESSAGE);
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"Ocorreu um erro, não foi possível validar as datas, contate o desenvolvedor e informe o código ''!",
								"Erro inesperado", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Favor corrigir as datas informadas!",
							"Data vazia/inválida", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btBuscaPeriodo.setToolTipText("Inserir \u00EDcone de lupa");
		btBuscaPeriodo.setFont(new Font("IrisUPC", Font.PLAIN, 20));
		btBuscaPeriodo.setBounds(401, 60, 60, 32);
		getContentPane().add(btBuscaPeriodo);

		JButton btLimparFiltro = new JButton("Apagador");
		btLimparFiltro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Preciso dessa ação para forçar a desceleção da linha na tabela, pois se
				// estiver selecionada o campo perde o foco mas não limpa a pesquisa
				atualizarTabela(null);
				dcInicioEditor.setText(null);
				dcFimEditor.setText(null);
				// Validar se o filtro já for nulo não precisa definir nulo novamente (retorna
				// erro se faze-lo)
				if (filtro != null) {
					filtro.setRowFilter(null);
				}
			}
		});
		btLimparFiltro.setToolTipText("Inserir \u00EDcone de Apagador");
		btLimparFiltro.setFont(new Font("IrisUPC", Font.PLAIN, 20));
		btLimparFiltro.setBounds(932, 68, 42, 32);
		getContentPane().add(btLimparFiltro);

		lbAvisoSelecao = new JLabel("Limpe a sele\u00E7\u00E3o para pesquisar novamente!");
		lbAvisoSelecao.setVisible(false);
		lbAvisoSelecao.setForeground(Color.RED);
		lbAvisoSelecao.setFont(MainInternalFrame.fonte6);
		lbAvisoSelecao.setBounds(635, 99, 339, 27);
		getContentPane().add(lbAvisoSelecao);

		atualizarTabela(null);
	}

	/**
	 * Pega informações do banco e atualiza a tabela
	 * 
	 * @param lg:
	 *            objeto do tipo Log, se não for nulo contém datas
	 */
	public void atualizarTabela(Log lg) {
		try {
			// Verifica se a atualização da tabela será puxando intervalos de data
			// específicos ou não (acionamento a partir do botão)
			if (lg == null) {
				// Traz os dados executando o método buscarRegistros na classe DAO e atribui na
				// variável logs
				logs = lgDao.buscarRegistros();
			} else {
				// Traz os dados executando o método buscarRegistrosPeriodo na classe DAO e
				// atribui na variável logs
				logs = lgDao.buscarRegistrosPeriodo(lg.getDataInicio(), lg.getDataFim());
			}
			// Define o tamanho da lista de registros (quantas linhas), coloquei em variável
			// antes do for pra não precisar chamar um método size() pra descobrir o tamanho
			// da lista toda vez que passar pelo for.
			int qntLinhas = logs.size();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int i = 0; i != qntLinhas; i++) {
				// Vai incluindo linhas conforme passa pelos objetos na lista
				model.addRow((Object[]) logs.get(i));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, não foi possível preencher a tabela adequadamente, contate o desenvolvedor e informe o código ''!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}