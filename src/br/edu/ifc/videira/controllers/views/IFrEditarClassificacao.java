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
import javax.swing.table.DefaultTableModel;

import br.edu.ifc.videira.DAOs.ClassificacaoDao;
import br.edu.ifc.videira.DAOs.UsuarioDao;
import br.edu.ifc.videira.beans.Classificacao;
import java.awt.Font;

public class IFrEditarClassificacao extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfClassificacao;
	private JTable table;
	private List<Object> registros = new ArrayList<Object>();
	ClassificacaoDao cfDao = new ClassificacaoDao();
	Classificacao cf = new Classificacao();
	private JTextField tfCod;
	private JButton btAtualizar;
	private JButton btCadastrar;
	private JButton btExcluir;
	
	
	public IFrEditarClassificacao() {
		super("Editar classificação # " + (++IFuLogin.openFrameCount) + "º", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable

		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(567, 634);

		// Set the window's location.
		setLocation(IFuLogin.xOffset * IFuLogin.openFrameCount, IFuLogin.yOffset * IFuLogin.openFrameCount);
		getContentPane().setLayout(null);

		JLabel lbTitle = new JLabel("Editar classifica\u00E7\u00E3o");
		lbTitle.setFont(MainInternalFrame.fonte1);
		lbTitle.setBounds(0, 0, 549, 55);
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitle);

		JLabel lblClassificacao = new JLabel("Classifica\u00E7\u00E3o:");
		lblClassificacao.setFont(MainInternalFrame.fonte4);
		lblClassificacao.setBounds(23, 473, 164, 34);
		getContentPane().add(lblClassificacao);

		tfClassificacao = new JTextField();
		tfClassificacao.setFont(MainInternalFrame.fonte4);
		tfClassificacao.setBounds(197, 474, 337, 34);
		getContentPane().add(tfClassificacao);
		tfClassificacao.setColumns(10);
		
		JScrollPane spClassificacao = new JScrollPane();
		spClassificacao.setBounds(23, 66, 511, 396);
		getContentPane().add(spClassificacao);
		
		btCadastrar = new JButton("Cadastrar");
		btCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Validação para campo em branco
				if(!tfClassificacao.getText().isEmpty()) {
					cf.setNome(tfClassificacao.getText());
					try {
						cfDao.cadastrarClassificacao(cf);
						JOptionPane.showMessageDialog(null, "Classificação cadastrada com sucesso!");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Ocorreu um erro, o cadastro da classificação não foi executado corretamente, contate o desenvolvedor e informe o código 'CL002'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
					}
					atualizarTabela();
					limpar();
				}else {
					JOptionPane.showMessageDialog(null, "Informe o nome da classificação!", "Campo obrigatório em branco", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btCadastrar.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btCadastrar.setFont(MainInternalFrame.fonte4);
		btCadastrar.setBounds(23, 562, 144, 32);
		getContentPane().add(btCadastrar);
		
		btAtualizar = new JButton("Atualizar");
		btAtualizar.setEnabled(false);
		btAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!tfClassificacao.getText().equals("")) {
					if(JOptionPane.showConfirmDialog(null, "Se você editar uma classificação, todos os registros que estão com sua referência serão atualizados, deseja continuar?", "Confirmar a ação", JOptionPane.YES_NO_OPTION) == 0) {
						cf.setCodigo(Integer.parseInt(tfCod.getText()));
						cf.setNome(tfClassificacao.getText());
						try {
							cfDao.atualizarClassificacao(cf);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Ocorreu um erro, a atualização da classificação não foi executada corretamente, contate o desenvolvedor e informe o código 'CL003'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
						atualizarTabela();
						limpar();
					}else {
						JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário");	
					}
				}else {
					JOptionPane.showMessageDialog(null, "Informe o nome da classificação!", "Campo obrigatório em branco", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btAtualizar.setToolTipText("Necessita autentica\u00E7\u00E3o"); //### - Inserir autenticação
		btAtualizar.setFont(MainInternalFrame.fonte4);
		btAtualizar.setBounds(209, 562, 144, 32);
		getContentPane().add(btAtualizar);
		
		JButton btLimpar = new JButton("Limpar");
		btLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpar();
			}
		});
		btLimpar.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btLimpar.setFont(MainInternalFrame.fonte4);
		btLimpar.setBounds(390, 519, 144, 32);
		getContentPane().add(btLimpar);
		
		tfCod = new JTextField("");
		tfCod.setEnabled(false);
		tfCod.setFont(MainInternalFrame.fonte4);
		tfCod.setColumns(10);
		tfCod.setBounds(23, 534, 31, 28);
		getContentPane().add(tfCod);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				tfCod.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
				tfClassificacao.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
				btAtualizar.setEnabled(true);
				btCadastrar.setEnabled(false);
			}
		});
		table.setFont(MainInternalFrame.fonteTabela);
		spClassificacao.setViewportView(table);
		//Impede movimentação das colunas pelo usuário
		table.getTableHeader().setReorderingAllowed(false);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"C\u00F3digo", "Classifica\u00E7\u00E3o"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		btExcluir = new JButton("Excluir");
		btExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Se o retorno do método getSelectedRow tem valor diferente de -1, significa
				// que tem linha selecionada
				if (table.getSelectedRow() != -1) {
					
					// Se voltarValor é diferente de 2 significa que o usuário não cancelou a
					// operação e faz a autenticação do usuário
					if (UsuarioDao.validar(true, false, null, null)) {
						// Definindo o código da linha selecionada
						cf.setCodigo(Integer.parseInt(String.valueOf(table.getValueAt(table.getSelectedRow(), 0))));
						// Enviando para o método responsável na classe RegistroDao
						cfDao.deletarClassificacao(cf);
						
						limpar();
						atualizarTabela();
						
					}//Se não validar a autenticação o próprio método vai devolver a mensagem. 

				} else {
					JOptionPane.showMessageDialog(null, "É necessário selecionar uma linha da tabela para excluir.",
							"Não há seleção", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btExcluir.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btExcluir.setFont(new Font("Sitka Subheading", Font.PLAIN, 25));
		btExcluir.setBounds(390, 562, 144, 32);
		getContentPane().add(btExcluir);
		table.getColumnModel().getColumn(0).setPreferredWidth(15);
		
		atualizarTabela();

	}
	
	/**
	 * Limpar campos
	 */
	public void limpar() {
		tfCod.setText("");
		tfClassificacao.setText("");
		btAtualizar.setEnabled(false);
		btCadastrar.setEnabled(true);
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
}