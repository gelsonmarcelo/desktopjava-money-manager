package br.edu.ifc.videira.controllers.views;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import br.edu.ifc.videira.DAOs.InstituicaoDao;
import br.edu.ifc.videira.beans.Transferencia;
import br.edu.ifc.videira.utils.JNumberFormatField;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class IFiTransferir extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JNumberFormatField tfValor;
	private InstituicaoDao inDao = new InstituicaoDao();
	private Transferencia tr = new Transferencia();

	public JComboBox<Object> cbOrigem;
	public JComboBox<Object> cbDestino;
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IFiTransferir() {
		super("Transferir # " + (++IFuLogin.openFrameCount) + "º", 
				false, // resizable
				true, // closable
				false, // maximizable
				true);// iconifiable

		// ...Create the GUI and put it in the window...

		// ...Then set the window size or call pack...

		setSize(565, 345);

		// Set the window's location.
		setLocation(IFuLogin.xOffset * IFuLogin.openFrameCount, IFuLogin.yOffset * IFuLogin.openFrameCount);
		getContentPane().setLayout(null);

		JLabel lbTitle = new JLabel("Transfer\u00EAncias");
		lbTitle.setFont(MainInternalFrame.fonte1);
		lbTitle.setBounds(0, 0, 549, 55);
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lbTitle);

		JLabel lbValor = new JLabel("Enviar a quantia de");
		lbValor.setFont(MainInternalFrame.fonte4);
		lbValor.setBounds(52, 98, 272, 34);
		getContentPane().add(lbValor);

		JLabel lbDestino = new JLabel("Destino:");
		lbDestino.setFont(MainInternalFrame.fonte4);
		lbDestino.setBounds(50, 185, 99, 34);
		getContentPane().add(lbDestino);

		tfValor = new JNumberFormatField();
		tfValor.setFont(new Font("Calibri", Font.PLAIN, 20));
		tfValor.setColumns(10);
		tfValor.setBounds(274, 99, 220, 34);
		getContentPane().add(tfValor);
		
		JButton btTransferir = new JButton("Transferir");
		btTransferir.setEnabled(false);
		btTransferir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Pegar instituição
				try {
					//Formatar para pegar apenas o ID a partir da opção selecionada.
					tr.setIdInstituicaoOrigem(Integer.parseInt(String.valueOf(cbOrigem.getSelectedItem()).split("\\*")[0]));
					tr.setIdInstituicaoDestino(Integer.parseInt(String.valueOf(cbDestino.getSelectedItem()).split("\\*")[0]));
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'IN003'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
					e2.printStackTrace();
					return;
				}
				
				//Pegar valor
				try {
					tr.setValor(Double.parseDouble(tfValor.getText().replaceAll("\\.", "").replaceAll(",", ".").replace("R$ ", "")));
					if(tr.getValor() <= 0) {
						JOptionPane.showMessageDialog(null, "O valor para transferir deve ser maior que 0", "Verifique o valor informado", JOptionPane.WARNING_MESSAGE);
						return;
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "O programa não conseguiu converter o valor em números, verifique os dados digitados", "Falha de conversão", JOptionPane.WARNING_MESSAGE);
					e2.printStackTrace();
					//Limpa campo incorreto
					tfValor.setText("");
					return;
				}
				
				//Processar
				try {
					inDao.transferir(tr);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				}
				
				//Limpar
				limpar();
			}
		});
		btTransferir.setFont(MainInternalFrame.fonte4);
		btTransferir.setBounds(100, 250, 155, 34);
		getContentPane().add(btTransferir);
		
		cbDestino = new JComboBox();
		cbDestino.addItemListener(new ItemListener() {
			
			//Habilita o botão de transferir apenas se o campo de destino foi preenchido, o que significa que o de origem já foi selecionado também.
			public void itemStateChanged(ItemEvent e) {
				if(!cbDestino.getSelectedItem().equals("")) {
					btTransferir.setEnabled(true);
				}
			}
		});
		cbDestino.setEnabled(false);
		//Preenchimento do comboBox com valores cadastrados no banco
		try {
			cbDestino.setModel(new DefaultComboBoxModel(inDao.buscarNomesInstituicao()));
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'IN002'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
		}
		cbDestino.setFont(MainInternalFrame.fonte5);
		cbDestino.setBounds(146, 188, 348, 34);
		getContentPane().add(cbDestino);
		
		JButton btLimpar = new JButton("Limpar");
		btLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpar();
			}
		});
		btLimpar.setFont(MainInternalFrame.fonte4);
		btLimpar.setBounds(319, 250, 155, 34);
		getContentPane().add(btLimpar);
		
		cbOrigem = new JComboBox<>();
		cbOrigem.addItemListener(new ItemListener() {
			
			//Isso fará com que seja impossível o usuário selecionar o mesmo item de origem e destino, dispensando a validação no momento do processamento.
			public void itemStateChanged(ItemEvent arg0) {
				//Remove item selecionado no combobox de origem
				cbDestino.removeItem(cbOrigem.getSelectedItem());
				//Deixa combobox de destino acessível para o usuário selecionar
				cbDestino.setEnabled(true);
				//Desabilita o combobox de origem pois se o usuário alterar o combobox depois de já ter escolhido, vai ir apagando as opções do destino e não voltam mais.
				cbOrigem.setEnabled(false);
			}
		});
		//Preenchimento do comboBox com valores cadastrados no banco
		try {
			cbOrigem.setModel(new DefaultComboBoxModel(inDao.buscarNomesInstituicao()));
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'IN001'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
		}
		cbOrigem.setFont(MainInternalFrame.fonte5);
		cbOrigem.setBounds(146, 143, 348, 34);
		getContentPane().add(cbOrigem);
		
		JLabel lbOrigem = new JLabel("Origem:");
		lbOrigem.setFont(MainInternalFrame.fonte4);
		lbOrigem.setBounds(52, 140, 97, 34);
		getContentPane().add(lbOrigem);
	}
	
	/**
	 * Limpa os campos
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void limpar() {
		
		cbOrigem.setEnabled(true);
		cbDestino.setEnabled(false);
		
		tfValor.setText("");
		
		//Atualização no preenchimento do comboBox com valores atualizados
		try {
			//Busca nomes com valores atualizados
			cbDestino.setModel(new DefaultComboBoxModel(inDao.buscarNomesInstituicao()));
			//Copia modelo atualizado para o segundo combobox
			cbOrigem.setModel(new DefaultComboBoxModel(inDao.buscarNomesInstituicao()));
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, contate o desenvolvedor e informe o código 'IN004'!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
}