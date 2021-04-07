package br.edu.ifc.videira.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifc.videira.beans.Instituicao;
import br.edu.ifc.videira.beans.Transferencia;
import br.edu.ifc.videira.utils.Conexao;

public class InstituicaoDao {
	protected java.sql.PreparedStatement sqlPrep;
	protected java.sql.Statement state;
	protected String sql;
	protected ResultSet rs;

	/** ### - Revisar todos os métodos, classe copiada
	 * Cadastra uma nova pessoa na tabela
	 * 
	 * @param in
	 * @throws SQLException
	 * @throws Exception
	 */
	public void cadastrarInstituicao(Instituicao in) throws SQLException, Exception {
		try {
			sql = "INSERT INTO instituicao (nome, saldo, idusuario, idtipo) VALUES (?,?," + UsuarioDao.idUser + ",?)";
			sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, in.getNome());
			sqlPrep.setDouble(2, in.getSaldo());
			sqlPrep.setInt(3, in.getIdTipo());
			sqlPrep.execute();
			
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
	public void atualizarInstituicao(Instituicao in) throws Exception {
		try {
			sql = "UPDATE instituicao SET nome=?, saldo=?, idtipo=? WHERE idinstituicao=?;";
			sqlPrep = Conexao.conectar().prepareStatement(sql);
			sqlPrep.setString(1, in.getNome());
			sqlPrep.setDouble(2, in.getSaldo());
			sqlPrep.setInt(3, in.getIdTipo());
			sqlPrep.setInt(4, in.getCodigo());
			sqlPrep.execute();
			
			JOptionPane.showMessageDialog(null, "Instituição atualizada com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Infelizmente ocorreu um erro inesperado, se persistir contate o desenvolvedor:\n" + e.getMessage(),
					"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Retorna todas as informações das instituições na tabela
	 * 
	 * @return instituicoes
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<Object> buscarTodos() throws SQLException, Exception {
		List<Object> instituicoes = new ArrayList<Object>();
		try {
			sql = 
					"SELECT instituicao.idinstituicao, instituicao.nome, instituicao.saldo, tipo_instituicao.nome " + 
					"FROM instituicao, tipo_instituicao " + 
					"WHERE instituicao.idtipo = tipo_instituicao.idtipoinstituicao AND instituicao.idusuario = " + UsuarioDao.idUser;
			state = Conexao.conectar().createStatement();
			rs = state.executeQuery(sql);

			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2),  rs.getString(3), rs.getString(4)};
				instituicoes.add(linha);
			}
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return instituicoes;
	}
	
//	/**_______________________________________________
//	 * Retorna todas as informações de clientes da tabela possibilitando recuperar
//	 * objetos isolados do Array
//	 * 
//	 * @return
//	 * @throws SQLException
//	 * @throws Exception
//	 */
//	public String[] buscarInformacoes(int codigoCliente) throws SQLException, Exception {
//		String[] cliente = new String[10];
//		try {
//			sql = "SELECT * FROM cliente WHERE id_cliente=?";
//			PreparedStatement sqlPrep = (PreparedStatement) Conexao.conectar().prepareStatement(sql);
//			sqlPrep.setInt(1, codigoCliente);
//			rs = sqlPrep.executeQuery();
//
//			while (rs.next()) {
//				cliente[0] = rs.getString(10); // Id usuario (irrelevante)
//				cliente[1] = rs.getString(1); // Codigo
//				cliente[2] = rs.getString(2); // Nome
//				cliente[3] = rs.getString(3); // Cpf
//				cliente[4] = rs.getString(4); // Nascimento
//				cliente[5] = rs.getString(5); // Logradouro
//				cliente[6] = rs.getString(6); // Anamnese
//				cliente[7] = rs.getString(7); // Observação
//				cliente[8] = rs.getString(8); // Data Cadastro
//				cliente[9] = rs.getString(9); // Saldo
//			}
//			sqlPrep.close();
//
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(null, e.getMessage());
//		}
//		return cliente;
//	}

	/**
	 * Realiza transferência de valores entre instituições.
	 * @param tr
	 * @throws Exception
	 */
	public void transferir(Transferencia tr) throws Exception {
		try {
			PreparedStatement sqlPrepDesconta = Conexao.conectar().prepareStatement("UPDATE instituicao SET saldo = saldo - ? WHERE idinstituicao = ?");
			PreparedStatement sqlPrepSoma = Conexao.conectar().prepareStatement("UPDATE instituicao SET saldo = saldo + ? WHERE idinstituicao = ?");
			
			sqlPrepDesconta.setDouble(1, tr.getValor());
			sqlPrepDesconta.setInt(2, tr.getIdInstituicaoOrigem());
			sqlPrepDesconta.execute();

			sqlPrepSoma.setDouble(1, tr.getValor());
			sqlPrepSoma.setInt(2, tr.getIdInstituicaoDestino());
			sqlPrepSoma.execute();
						
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Infelizmente ocorreu um erro inesperado, se persistir contate o desenvolvedor informando o código IN005.\n" + e.getMessage(),
					"Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(null, "Transferência realizada com sucesso!");
	}
	
	/**
	 * Retorna nome das instituições, especialmente criado para preencher comboBoxes dos
	 * frames
	 */
	public String[] buscarNomesInstituicao() throws SQLException, Exception {
		String[] nomeInstituicao = null;
		int qntNomes = 0;
		int i = 1;
		try {
			//Pega somente a relação de pessoas cadastradas na conta do usuário atualmente logado
			sql = "SELECT COUNT(nome) FROM instituicao WHERE idusuario = " + UsuarioDao.idUser;
			state = Conexao.conectar().createStatement();
			rs = state.executeQuery(sql);

			// Conta quantos nomes tem na tabela
			while (rs.next()) {
				qntNomes = Integer.parseInt(rs.getString(1));
			}
			rs.close();

			// Declara vetor com a quantidade de nomes recebida
			nomeInstituicao = new String[qntNomes + 1];

			// Adiciona primeiro indice como vazio para exibir nos comboBoxes
			nomeInstituicao[0] = "";

			//Essa SQL unirá o ID da instituicao ao nome no retorno vindo do banco de dados
			sql = "SELECT CONCAT_WS('*', idinstituicao, nome, saldo) as 'resultado' FROM instituicao WHERE idusuario =" + UsuarioDao.idUser;
			rs = state.executeQuery(sql);
			// Passa nomes da tabela para o vector
			while (rs.next()) {
				nomeInstituicao[i] = rs.getString("resultado");
				i++;
			}
			rs.close();
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return nomeInstituicao;
	}
	
	/**
	 * Buscará, na tabela Instituicao, as informações (id, nome e saldo) da classificação com nome igual o parâmetro passado e montará a string
	 * para ficar compatível com as opções do comboBox que existe na tela de Registro.
	 * @param nomeInstituicao
	 * @return String do nome com dados montada
	 * @throws Exception
	 */
	public String RecuperarInstituicao(int codigoRegistro) throws Exception {
		String dadosInstituicao = "";
		try {
			// Essa SQL unirá o ID da pessoa ao nome e saldo, a partir do id do registro, montando uma string com formato exato das opções do combobox para pré-selecionar o item exato quando chamar a janela de registro.
			sql = "SELECT CONCAT_WS('*', instituicao.idinstituicao, instituicao.nome, instituicao.saldo) as 'resultado' FROM instituicao, registro WHERE registro.idregistro = " + codigoRegistro + " AND registro.idinstituicao = instituicao.idinstituicao;";
			state = Conexao.conectar().createStatement();
			rs = state.executeQuery(sql);
			// Joga o retorno do banco para a String
			while (rs.next()) {
				dadosInstituicao = rs.getString("resultado");
			}
			rs.close();
			state.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, falha ao recuperar instituição do registro, contate o desenvolvedor e informe o código ''!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return dadosInstituicao;
	}
	
	/**
	 * Buscará, na tabela Instituicao, as informações (id, nome e saldo) da classificação com nome igual o parâmetro passado e montará a string
	 * para ficar compatível com as opções do comboBox que existe na tela de Registro.
	 * @param nomeInstituicao
	 * @return String do nome com dados montada
	 * @throws Exception
	 */
	public String RecuperarTipoInstituicao(String nomeTipo) throws Exception {
		String dadosTipo = "";
		try {
			// Essa SQL unirá o ID da pessoa ao nome e saldo, a partir do id do registro, montando uma string com formato exato das opções do combobox para pré-selecionar o item exato quando chamar a janela de registro.
			sql = "SELECT CONCAT_WS('*', tipo_instituicao.idtipoinstituicao, tipo_instituicao.nome) as 'resultado' " + 
					"FROM tipo_instituicao " + 
					"WHERE tipo_instituicao.nome like '" + nomeTipo + "';";
			state = Conexao.conectar().createStatement();
			rs = state.executeQuery(sql);
			// Joga o retorno do banco para a String
			while (rs.next()) {
				dadosTipo = rs.getString("resultado");
			}
			rs.close();
			state.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, falha ao recuperar tipo de instituição do registro, contate o desenvolvedor e informe o código ''!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return dadosTipo;
	}
	
	/**
	 * Retorna nome das classificações, especialmente criado para preencher comboBoxes dos
	 * frames
	 */
	public String[] buscarTipos() throws SQLException, Exception {
		String[] nomeTipo = null;
		int qntNomes = 0;
		int i = 1;
		try {
			sql = "SELECT COUNT(nome) FROM tipo_instituicao";
			state = Conexao.conectar().createStatement();
			rs = state.executeQuery(sql);

			// Conta quantos nomes tem na tabela
			while (rs.next()) {
				qntNomes = Integer.parseInt(rs.getString(1));
			}
			rs.close();

			// Declara vetor com a quantidade de nomes recebida
			nomeTipo = new String[qntNomes + 1];

			// Adiciona primeiro indice como vazio para exibir nos comboBoxes
			nomeTipo[0] = "";

			//Essa SQL unirá o ID do tipo ao nome no retorno vindo do banco de dados
			sql = "SELECT CONCAT_WS('*', idtipoinstituicao, nome) as 'resultado' FROM tipo_instituicao";
			rs = state.executeQuery(sql);
			// Passa nomes da tabela para o vetor
			while (rs.next()) {
				nomeTipo[i] = rs.getString("resultado");
				i++;
			}
			rs.close();
			state.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return nomeTipo;
	}
	
	/**
	 * Exclui um registro de conta
	 * @param rg
	 * @param voltarValor
	 * @param operacao
	 */
	public void deletarInstituicao(Instituicao in){
		//Isso executará de qualquer forma, pois é onde exclui o registro
		//Código que irá executar no banco
		sql = "DELETE FROM instituicao WHERE idinstituicao = ?";
		try {
			//Declaração do PreparedStatement com sql
			sqlPrep = Conexao.conectar().prepareStatement(sql);
			//Definindo os valores ? da SQL
			sqlPrep.setInt(1, in.getCodigo());
			//Executando Query
			sqlPrep.execute();
			JOptionPane.showMessageDialog(null, "Exlusão do instituição realizada com sucesso!");
			sqlPrep.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro, instituição pode não ter sido deletada, contate o desenvolvedor e informe o código ''!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);			
			e.printStackTrace();
		}
	}

}
