package br.edu.ifc.videira.DAOs;

import java.sql.ResultSet;

import javax.swing.JOptionPane;

import br.edu.ifc.videira.controllers.views.MainInternalFrame;
import br.edu.ifc.videira.utils.Conexao;

public class TotalDao {

	/**
	 * Método que atualiza o saldo total do usuário, apresentado na janela principal 
	 */
	public void atualizarTotal() {
		String total = "";
		try {
			String sql = "SELECT sum(saldo) FROM instituicao WHERE idusuario = " + UsuarioDao.idUser;
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);
			// Joga o retorno do banco para a String
			while (rs.next()) {
				total = rs.getString(1);
			}
			rs.close();
			state.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Falha ao atualizar o saldo total.",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		MainInternalFrame.tfSaldoTotal.setText(total);
	}
}
