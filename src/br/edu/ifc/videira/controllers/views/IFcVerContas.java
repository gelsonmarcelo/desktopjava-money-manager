package br.edu.ifc.videira.controllers.views;

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

import com.toedter.calendar.JDateChooser;

import br.edu.ifc.videira.DAOs.UsuarioDao;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class IFcVerContas extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField tfBusca;
	private JTable table;
	private List<Object> registros = new ArrayList<Object>();
	UsuarioDao usDao = new UsuarioDao();

	public IFcVerContas() {
		super("Ver Contas # " + (++IFuLogin.openFrameCount) + "º", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable

		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...
		setSize(1000, 650);

		// Set the window's location.
		setLocation(IFuLogin.xOffset * IFuLogin.openFrameCount, IFuLogin.yOffset * IFuLogin.openFrameCount);
		getContentPane().setLayout(null);

		JLabel lbTitle = new JLabel("Ver Contas");
		lbTitle.setFont(MainInternalFrame.fonte1);
		lbTitle.setBounds(0, 0, 984, 55);
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitle);

		JLabel lblNewLabel = new JLabel("Buscar:");
		lblNewLabel.setFont(MainInternalFrame.fonte4);
		lblNewLabel.setBounds(687, 66, 92, 34);
		getContentPane().add(lblNewLabel);

		tfBusca = new JTextField();
		tfBusca.setFont(MainInternalFrame.fonte4);
		tfBusca.setBounds(774, 67, 200, 34);
		getContentPane().add(tfBusca);
		tfBusca.setColumns(10);
		
		JLabel lblDataEspecfica = new JLabel("De");
		lblDataEspecfica.setFont(MainInternalFrame.fonte4);
		lblDataEspecfica.setBounds(38, 66, 42, 34);
		getContentPane().add(lblDataEspecfica);
		
		JDateChooser dcInicio = new JDateChooser();
		dcInicio.setBounds(76, 66, 162, 34);
		dcInicio.setFont(MainInternalFrame.fonte6);
		getContentPane().add(dcInicio);
		
		JDateChooser dcFim = new JDateChooser();
		dcFim.setFont(MainInternalFrame.fonte6);
		dcFim.setBounds(288, 66, 162, 34);
		getContentPane().add(dcFim);
		
		JLabel lblFim = new JLabel("at\u00E9");
		lblFim.setFont(MainInternalFrame.fonte4);
		lblFim.setBounds(248, 66, 42, 34);
		getContentPane().add(lblFim);
		
		JScrollPane spRegistros = new JScrollPane();
		spRegistros.setBounds(20, 110, 964, 433);
		getContentPane().add(spRegistros);

		table = new JTable();
		table.setFont(MainInternalFrame.fonteTabela);
		spRegistros.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"C\u00F3digo", "Nome", "Descri\u00E7\u00E3o", "Pr\u00F3ximo Vencimento", "Situa\u00E7\u00E3o"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(58);
		table.getColumnModel().getColumn(1).setPreferredWidth(113);
		table.getColumnModel().getColumn(2).setPreferredWidth(285);
		table.getColumnModel().getColumn(3).setPreferredWidth(112);
		
		JButton btExcluir = new JButton("Excluir");
		btExcluir.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btExcluir.setFont(MainInternalFrame.fonte4);
		btExcluir.setBounds(592, 568, 173, 32);
		getContentPane().add(btExcluir);
		
		JButton btEditar = new JButton("Editar");
		btEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btEditar.setFont(MainInternalFrame.fonte4);
		btEditar.setBounds(414, 568, 168, 32);
		getContentPane().add(btEditar);
		
		JButton btNovaConta = new JButton("Nova Conta");
		btNovaConta.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btNovaConta.setFont(MainInternalFrame.fonte4);
		btNovaConta.setBounds(231, 568, 173, 32);
		getContentPane().add(btNovaConta);
		
		JButton btPaga = new JButton("Paga");
		btPaga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btPaga.setToolTipText("Quando ativado, arbre menu para escolher institui\u00E7\u00E3o para gera valor descontando do saldo do usu\u00E1rio.");
		btPaga.setFont(MainInternalFrame.fonte4);
		btPaga.setBounds(535, 66, 92, 32);
		getContentPane().add(btPaga);
		atualizarTabela();
	}
	
	/**
	 * Pega informações do banco e atualiza a tabela
	 */
	public void atualizarTabela() {
		try {
//			registros = usDao.buscarTodos();
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