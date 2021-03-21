package br.edu.ifc.videira.controllers.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
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

import br.edu.ifc.videira.DAOs.QuemDao;
import br.edu.ifc.videira.DAOs.UsuarioDao;
import br.edu.ifc.videira.beans.Quem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IFpEditarPessoas extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfNome;
	private JTable table;
	private List<Object> registros = new ArrayList<Object>();
	UsuarioDao usDao = new UsuarioDao();
	private JTextField tfCod;
	private JTextField tfSaldo;
	
	Quem qm = new Quem();
	QuemDao qd = new QuemDao();
	
	public IFpEditarPessoas() {
		super("Editar Pessoas # " + (++IFuLogin.openFrameCount) + "º", 
				false, // resizable
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

		JLabel lbNome = new JLabel("Nome:");
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
					tfSaldo.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
					
				}
			}
		});
		table.setFont(MainInternalFrame.fonteTabela);
		spPessoas.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"C\u00F3digo", "Nome", "Saldo", "Contato"
			}
		));
		table.getColumnModel().getColumn(1).setPreferredWidth(383);
		atualizarTabela();
		
		JButton btRegistrar = new JButton("Registrar");
		btRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				qm.setNome(tfNome.getText());
				qm.setSaldo(Double.parseDouble(tfSaldo.getText()));
				
				try {
					qd.cadastrarPessoa(qm);
					limpar();
					atualizarTabela();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		});
		btRegistrar.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btRegistrar.setFont(MainInternalFrame.fonte4);
		btRegistrar.setBounds(23, 448, 144, 32);
		getContentPane().add(btRegistrar);
		
		JButton btAtualizar = new JButton("Atualizar");
		btAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				qm.setCodigo(Integer.parseInt(tfCod.getText()));
				qm.setNome(tfNome.getText());
				qm.setSaldo(Double.parseDouble(tfSaldo.getText()));
				try {
					qd.atualizarPessoa(qm);
					atualizarTabela();
					limpar();
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btAtualizar.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btAtualizar.setFont(MainInternalFrame.fonte4);
		btAtualizar.setBounds(209, 448, 144, 32);
		getContentPane().add(btAtualizar);
		
		tfCod = new JTextField();
		tfCod.setFont(MainInternalFrame.fonte4);
		tfCod.setColumns(10);
		tfCod.setBounds(0, 483, 31, 28);
		getContentPane().add(tfCod);
		
		tfSaldo = new JTextField();
		tfSaldo.setFont(MainInternalFrame.fonte4);
		tfSaldo.setColumns(10);
		tfSaldo.setBounds(124, 356, 205, 34);
		getContentPane().add(tfSaldo);
		
		JLabel lbSaldo = new JLabel("Saldo:");
		lbSaldo.setFont(MainInternalFrame.fonte4);
		lbSaldo.setBounds(23, 356, 102, 34);
		getContentPane().add(lbSaldo);
		
		JButton btLimpar = new JButton("Limpar");
		btLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpar();
			}
		});
		btLimpar.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btLimpar.setFont(MainInternalFrame.fonte4);
		btLimpar.setBounds(390, 448, 144, 32);
		getContentPane().add(btLimpar);
		
		atualizarTabela();

	}
	/**
	 * Limpa campos
	 */
	public void limpar() {
		tfCod.setText("");
		tfNome.setText("");
		tfSaldo.setText("");		
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
}