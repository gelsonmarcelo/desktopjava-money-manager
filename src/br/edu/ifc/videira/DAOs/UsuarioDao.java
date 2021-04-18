package br.edu.ifc.videira.DAOs;

import java.awt.Dimension;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import br.edu.ifc.videira.beans.Usuario;
import br.edu.ifc.videira.controllers.views.MainInternalFrame;
import br.edu.ifc.videira.utils.Conexao;
import br.edu.ifc.videira.utils.LoginPanel;

public class UsuarioDao {
	public static int idUser=0;
	/**
	 * Acessa o banco levando usuario e senha para realizar a autenticação, pode ou não chamar um OptionPane para solicitação dos dados, para autenticações básicas.
	 * @param abrirJanela
	 * @param definirIdUser
	 * @param usuario
	 * @param senha
	 * @return
	 */
	public static boolean validar(boolean abrirJanela, boolean definirIdUser, String usuario, String senha) {
		//Falso até que se prove o contrário
		boolean sucesso = false;
		
		if(abrirJanela) {
			//Cria o objeto para importar o Panel e utilizar no OptionPane personalizado com 2 campos para entrada de dados
			LoginPanel lg = new LoginPanel();
			//Define um tamanho personalizado para o OptionPane, assim aparecem todos os elementos do Panel personalizado criado
			UIManager.put("OptionPane.minimumSize", new Dimension(330, 210));
			//Quando o usuário informa os dados e pressiona Ok, o programa recupera essas entradas e define nas variáveis para validação
			int resultado = JOptionPane.showConfirmDialog(null, lg, "Autenticação", JOptionPane.OK_CANCEL_OPTION);
			//Redefine o tamanho dos optionPanes para default
			UIManager.put("OptionPane.minimumSize", null);
			//Se o usuário pressionou o botão de OK
			if (resultado == JOptionPane.OK_OPTION) {
				//Define as entradas do usuário como valor das variáveis
				usuario = lg.tfUsuario.getText();
				senha = String.valueOf(lg.psSenha.getPassword());

			}else { //Caso contrário ele cancelou a autenticação
				JOptionPane.showMessageDialog(null,
						"Operação cancelada pelo usuário.",
						"Aviso", JOptionPane.WARNING_MESSAGE);
				//Nesse caso interrompe a execução do método retornando direto false
				return false;
			}
		}
		
		//Parte que leva os dados para verifiação no Bando de Dados
		try {
			String sql;
			//Se o idUser for diferente de 0 significa que o usuário já está autenticado no sistema, senão é o primeiro login
			if (idUser != 0) {
				sql = "SELECT * FROM usuario WHERE login=? AND senha=? AND idusuario=" + idUser + " limit 1";
			}else {
				sql = "SELECT * FROM usuario WHERE login=? AND senha=? limit 1";
			}
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, usuario);
			sqlPrep.setString(2, senha);

			ResultSet rs = sqlPrep.executeQuery();

			while (rs.next()) {
				sucesso = true;
				if (definirIdUser) {
					//Caso confirmado que se deve definir o id, através do parâmetro setarIdUser, tal será pego do banco
					idUser = Integer.parseInt(rs.getString(1));
				}
			}
			rs.close();
			sqlPrep.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		//Estou mostrando a mensagem aqui pois se o usuário cancelar a operação (no optionPane), vai retornar false para a classe que chamou e o usuário receberá a mensagem que cancelou a operação mais a mensagem de senha incorreta.
		if(abrirJanela && !sucesso) {
			JOptionPane.showMessageDialog(null,
					"Usuário e/ou senha incorretos, verifique.",
					"Operação cancelada", JOptionPane.ERROR_MESSAGE);
		}
		return sucesso;
	}
	
	/**
	 * Cadastra um novo usuário no banco
	 * 
	 * @param us
	 * @throws SQLException
	 * @throws Exception
	 */
	public boolean cadastrarUsuario(Usuario us) throws SQLException, Exception {
		
		try {
			String sql = "INSERT INTO usuario (login, senha, salario) VALUES (?,?,?)";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, us.getLogin());
			sqlPrep.setString(2, us.getSenha());
			sqlPrep.setDouble(3, us.getSalario());
			sqlPrep.execute();
			sqlPrep.close();

			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return false;
	}
	
	/**
	 * Atualiza informações do usuário
	 */
	public void atualizarUsuario(Usuario us) {

		try {
			String sql = "UPDATE usuario SET senha=?, salario=? WHERE idusuario=" + idUser;
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, us.getSenha());
			sqlPrep.setDouble(contador++, us.getSalario());
			sqlPrep.execute();
			sqlPrep.close();
			JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	/**
	 * Acessa o banco buscando informações do usuario
	 * @param idUsuario
	 * @return
	 */
	public static String[] buscarUsuario() throws SQLException, Exception {
		String usuario[] = new String[2];
		String sql = null;
		try {
			sql = "SELECT * FROM usuario WHERE idusuario=" + idUser;
			
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()) {
				usuario[0] = rs.getString(2);
				usuario[1] = rs.getString(4);
			}
			state.close();
			rs.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		return usuario;
	}
	
	/**
	 * Manda o tema escolhido para salvar no usuário
	 * @param us
	 */
	public void salvarTema(String tema) {
		try {
			String sql = "UPDATE usuario SET tema=? WHERE idusuario=" + idUser;
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, tema);
			
			sqlPrep.execute();
			sqlPrep.close();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	/**
	 * Recupera o tema do usuário 
	 * @return
	 */
	public String recuperarTema() {
		String tema = "";
		try {
			String sql = "SELECT tema FROM usuario WHERE idusuario=" + idUser;
			
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()) {
				tema = rs.getString(1);
			}
			state.close();
			rs.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		for (int i = 0; i < MainInternalFrame.cbTema.getItemCount(); i++) {
			if (MainInternalFrame.cbTema.getItemAt(i).toString().split("\\* ")[1].equals(tema)) {
				return MainInternalFrame.cbTema.getItemAt(i).toString();
			}
		}
		return MainInternalFrame.cbTema.getItemAt(0).toString();
	}
}
