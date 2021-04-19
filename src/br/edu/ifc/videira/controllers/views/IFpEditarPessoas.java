package br.edu.ifc.videira.controllers.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import br.edu.ifc.videira.DAOs.QuemDao;
import br.edu.ifc.videira.DAOs.UsuarioDao;
import br.edu.ifc.videira.beans.Quem;
import br.edu.ifc.videira.utils.JNumberFormatField;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.ImageIcon;

public class IFpEditarPessoas extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfNome;
	private JTable table;
	private List<Object> registros = new ArrayList<Object>();
	UsuarioDao usDao = new UsuarioDao();
	private JTextField tfCod;
	private JNumberFormatField tfSaldo;

	Quem qm = new Quem();
	QuemDao qd = new QuemDao();
	private JTextField tfContato;
	private JCheckBox cboxSaldoNegativo;
	private JButton btSalvar;

	public IFpEditarPessoas() {
		super("Editar Pessoas # " + (++IFuLogin.openFrameCount) + "º", false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable

		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(565, 540);

		// Set the window's location.
		setLocation(IFuLogin.xOffset * IFuLogin.openFrameCount, IFuLogin.yOffset * IFuLogin.openFrameCount);
		getContentPane().setLayout(null);

		JLabel lblTitulo = new JLabel("Editar pessoas/entidades");
		lblTitulo.setFont(MainInternalFrame.fonte1);
		lblTitulo.setBounds(0, 0, 549, 55);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblTitulo);

		JLabel lbNome = new JLabel("*Nome:");
		lbNome.setFont(MainInternalFrame.fonte4);
		lbNome.setBounds(23, 300, 102, 34);
		getContentPane().add(lbNome);

		tfNome = new JTextField();
		tfNome.setFont(MainInternalFrame.fonte4);
		tfNome.setBounds(124, 301, 410, 34);
		getContentPane().add(tfNome);
		tfNome.setColumns(10);

		JScrollPane spPessoas = new JScrollPane();
		spPessoas.setBounds(23, 66, 511, 223);
		getContentPane().add(spPessoas);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (table.getSelectedRow() != -1) {
					tfCod.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
					tfNome.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
					String saldo = String.valueOf(table.getValueAt(table.getSelectedRow(), 2));
					tfSaldo.setText(saldo);
					if (Double.parseDouble(saldo) < 0) {
						cboxSaldoNegativo.setSelected(true);
					} else {
						cboxSaldoNegativo.setSelected(false);
					}
				}
			}
		});
		table.setFont(MainInternalFrame.fonteTabela);
		spPessoas.setViewportView(table);
		table.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "C\u00F3digo", "Nome", "Saldo", "Contato" }) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					boolean[] columnEditables = new boolean[] { false, false, false, false };

					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});
		table.getColumnModel().getColumn(1).setPreferredWidth(383);
		atualizarTabela();

		btSalvar = new JButton("Cadastrar");
		btSalvar.setIcon(new ImageIcon(IFpEditarPessoas.class.getResource("/br/edu/ifc/videira/imgs/salvar.png")));
		btSalvar.addActionListener(new AcaoSalvar());
		btSalvar.setToolTipText(
				"Preencha os dados para cadastrar uma nova pessoa ou selecione uma linha da tabela para atualizar.");
		btSalvar.setFont(MainInternalFrame.fonteBotoes);
		btSalvar.setBounds(80, 448, 170, 39);
		getContentPane().add(btSalvar);

		tfCod = new JTextField();
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
		tfCod.setFont(MainInternalFrame.fonte4);
		tfCod.setColumns(10);
		tfCod.setBounds(0, 483, 31, 28);
		tfCod.setVisible(false);
		getContentPane().add(tfCod);

		tfSaldo = new JNumberFormatField();
		tfSaldo.setFont(new Font("Calibri", Font.PLAIN, 20));
		tfSaldo.setColumns(10);
		tfSaldo.setBounds(124, 390, 149, 34);
		getContentPane().add(tfSaldo);

		JLabel lbSaldo = new JLabel("Saldo:");
		lbSaldo.setFont(MainInternalFrame.fonte4);
		lbSaldo.setBounds(23, 390, 102, 34);
		getContentPane().add(lbSaldo);

		JButton btLimpar = new JButton("Limpar");
		btLimpar.setIcon(new ImageIcon(IFpEditarPessoas.class.getResource("/br/edu/ifc/videira/imgs/apagador.png")));
		btLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpar();
			}
		});
		btLimpar.setToolTipText("Limpar seleção/valores inseridos");
		btLimpar.setFont(MainInternalFrame.fonteBotoes);
		btLimpar.setBounds(300, 448, 170, 39);
		getContentPane().add(btLimpar);

		JLabel lbContato = new JLabel("Contato:");
		lbContato.setToolTipText("N\u00E3o dispon\u00EDvel ainda");
		lbContato.setEnabled(false);
		lbContato.setFont(new Font("Sitka Subheading", Font.PLAIN, 25));
		lbContato.setBounds(23, 345, 102, 34);
		getContentPane().add(lbContato);

		tfContato = new JTextField();
		tfContato.setToolTipText("N\u00E3o dispon\u00EDvel ainda");
		tfContato.setEnabled(false);
		tfContato.setFont(new Font("Sitka Subheading", Font.PLAIN, 25));
		tfContato.setColumns(10);
		tfContato.setBounds(124, 345, 410, 34);
		getContentPane().add(tfContato);

		cboxSaldoNegativo = new JCheckBox("Saldo negativo");
		cboxSaldoNegativo.setFont(MainInternalFrame.fonte5);
		cboxSaldoNegativo.setBounds(279, 397, 255, 23);
		getContentPane().add(cboxSaldoNegativo);

		atualizarTabela();

	}

	/**
	 * Limpa campos
	 */
	public void limpar() {
		tfCod.setText("");
		tfNome.setText("");
		tfSaldo.setText("");
		cboxSaldoNegativo.setSelected(false);
		table.clearSelection();
	}

	/**
	 * Pega informações do banco e atualiza a tabela
	 */
	public void atualizarTabela() {
		try {
			registros = qd.buscarTodos();
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
	 * Método que vai definir qual ação será tomada quando o usuário clicar em
	 * salvar Se o usuário tiver selecionado algum item da tabela essa função fará
	 * uma atualização do item Se ele nao tiver selecionado nada, significa que é um
	 * novo cadastro.
	 * 
	 * @author Gelson
	 *
	 */
	private class AcaoSalvar implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// Se o campo de código estiver vazio o usuário nao selecionou nada da tabela.
			if (tfCod.getText().isEmpty()) {
				if (!tfNome.getText().isEmpty()) {
					qm.setNome(tfNome.getText());
					if (cboxSaldoNegativo.isSelected()) {
						qm.setSaldo(tfSaldo.getText(), true);
					} else {
						qm.setSaldo(tfSaldo.getText(), false);
					}

					try {
						qd.cadastrarPessoa(qm);
						limpar();
						atualizarTabela();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"Pelo menos o nome da pessoa deve ser preenchido para cadastrar.",
							"Campo obrigatório em branco", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				// Senão vai atualizar
				if (!tfNome.getText().isEmpty()) {
					qm.setCodigo(Integer.parseInt(tfCod.getText()));
					qm.setNome(tfNome.getText());
					qm.setSaldo(tfSaldo.getText(), cboxSaldoNegativo.isSelected());
					try {
						qd.atualizarPessoa(qm);
						atualizarTabela();
						limpar();

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"Pelo menos o nome da pessoa deve ser preenchido para atualizar",
							"Campo obrigatório em branco", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

}