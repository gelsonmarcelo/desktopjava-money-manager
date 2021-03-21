package br.edu.ifc.videira.controllers.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import br.edu.ifc.videira.DAOs.InstituicaoDao;
import br.edu.ifc.videira.DAOs.UsuarioDao;
import br.edu.ifc.videira.beans.Instituicao;
import br.edu.ifc.videira.utils.JNumberFormatField;

public class IFiEditarInstituicao extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfInstituicao;
	private JTable table;
	private JTable tableSubs;
	private List<Object> registros = new ArrayList<Object>();
	private JComboBox<Object> cbTipo;
	InstituicaoDao inDao = new InstituicaoDao();
	Instituicao in = new Instituicao();
	private JTextField tfCod;
	private JTextField tfSubs;
	private JTextField tfSaldo;
	private JButton btAtualizar;
	private JButton btExcluir;
	private JButton btCadastrar;
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IFiEditarInstituicao() {
		super("Editar classificação # " + (++IFuLogin.openFrameCount) + "º", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable

		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(565, 590);

		// Set the window's location.
		setLocation(IFuLogin.xOffset * IFuLogin.openFrameCount, IFuLogin.yOffset * IFuLogin.openFrameCount);
		getContentPane().setLayout(null);

		JLabel lbTitle = new JLabel("Editar institui\u00E7\u00E3o");
		lbTitle.setFont(MainInternalFrame.fonte1);
		lbTitle.setBounds(0, 0, 549, 55);
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitle);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setFont(MainInternalFrame.fonte4);
		lblNome.setBounds(23, 231, 135, 34);
		getContentPane().add(lblNome);

		tfInstituicao = new JTextField();
		tfInstituicao.setFont(MainInternalFrame.fonte4);
		tfInstituicao.setBounds(158, 232, 376, 34);
		getContentPane().add(tfInstituicao);
		tfInstituicao.setColumns(10);
		
		btCadastrar = new JButton("Cadastrar");
		btCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Validação para campo em branco
				if(!tfInstituicao.getText().equals("") && !(cbTipo.getSelectedIndex()==0)) {
					in.setNome(tfInstituicao.getText());
					in.setIdTipo(Integer.parseInt(String.valueOf(cbTipo.getSelectedItem()).split("\\*")[0]));
					in.setSaldo(Double.parseDouble(tfSaldo.getText().replaceAll("\\.", "").replaceAll(",", ".").replace("R$ ", "")));
					try {
						inDao.cadastrarInstituicao(in);
						JOptionPane.showMessageDialog(null, "Instituição cadastrada com sucesso!");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Ocorreu um erro, o cadastro da instituição não foi executado corretamente, contate o desenvolvedor e informe o código ''!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
					}
					atualizarTabela();
					limpar();
					
				}else {
					JOptionPane.showMessageDialog(null, "Informe pelo menos o nome e tipo da instituição para cadastrar!", "Campo obrigatório em branco", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btCadastrar.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btCadastrar.setFont(MainInternalFrame.fonte4);
		btCadastrar.setBounds(23, 501, 144, 32);
		getContentPane().add(btCadastrar);
		
		btAtualizar = new JButton("Atualizar");
		btAtualizar.setEnabled(false);
		btAtualizar.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btAtualizar.setFont(MainInternalFrame.fonte4);
		btAtualizar.setBounds(209, 501, 144, 32);
		getContentPane().add(btAtualizar);
		
		btExcluir = new JButton("Excluir");
		btExcluir.setEnabled(false);
		btExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Se o retorno do método getSelectedRow tem valor diferente de -1, significa
				// que tem linha selecionada
				if (table.getSelectedRow() != -1) {
					
					// Se voltarValor é diferente de 2 significa que o usuário não cancelou a
					// operação e faz a autenticação do usuário
					if (UsuarioDao.validar(true, false, null, null)) {
						// Definindo o código da linha selecionada
						in.setCodigo(Integer.parseInt(String.valueOf(table.getValueAt(table.getSelectedRow(), 0))));
						// Enviando para o método responsável na classe RegistroDao
						inDao.deletarInstituicao(in);
						
						limpar();
						atualizarTabela();
						
					} else {
						// Nesse caso a validação falhou
						JOptionPane.showMessageDialog(null, "Operação cancelada", "Aviso",
								JOptionPane.WARNING_MESSAGE);
					}

				} else {
					JOptionPane.showMessageDialog(null, "É necessário selecionar uma linha da tabela para excluir.",
							"Não há seleção", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btExcluir.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btExcluir.setFont(MainInternalFrame.fonte4);
		btExcluir.setBounds(390, 501, 144, 32);
		getContentPane().add(btExcluir);
		
		tfCod = new JTextField();
		tfCod.setEnabled(false);
		tfCod.setFont(MainInternalFrame.fonte4);
		tfCod.setColumns(10);
		tfCod.setBounds(23, 447, 31, 28);
		getContentPane().add(tfCod);
		
		JLabel lbSubGrupos = new JLabel("Subs:");
		lbSubGrupos.setEnabled(false);
		lbSubGrupos.setFont(MainInternalFrame.fonte4);
		lbSubGrupos.setBounds(23, 375, 135, 34);
		getContentPane().add(lbSubGrupos);
		
		tfSubs = new JTextField();
		tfSubs.setToolTipText("indispon\u00EDvel no momento.");
		tfSubs.setEnabled(false);
		tfSubs.setFont(MainInternalFrame.fonte4);
		tfSubs.setColumns(10);
		tfSubs.setBounds(158, 375, 376, 34);
		getContentPane().add(tfSubs);
		
		JButton btAdicionar = new JButton("Add");
		btAdicionar.setEnabled(false);
		btAdicionar.setToolTipText("Indispon\u00EDvel");
		btAdicionar.setFont(MainInternalFrame.fonte4);
		btAdicionar.setBounds(79, 443, 75, 32);
		getContentPane().add(btAdicionar);
		
		JScrollPane spInstituicao = new JScrollPane();
		spInstituicao.setBounds(23, 66, 511, 154);
		getContentPane().add(spInstituicao);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				tfCod.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
				tfInstituicao.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
				tfSaldo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
				if(tfSaldo.getText().equals("R$ 0,00")) {
					btExcluir.setEnabled(true);
					btExcluir.setToolTipText("");
				}else {
					btExcluir.setEnabled(false);
					btExcluir.setToolTipText("É necessário que a instituição esteja com saldo zerado para excluir!");
				}
				
				try {
					cbTipo.setSelectedItem(inDao.RecuperarTipoInstituicao(String.valueOf(table.getValueAt(table.getSelectedRow(), 3))));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				btAtualizar.setEnabled(true);
				btCadastrar.setEnabled(false);
				btCadastrar.setToolTipText("Limpe a seleção para reativar");
			}
		});
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 0), Color.BLACK, Color.DARK_GRAY, Color.DARK_GRAY));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(MainInternalFrame.fonteTabela);
		table.setAutoCreateRowSorter(true);
		spInstituicao.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Codigo", "Nome", "Saldo", "Tipo"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(250);
		table.getColumnModel().getColumn(2).setPreferredWidth(25);
		table.getColumnModel().getColumn(3).setPreferredWidth(25);
		//Impede movimentação das colunas pelo usuário
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(20);
		
		JScrollPane spSub = new JScrollPane();
		spSub.setEnabled(false);
		spSub.setBounds(158, 420, 376, 55);
		getContentPane().add(spSub);

		tableSubs = new JTable();
		tableSubs.setEnabled(false);
		tableSubs.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 0), Color.BLACK, Color.DARK_GRAY, Color.DARK_GRAY));
		tableSubs.setFillsViewportHeight(true);
		tableSubs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSubs.setFont(MainInternalFrame.fonteTabela);
		tableSubs.setAutoCreateRowSorter(true);
		spSub.setViewportView(tableSubs);
		tableSubs.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"C\u00F3digo", "Nome", "Saldo"
			}
		));
		tableSubs.getColumnModel().getColumn(0).setPreferredWidth(10);
		tableSubs.getColumnModel().getColumn(1).setPreferredWidth(200);
		tableSubs.getColumnModel().getColumn(2).setPreferredWidth(25);
		//Impede movimentação das colunas pelo usuário
		tableSubs.getTableHeader().setReorderingAllowed(false);
		tableSubs.setRowHeight(20);
		
		tfSaldo = new JNumberFormatField();
		tfSaldo.setFont(new Font("Calibri", Font.PLAIN, 20));
		tfSaldo.setColumns(10);
		tfSaldo.setBounds(158, 330, 195, 34);
		getContentPane().add(tfSaldo);
		
		JLabel lblSaldo = new JLabel("Saldo R$:");
		lblSaldo.setFont(new Font("Sitka Subheading", Font.PLAIN, 25));
		lblSaldo.setBounds(23, 328, 135, 34);
		getContentPane().add(lblSaldo);
		
		JButton btLimpar = new JButton("Limpar");
		btLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpar();
			}
		});
		btLimpar.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btLimpar.setFont(new Font("Sitka Subheading", Font.PLAIN, 25));
		btLimpar.setBounds(390, 332, 144, 32);
		getContentPane().add(btLimpar);
				
		cbTipo = new JComboBox<>();
		//Preenchimento do comboBox com valores cadastrados no banco
		try {
			cbTipo.setModel(new DefaultComboBoxModel(inDao.buscarTipos()));
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código ''!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
		}
		cbTipo.setFont(MainInternalFrame.fonte5);
		cbTipo.setBounds(158, 281, 376, 34);
		getContentPane().add(cbTipo);
		
		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setFont(new Font("Sitka Subheading", Font.PLAIN, 25));
		lblTipo.setBounds(23, 281, 135, 34);
		getContentPane().add(lblTipo);
		
		
		atualizarTabela();

	}
	/**
	 * Pega informações do banco e atualiza a tabela
	 */
	public void atualizarTabela() {
		try {
			registros = inDao.buscarTodos();
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
	 * Limpar campos
	 */
	public void limpar() {
		tfSaldo.setText("");
		tfInstituicao.setText("");
		tfCod.setText("");
		cbTipo.setSelectedIndex(0);
		
		btCadastrar.setEnabled(true);
		btCadastrar.setToolTipText("");
		
		btAtualizar.setEnabled(false);
		btAtualizar.setToolTipText("Selecione uma linha da tabela para habilitar esse botão");
		
		btExcluir.setEnabled(false);
		btExcluir.setToolTipText("É necessário selecionar uma linha da tabela para excluir");
		
		table.clearSelection();
	}
}