package br.edu.ifc.videira.controllers.views;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
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
import br.edu.ifc.videira.beans.Registro;

import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;

public class IFdDividas extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private List<Object> registros = new ArrayList<Object>();
	UsuarioDao usDao = new UsuarioDao();
	QuemDao qmDao = new QuemDao();
	private JTextField tfSaldo;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IFdDividas() {
		super("Ver Dívidas # " + (++IFuLogin.openFrameCount) + "º", 
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

		JLabel lbTitle = new JLabel("Ver Dívidas");
		lbTitle.setFont(MainInternalFrame.fonte1);
		lbTitle.setBounds(0, 0, 984, 55);
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitle);

		JLabel lbPessoa = new JLabel("Pessoa:");
		lbPessoa.setFont(new Font("Dialog", Font.PLAIN, 28));
		lbPessoa.setBounds(10, 66, 100, 34);
		getContentPane().add(lbPessoa);
		
		JScrollPane spDividas = new JScrollPane();
		spDividas.setBounds(10, 124, 964, 433);
		getContentPane().add(spDividas);

		table = new JTable();
		table.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		spDividas.setViewportView(table);
		table.getTableHeader().setReorderingAllowed(false);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"C\u00F3digo", "Valor", "Descri\u00E7\u00E3o", "Data"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(15);
		table.getColumnModel().getColumn(0).setMinWidth(10);
		table.getColumnModel().getColumn(2).setPreferredWidth(400);
		table.getColumnModel().getColumn(2).setMinWidth(20);
		
		JButton btExcluirRegistro = new JButton("Excluir");
		btExcluirRegistro.setToolTipText("Necessita autentica\u00E7\u00E3o");
		btExcluirRegistro.setFont(new Font("IrisUPC", Font.PLAIN, 35));
		btExcluirRegistro.setBounds(591, 568, 150, 32);
		getContentPane().add(btExcluirRegistro);
		
		JButton btEditarRegistro = new JButton("Editar");
		btEditarRegistro.setFont(new Font("IrisUPC", Font.PLAIN, 35));
		btEditarRegistro.setBounds(431, 568, 150, 32);
		getContentPane().add(btEditarRegistro);
		
		JComboBox<Object> cbQuem = new JComboBox<Object>();
		//Preenchimento do comboBox com valores cadastrados no banco
		try {
			cbQuem.setModel(new DefaultComboBoxModel(new String[] {"Essa tela n\u00E3o ser\u00E1 mais necess\u00E1ria", "2*Segunda Pessoa*0", "3*terce*5100.99", "4*tryu*4499.997", "6*sexta*5666", "7*quem?*1500"}));
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'VR001'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
		}
		cbQuem.setFont(new Font("Dialog", Font.PLAIN, 23));
		cbQuem.setBounds(106, 66, 363, 34);
		getContentPane().add(cbQuem);
		cbQuem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				//Verifica se ocorreram mudanças no item selecionado dentro do Combobox
				if (e.getStateChange() == ItemEvent.SELECTED) {
					//Pegando o texto do item selecionado
					atualizarTabela(e.getItem().toString(), null);
				}
			}
		});
		
		tfSaldo = new JTextField();
		tfSaldo.setEditable(false);
		tfSaldo.setFont(new Font("IrisUPC", Font.PLAIN, 30));
		tfSaldo.setColumns(10);
		tfSaldo.setBounds(816, 66, 158, 34);
		getContentPane().add(tfSaldo);
		
		JLabel lbSaldo = new JLabel("Saldo:");
		lbSaldo.setFont(new Font("IrisUPC", Font.PLAIN, 35));
		lbSaldo.setBounds(726, 66, 80, 34);
		getContentPane().add(lbSaldo);
		
		JButton btNovoRegistro = new JButton("Novo");
		btNovoRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrar();
			}
		});
		btNovoRegistro.setFont(new Font("IrisUPC", Font.PLAIN, 35));
		btNovoRegistro.setToolTipText("### - Travar campo Quem");
		btNovoRegistro.setBounds(271, 568, 150, 32);
		getContentPane().add(btNovoRegistro);
//		atualizarTabela();
	}
	
	/**
	 * ### - Quando abrir esta seguinte janela a partir daqui travar o campo Quem com 
	 * pessoa selecionada daqui.
	 **/
	protected void registrar() {
		IFrRegistrar frame = new IFrRegistrar();
		frame.setVisible(true);
		MainInternalFrame.desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Pega informações do banco e atualiza a tabela
	 * @param tipoRegistro: tipo de registro selecionado pelo usuário
	 * @param rg: objeto do tipo Registro
	 * @param pesquisaIntervaloDatas: se a consulta será para retornar valores levando em consideração o intervalo de datas passado no objeto
	 */
	public void atualizarTabela(String tipoRegistro, Registro rg) {
		try {
			//Traz os dados executando o método buscarRegistros na classe DAO e atribui na variável registros
//			registros = qmDao.buscarRegistros(tipoRegistro);
			//Define o tamanho da lista de registros (quantas linhas), coloquei em variável antes do for pra não precisar chamar um método size() pra descobrir o tamanho da lista toda vez que passar pelo for.
			int qntLinhas = registros.size();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setNumRows(0);
			for (int i = 0; i != qntLinhas; i++) {
				//Vai incluindo linhas conforme passa pelos objetos na lista
				model.addRow((Object[]) registros.get(i));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, não foi possível preencher a tabela adequadamente, contate o desenvolvedor e informe o código 'VR009'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}