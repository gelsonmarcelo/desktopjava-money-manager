package br.edu.ifc.videira.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifc.videira.beans.Quem;
import br.edu.ifc.videira.utils.Conexao;

public class QuemDao {

	/**
	 * Cadastra uma nova pessoa na tabela
	 * 
	 * @param qm
	 * @throws SQLException
	 * @throws Exception
	 */
	public void cadastrarPessoa(Quem qm) throws SQLException, Exception {
		try {
			String sql = "INSERT INTO quem (nome, saldo, idusuario) VALUES (?,?," + UsuarioDao.idUser + ")";
			java.sql.PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, qm.getNome()); // ou no lugar de "contador++" 1, 2, 3...
			sqlPrep.setDouble(contador++, qm.getSaldo());
			sqlPrep.execute();

			JOptionPane.showMessageDialog(null, "Pessoa cadastrada com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Atualizar informações de uma pessoa
	 * 
	 * @param qm
	 * @throws Exception
	 */
	public void atualizarPessoa(Quem qm) throws Exception {
		try {
			String sql = "UPDATE quem SET nome=?, saldo=? WHERE idquem=?;";
			PreparedStatement sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setString(contador++, qm.getNome());
			sqlPrep.setDouble(contador++, qm.getSaldo());
			sqlPrep.setInt(contador++, qm.getCodigo());
			sqlPrep.execute();

			JOptionPane.showMessageDialog(null, "Pessoa atualizada com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Infelizmente ocorreu um erro inesperado, se persistir contate o desenvolvedor:\n" + e.getMessage(),
					"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Retorna todas as informações das pessoas na tabela
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTodos() throws SQLException, Exception {
		List<Object> pessoas = new ArrayList<Object>();
		try {
			String sql = "SELECT * FROM quem WHERE idusuario = " + UsuarioDao.idUser;
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);

			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2), rs.getString(3) };
				pessoas.add(linha);
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return pessoas;
	}

	/**
	 * _______________________________________________ Retorna todas as informações
	 * de clientes da tabela possibilitando recuperar objetos isolados do Array
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public String[] buscarInformacoes(int codigoCliente) throws SQLException, Exception {
		String[] cliente = new String[10];
		try {
			String sql = "SELECT * FROM cliente WHERE id_cliente=?";
			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
			sqlPrep.setInt(1, codigoCliente);
			ResultSet rs = sqlPrep.executeQuery();

			while (rs.next()) {
				cliente[0] = rs.getString(10); // Id usuario (irrelevante)
				cliente[1] = rs.getString(1); // Codigo
				cliente[2] = rs.getString(2); // Nome
				cliente[3] = rs.getString(3); // Cpf
				cliente[4] = rs.getString(4); // Nascimento
				cliente[5] = rs.getString(5); // Logradouro
				cliente[6] = rs.getString(6); // Anamnese
				cliente[7] = rs.getString(7); // Observação
				cliente[8] = rs.getString(8); // Data Cadastro
				cliente[9] = rs.getString(9); // Saldo
			}
			sqlPrep.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return cliente;
	}

	/**
	 * Retorna nome das pessoas, especialmente criado para preencher comboBoxes dos
	 * frames
	 */
	public String[] buscarNomesQuem(boolean verRegistros) throws SQLException, Exception {
		String[] nomeQuem = null;
		String sql = null;
		ResultSet rs = null;
		java.sql.Statement state = null;
		int qntNomes = 0;
		int i;
		try {
			// Pega somente a relação de pessoas cadastradas na conta do usuário atualmente logado
			sql = "SELECT COUNT(nome) FROM quem WHERE idusuario = " + UsuarioDao.idUser;

			state = Conexao.conectar().createStatement();
			rs = state.executeQuery(sql);

			// Conta quantos nomes tem na tabela
			while (rs.next()) {
				qntNomes = Integer.parseInt(rs.getString(1));
			}
			rs.close();
			
			//Se verRegistros é true, significa que serão inclusas as opções para visualização de registros no vetor que irá para o comboBox
			if (verRegistros) {
				// Declara vetor com a quantidade de nomes recebida
				nomeQuem = new String[qntNomes + 4];
				nomeQuem[0] = "Todos";
				nomeQuem[1] = "Receitas";
				nomeQuem[2] = "Despesas";
				nomeQuem[3] = "________* Pessoas *_________";
				//Inicia no 4 a colocar os nomes
				i = 4;
			}else {
				nomeQuem = new String[qntNomes + 1];
				// Adiciona primeiro índice como vazio para exibir nos comboBoxes
				nomeQuem[0] = "";
				//Inicia no 1 para colocar os nomes
				i = 1;
			}
			// Essa SQL unirá o ID da pessoa ao nome no retorno vindo do banco de dados
			sql = "SELECT CONCAT_WS('*', idquem, nome, saldo) as 'resultado' FROM quem WHERE idusuario =" + UsuarioDao.idUser;

			rs = state.executeQuery(sql);
			// Passa nomes da tabela para o vetor
			while (rs.next()) {
				nomeQuem[i] = rs.getString("resultado");
				i++;
			}
			rs.close();
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, falha ao trazer nomes das pessoas do banco de dados, contate o desenvolvedor e informe o código DQ001!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return nomeQuem;
	}

	/**
	 * Buscará, na tabela Quem, as informações da pessoa com id que consta no registro com de id igual o parâmetro passado e montará a string
	 * para ficar compatível com as opções do comboBox que existe na tela de Registro.
	 * @param nomeQuem
	 * @return String do nome com dados montada
	 * @throws Exception
	 */
	public String RecuperarQuem(int codigoRegistro) throws Exception {
		String dadosQuem = "";
		try {
			// Essa SQL unirá o ID da pessoa ao nome e saldo, a partir do id do registro, montando uma string com formato exato das opções do combobox para pré-selecionar o item exato quando chamar a janela de registro.
			String sql = "SELECT CONCAT_WS('*', quem.idquem, quem.nome, quem.saldo) as 'resultado' FROM quem, registro WHERE registro.idregistro = " + codigoRegistro + " AND registro.idquem = quem.idquem";
			java.sql.Statement state = Conexao.conectar().createStatement();
			ResultSet rs = state.executeQuery(sql);
			// Joga o retorno do banco para a String
			while (rs.next()) {
				dadosQuem = rs.getString("resultado");
			}
			rs.close();
			state.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, falha ao recuperar o nome da pessoa do registro, contate o desenvolvedor e informe o código DQ002!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return dadosQuem;
	}
}
