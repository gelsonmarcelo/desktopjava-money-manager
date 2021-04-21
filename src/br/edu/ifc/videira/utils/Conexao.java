package br.edu.ifc.videira.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*Utils: coisas que servem para o programa em geral*/
public class Conexao {
	/**
	 * conexão MySQL
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection conectar() throws Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			/* Casa */
			Connection conexao = DriverManager.getConnection(
					"jdbc:mysql://localhost/db_money_manager?user=root&password=password&useTimezone=true&serverTimezone=UTC");
			return conexao;
		} catch (ClassNotFoundException | SQLException e) {
			throw new Exception(e.getMessage());
		}
	}
}