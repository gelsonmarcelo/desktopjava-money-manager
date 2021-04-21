package br.edu.ifc.videira.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifc.videira.beans.Classificacao;
import br.edu.ifc.videira.utils.Conexao;

public class ClassificacaoDao {
	String sql;
	java.sql.PreparedStatement sqlPrep;
	java.sql.Statement state;
	/**
	 * Cadastra uma nova pessoa na tabela
	 * 
	 * @param qm
	 * @throws SQLException
	 * @throws Exception
	 */
	public void cadastrarClassificacao(Classificacao cf) throws SQLException, Exception {
		try {
			//### - Falta adaptação do usuário específico para classificações
			sql = "INSERT INTO classificacao (nome) VALUES (?)";
			sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, cf.getNome());
			sqlPrep.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, falha ao registrar classificacao no banco de dados, contate o desenvolvedor e informe o código DC002!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();		}
	}

	/**
	 * Atualizar informações de uma classificacao
	 * 
	 * @param cf
	 * @throws Exception
	 */
	public void atualizarClassificacao(Classificacao cf) throws Exception {
		try {
			sql = "UPDATE classificacao SET nome=? WHERE idclassificacao=?;";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, cf.getNome());
			sqlPrep.setInt(2, cf.getCodigo());
			sqlPrep.execute();
			JOptionPane.showMessageDialog(null, "Classificação atualizada com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, falha ao atualizar classificacao no banco de dados, contate o desenvolvedor e informe o código DC003!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Retorna todas as informações das classificações na tabela
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTodos() throws SQLException, Exception {
		List<Object> classificacoes = new ArrayList<Object>();
		try {
			sql = "SELECT * FROM classificacao;";
			state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2)};
				classificacoes.add(linha);
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return classificacoes;
	}
	
	/**
	 * Retorna nome das classificações, especialmente criado para preencher comboBoxes dos
	 * frames
	 */
	public String[] buscarNomesClassificacoes() throws SQLException, Exception {
		String[] nomeClassificacao = null;
		int qntNomes = 0;
		int i = 1;
		try {
			//Pega somente a relação de pessoas cadastradas na conta do usuário atualmente logado
			sql = "SELECT COUNT(nome) FROM classificacao";
			state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			// Conta quantos nomes tem na tabela
			while (rs.next()) {
				qntNomes = Integer.parseInt(rs.getString(1));
			}
			rs.close();

			// Declara vetor com a quantidade de nomes recebida
			nomeClassificacao = new String[qntNomes + 1];

			// Adiciona primeiro indice como vazio para exibir nos comboBoxes
			nomeClassificacao[0] = "";

			//Essa SQL unirá o ID da classificacao ao nome no retorno vindo do banco de dados
			sql = "SELECT CONCAT_WS('*', idclassificacao, nome) as 'resultado' FROM classificacao";
			rs = state.executeQuery(sql);
			// Passa nomes da tabela para o vetor
			while (rs.next()) {
				nomeClassificacao[i] = rs.getString("resultado");
				i++;
			}
			rs.close();
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return nomeClassificacao;
	}
	
	/**
	 * Buscará, na tabela classificacao, as informações da pessoa com id que consta no registro com de id igual o parâmetro passado e montará a string
	 * para ficar compatível com as opções do comboBox que existe na tela de Registro.
	 * @param nomeClassificacao
	 * @return String do nome com dados montada
	 * @throws Exception
	 */
	public String recuperarClassificacao(int codigoRegistro) throws Exception {
		String dadosClassificacao = "";
		try {
			// Essa SQL unirá o ID da classificacao ao nome, a partir do id do registro, montando uma string com formato exato das opções do combobox para pré-selecionar o item exato quando chamar a janela de registro.
			sql = "SELECT CONCAT_WS('*', classificacao.idclassificacao, classificacao.nome) as 'resultado' FROM classificacao, registro WHERE registro.idregistro = " + codigoRegistro + " AND registro.idclassificacao = classificacao.idclassificacao;";/* ### - + "' AND quem.idusuario = " + UsuarioDao.idUser;*/
			state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);
			// Joga o retorno do banco para a String
			while (rs.next()) {
				dadosClassificacao = rs.getString("resultado");
			}
			rs.close();
			state.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, falha ao recuperar classificacao do registro, contate o desenvolvedor e informe o código DC001!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return dadosClassificacao;
	}
	
	/**
	 * Exclui um registro de conta
	 * @param rg
	 * @param voltarValor
	 * @param operacao
	 */
	public void deletarClassificacao(Classificacao cf){
		//Isso executará de qualquer forma, pois é onde exclui o registro
		//Código que irá executar no banco
		sql = "DELETE FROM classificacao WHERE idclassificacao = ?";
		try {
			//Declaração do PreparedStatement com sql
			sqlPrep = Conexao.conectar().prepareStatement(sql);
			//Definindo os valores ? da SQL
			sqlPrep.setInt(1, cf.getCodigo());
			//Executando Query
			sqlPrep.execute();
			JOptionPane.showMessageDialog(null, "Exlusão do classificação realizada com sucesso!");
			sqlPrep.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, classificação pode não ter sido deletada, contate o desenvolvedor e informe o código ''!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);			
			e.printStackTrace();
		}
	}
}