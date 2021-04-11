package br.edu.ifc.videira.DAOs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifc.videira.utils.Conexao;

public class LogDao {
	protected String sql;
	protected java.sql.Statement state;
	protected ResultSet rs;
	final SimpleDateFormat formatoAmericano = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	final SimpleDateFormat formatoBrasileiro = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	/**
	 * Retorna todos os registros da tabela
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarRegistros() throws SQLException, Exception {
		List<Object> logs = new ArrayList<Object>();
		try {
			sql = "SELECT * FROM log WHERE usuario=" + UsuarioDao.idUser + " OR usuario=0 ORDER BY id DESC";
			state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2), formatoBrasileiro.format(formatoAmericano.parse(rs.getString(3))), rs.getString(4),
						rs.getString(5) };
				logs.add(linha);
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return logs;
	}

	/**
	 * Busca todas os registros compreendidos entre o periodo de datas passado
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 **/
	public List<Object> buscarRegistrosPeriodo(String dataInicio, String dataFim) throws SQLException, Exception {
		List<Object> logs = new ArrayList<Object>();

		sql = "SELECT * " + "FROM log " + "WHERE (usuario = " + UsuarioDao.idUser + " OR usuario = 0)"
		// ### - Isso (idusuario = 0) temporariamente até corrigir as classificações por usuario
				+ " AND (momento >= '" + dataInicio + "' AND momento <= '" + dataFim + " 23:59:59') ORDER BY momento DESC";

		state = Conexao.conectar().createStatement();
		rs = state.executeQuery(sql);
		while (rs.next()) {
			Object[] linha = { rs.getString(1), rs.getString(2),
					formatoBrasileiro.format(formatoAmericano.parse(rs.getString(3))), rs.getString(4),
					rs.getString(5) };
			logs.add(linha);
		}
		state.close();
		rs.close();

		return logs;
	}
}