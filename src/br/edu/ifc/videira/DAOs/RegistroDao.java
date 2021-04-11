package br.edu.ifc.videira.DAOs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.edu.ifc.videira.beans.Registro;
import br.edu.ifc.videira.utils.Conexao;

public class RegistroDao {

	/**
	 * ### - Revisar todos os métodos, classe copiada Realizar um novo registro
	 * 
	 * @param re
	 * @throws SQLException
	 * @throws Exception
	 */
	final SimpleDateFormat formatoAmericano = new SimpleDateFormat("yyyy-MM-dd");
	final SimpleDateFormat formatoBrasileiro = new SimpleDateFormat("dd-MM-yyyy");
	protected java.sql.PreparedStatement sqlPrep;
	protected java.sql.Statement state;
	protected ResultSet rs;
	protected String sql;

	public void registrar(Registro re, int codigoEdicao, String valorRegistroAnterior) throws SQLException, Exception {
		// Receber escolhas dos ConfirmDialog
		int respostaDialog;
		// Servirá para passar os acontecimentos prévios ao usuário.
		String mensagem = "";
		// Pegar momento do registro
		LocalDateTime momento = LocalDateTime.now();

		// Insere as informações
		try {
			// **Aqui apenas realiza o registro e reverte o valor anterior do registro em
			// caso de edição**
			// Se codigoEdicao é = -1 significa que é um novo registro, caso contrário é uma
			// edição
			if (codigoEdicao == -1) {
				sql = "INSERT INTO registro (valor, descricao, data, momento_registro, idusuario, idquem, idtipo, idclassificacao, idinstituicao) VALUES (?,?,?,?,?,?,?,?,?)";
			} else {
				// Reverte valor que está sendo editado na instituição.
				if (re.getIdTipo() == 1/* || re.getIdTipo() == 4 */) {
					// Query que reverte da instituicao atual o valor atual do registro sendo
					// executada
					sqlPrep = Conexao.conectar().prepareStatement(
							"UPDATE instituicao, registro SET instituicao.saldo = instituicao.saldo - registro.valor WHERE registro.idregistro = "
									+ codigoEdicao + " AND registro.idinstituicao = instituicao.idinstituicao");
					sqlPrep.execute();
					sqlPrep.close();
					mensagem = "> O valor de " + valorRegistroAnterior
							+ " (que estava registrado anteriormente à edição) foi revertido (subtraído) da instituição definida antes da edição do registro.";
				} else if (re.getIdTipo() == 2/* || re.getIdTipo() == 4 */) {
					// Query que reverte da instituicao atual o valor atual do registro sendo
					// executada
					sqlPrep = Conexao.conectar().prepareStatement(
							"UPDATE instituicao, registro SET instituicao.saldo = instituicao.saldo + registro.valor WHERE registro.idregistro = "
									+ codigoEdicao + " AND registro.idinstituicao = instituicao.idinstituicao");
					sqlPrep.execute();
					sqlPrep.close();
					mensagem = "> O valor de " + valorRegistroAnterior
							+ " (que estava registrado anteriormente à edição) foi revertido (somado) da instituição definida antes da edição do registro.";
				}
				// Define a query de edição do registro
				sql = "UPDATE registro SET valor = ?, descricao = ?, data = ?, momento_registro = ?, idusuario = ?, idquem = ?, idtipo = ?, idclassificacao = ?, idinstituicao = ? WHERE idregistro = "
						+ codigoEdicao;
			}
			sqlPrep = Conexao.conectar().prepareStatement(sql);
			int contador = 1;
			sqlPrep.setDouble(contador++, re.getValor());
			sqlPrep.setString(contador++, re.getDescricao());
			sqlPrep.setString(contador++, re.getData());
			sqlPrep.setString(contador++, momento.toString());
			sqlPrep.setInt(contador++, UsuarioDao.idUser);
			sqlPrep.setInt(contador++, re.getIdQuem());
			sqlPrep.setInt(contador++, re.getIdTipo());
			sqlPrep.setInt(contador++, re.getIdClassificacao());
			sqlPrep.setInt(contador++, re.getIdInstituicao());
			sqlPrep.execute();

			// **Realiza as ações consequentes da operação escolhida**
			int repetirLoop = 0; // funções serão reaproveitadas na volta do loop em caso do programa precisar
									// fazer o registro e mais de uma ação.
			/**
			 * 0 = não repetir; 1 = primeira repetição 2 = já repetiu 1 vez (máx, então
			 * repetirLoop = 2 para sair)
			 */
			do {
				respostaDialog = 0;
				// Escolhe qual tipo de registro para determinar a operação a fazer
				switch (re.getIdTipo()) {
				// Receita
				case 1:
					// Verifica se é o primeiro loop para perguntar, caso não seja é porque a opção
					// primária selecionada já perguntou se ela queria executar (essa) operação
					// secundária.
					if (repetirLoop == 0) {
						// Verificar se o "quem" escolhido possui saldo de dívida diferente de zero
						if (re.getSaldoQuem() != 0) {
							respostaDialog = JOptionPane.showConfirmDialog(null,
									"Foi verificado que você e '" + re.getNomeQuem() + "' "
											+ "possuem valores pendentes a acertar!" + "\nDeseja extrair R$"
											+ re.getValor() + " do saldo de '" + re.getNomeQuem() + "'?"
											+ "\n\n > Ao final da operação os valores serão:\n > " + re.getNomeQuem()
											+ ": " + re.getSaldoQuem() + " - " + re.getValor() + " = "
											+ (re.getSaldoQuem() - re.getValor()) + "" + "\n > Meu saldo: "
											+ re.getSaldoInstituicao() + " + " + re.getValor() + " = "
											+ (re.getSaldoInstituicao() + re.getValor()) + "",
									"Atualização de Dívida em Aberto", JOptionPane.YES_NO_OPTION);
							// Ações a partir da escolha na resposta do usuário:
							switch (respostaDialog) {
							// Resposta = sim, então o id do tipo de registro será alterado, voltando no
							// loop, para fazer a função de subtração no saldo do "quem".
							case 0:
								re.setIdTipo(4);
								repetirLoop++;
								break;
							// Resposta diferente de sim, o sistema apenas realizará o registro e atualizar
							// o valor da instituição
							default:
								repetirLoop = 2;
								break;
							}
						} else {
							// Nesse caso vai pular direto para sair do loop
							repetirLoop = 2;
						}
					} else {
						// Nesse caso vai pular direto para sair do loop quando sair do switch
						repetirLoop = 2;
					}
					/** Atualiza saldo na instituição **/
					sql = "UPDATE instituicao SET saldo = saldo + ? WHERE idinstituicao = ?";
					sqlPrep = Conexao.conectar().prepareStatement(sql);
					// Definindo os valores da SQL
					sqlPrep.setDouble(1, re.getValor());
					sqlPrep.setInt(2, re.getIdInstituicao());
					// Executando Query
					sqlPrep.execute();
					sqlPrep.close();
					JOptionPane.showMessageDialog(null, "Registro de receita realizado com sucesso!\n" + mensagem
							+ "\n> Receita de R$+" + re.getValor() + " para '" + re.getNomeInstituicao() + "'.");
					break;
				// Despesa
				case 2:
					// Verifica se é o primeiro loop para perguntar, caso não seja é porque a opção
					// primária selecionada já perguntou se ela queria executar (essa) operação
					// secundária.
					if (repetirLoop == 0) {
						// Verificar se o "quem" escolhido possui saldo de dívida diferente de zero
						if (re.getSaldoQuem() != 0) {
							respostaDialog = JOptionPane.showConfirmDialog(null,
									"Foi verificado que você e '" + re.getNomeQuem() + "' "
											+ "possuem valores pendentes a acertar!" + "\nDeseja incluir R$"
											+ re.getValor() + " no saldo de '" + re.getNomeQuem() + "'?"
											+ "\n\n > Ao final da operação os valores serão:\n > " + re.getNomeQuem()
											+ ": " + re.getSaldoQuem() + " + " + re.getValor() + " = "
											+ (re.getSaldoQuem() + re.getValor()) + "" + "\n > Meu saldo: "
											+ re.getSaldoInstituicao() + " - " + re.getValor() + " = "
											+ (re.getSaldoInstituicao() - re.getValor()) + "");
							// Ações a partir da escolha na resposta do usuário:
							switch (respostaDialog) {
							// Resposta = sim, então o id do tipo de registro será alterado, voltando no
							// loop, para fazer a função de adição no saldo do "quem".
							case 0:
								re.setIdTipo(3);
								repetirLoop++;
								break;
							// Resposta diferente de sim, o sistema apenas realizará o registro e atualizar
							// o valor da instituição
							default:
								repetirLoop = 2;
								break;
							}
						} else {
							// Nesse caso vai pular direto para sair do loop
							repetirLoop = 2;
						}
					} else {
						// Nesse caso vai pular direto para sair do loop quando sair do switch
						repetirLoop = 2;
					}
					/** Atualiza saldo na instituição **/
					sql = "UPDATE instituicao SET saldo = saldo - ? WHERE idinstituicao = ?";
					sqlPrep = Conexao.conectar().prepareStatement(sql);
					// Definindo os valores da SQL
					sqlPrep.setDouble(1, re.getValor());
					sqlPrep.setInt(2, re.getIdInstituicao());
					// Executando Query
					sqlPrep.execute();
					sqlPrep.close();
					JOptionPane.showMessageDialog(null, "Registro de despesa realizado com sucesso!\n> " + mensagem
							+ "\n> Despesa de R$-" + re.getValor() + " para '" + re.getNomeInstituicao() + "'.");
					break;
				// Está me devendo +(o "quem" escolhido terá o valor registrado como dívida a
				// favor do usuário)
				case 3:
					// Verifica se é o primeiro loop para perguntar, caso não seja é porque a opção
					// primária selecionada já perguntou se ela queria executar (essa) operação
					// secundária.
					if (repetirLoop == 0) {
						respostaDialog = JOptionPane.showConfirmDialog(null,
								"Esse valor foi retirado do seu bolso para emprestar para '" + re.getNomeQuem() + "'?"
										+ "\nDeseja descontar o valor de R$" + re.getValor() + " do saldo da '"
										+ re.getNomeInstituicao() + "'?"
										+ "\n\n > Ao final da operação os valores serão:\n > " + re.getNomeQuem() + ": "
										+ re.getSaldoQuem() + " + " + re.getValor() + " = "
										+ (re.getSaldoQuem() + re.getValor()));
						// Ações a partir da escolha na resposta do usuário:
						switch (respostaDialog) {
						// Resposta = sim, então o id do tipo de registro será alterado, voltando no
						// loop, para fazer a função de adição no saldo do "quem".
						case 0:
							re.setIdTipo(2);
							break;
						// Resposta diferente de sim, o sistema apenas realizará o registro e atualizar
						// o saldo de "quem"
						default:
							repetirLoop = 2;
							break;
						}
					}
					// Atualiza saldo de dívida da pessoa
					sql = "UPDATE quem SET saldo = saldo + ? WHERE idquem = ?";
					sqlPrep = Conexao.conectar().prepareStatement(sql);
					// Definindo os valores da SQL
					sqlPrep.setDouble(1, re.getValor());
					sqlPrep.setInt(2, re.getIdQuem());
					// Executando Query
					sqlPrep.execute();
					JOptionPane.showMessageDialog(null, "Atualização no saldo de '" + re.getNomeQuem() + "' somando R$+"
							+ re.getValor() + " registrada com sucesso!");
					// Caso a repetirLoop esteja ativado, vai parar de repetir aqui
					repetirLoop++;
					break;
				// Estou devendo -(o "quem" escolhido terá o valor registrado como dívida contra
				// o usuário)
				case 4:
					// Verifica se é o primeiro loop para perguntar, caso não seja é porque a opção
					// primária selecionada já perguntou se ela queria executar (essa) operação
					// secundária.
					if (repetirLoop == 0) {
						respostaDialog = JOptionPane.showConfirmDialog(null,
								"Esse valor foi adicionado no seu bolso quando emprestou de '" + re.getNomeQuem() + "'?"
										+ "\nDeseja somar o valor de R$" + re.getValor() + " no saldo da '"
										+ re.getNomeInstituicao() + "'?"
										+ "\n\n > Ao final da operação os valores serão:\n > " + re.getNomeQuem() + ": "
										+ re.getSaldoQuem() + " - " + re.getValor() + " = "
										+ (re.getSaldoQuem() - re.getValor()));
						// Ações a partir da escolha na resposta do usuário:
						switch (respostaDialog) {
						// Resposta = sim, então o id do tipo de registro será alterado, voltando no
						// loop, para fazer a função de adição no saldo do "quem".
						case 0:
							re.setIdTipo(1);
							break;
						// Resposta diferente de sim, o sistema apenas realizará o registro e atualizar
						// o saldo de "quem"
						default:
							repetirLoop = 2;
							break;
						}
					}
					// Atualiza saldo de dívida da pessoa
					sql = "UPDATE quem SET saldo = saldo - ? WHERE idquem = ?";
					sqlPrep = Conexao.conectar().prepareStatement(sql);
					// Definindo os valores da SQL
					sqlPrep.setDouble(1, re.getValor());
					sqlPrep.setInt(2, re.getIdQuem());
					// Executando Query
					sqlPrep.execute();
					JOptionPane.showMessageDialog(null, "Atualização no saldo de '" + re.getNomeQuem()
							+ "' extraindo R$-" + re.getValor() + " registrada com sucesso!");
					// Caso a repetirLoop esteja ativado, vai parar de repetir aqui
					repetirLoop++;
					break;
				default:
					JOptionPane.showMessageDialog(null,
							"Ocorreu um erro, teoricamente seria impossível você chegar aqui!, contate o desenvolvedor e informe o código 'DR002'!",
							"Erro inesperado", JOptionPane.ERROR_MESSAGE);
					break;
				}
			} while (repetirLoop < 2); // Se for 2 significa que já passou uma repetição no loop, deve sair
			// Fechar o Prepared Statement
			sqlPrep.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, é possível que as informações não tenham sido registradas corretamente, contate o desenvolvedor e informe o código 'DR001'!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Altera o estado da conta (pago ou não pago)
	 * 
	 * @param co
	 * @throws Exception
	 *
	 *             public void atualizarEstadoConta(Contas co) throws Exception {
	 *             try { String sql = "UPDATE contas SET status=? WHERE id_conta=?";
	 *             PreparedStatement sqlPrep =
	 *             Conexao.conectar().prepareStatement(sql); int contador = 1;
	 *             sqlPrep.setBoolean(contador++, co.isStatus());
	 *             sqlPrep.setInt(contador++, co.getCodigo()); sqlPrep.execute(); }
	 *             catch (Exception e) { JOptionPane.showMessageDialog(null,
	 *             e.getMessage()); } }
	 */

	/**
	 * Atualizar informações de um registro de conta
	 * 
	 * @param co
	 * @throws Exception
	 * 
	 *             public void atualizarConta(Contas co) throws Exception { try {
	 *             String sql = "UPDATE contas SET descricao=?, data_vencimento=?,
	 *             valor=?, status=?, id_usuario_contas=? WHERE id_conta=?";
	 *             PreparedStatement sqlPrep =
	 *             Conexao.conectar().prepareStatement(sql); int contador = 1;
	 *             sqlPrep.setString(contador++, co.getDescricao());
	 *             sqlPrep.setString(contador++, co.getDataVencimento());
	 *             sqlPrep.setDouble(contador++, co.getValor());
	 *             sqlPrep.setBoolean(contador++, co.isStatus());
	 *             sqlPrep.setInt(contador++, Login.idUser);
	 *             sqlPrep.setInt(contador++, co.getCodigo()); sqlPrep.execute(); }
	 *             catch (Exception e) { JOptionPane.showMessageDialog(null,
	 *             e.getMessage()); } }
	 */

	/**
	 * Exclui um registro de conta
	 * 
	 * @param rg
	 * @param voltarValor
	 * @param operacao
	 */
	public void deletarRegistro(Registro rg, int voltarValor, String operacao) {
		// Usuário escolheu sim, quer que o valor do registro volte para a instituicao
		if (voltarValor == 0) {
			if (operacao.equals("Receita")/* || operacao.equals("Estou devendo") */) {
				// Se a operação era de Receita|Estou devendo, então havia sido incrementado
				// valor na instituição, agora será removido
				operacao = "-";
			} else if (operacao.equals("Despesa")/* || operacao.equals("Está me devendo") */) {
				// Se a operação era de Despesa|Está me devendo, então havia sido subtraído
				// valor na instituição, agora será adicionado
				operacao = "+";
			} else {
				JOptionPane.showMessageDialog(null,
						"Ocorreu um erro, o tipo de registro detectado não é compatível com os permitidos para a operação, contate o desenvolvedor e informe o código DR006!",
						"Erro inesperado", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Código que irá executar no banco
			sql = "UPDATE instituicao, registro SET instituicao.saldo = instituicao.saldo " + operacao
					+ " registro.valor WHERE instituicao.idinstituicao = registro.idinstituicao AND registro.idregistro = ?";
			try {
				// Definição de valores do PreparedStatement com sql
				sqlPrep = Conexao.conectar().prepareStatement(sql);
				// Definindo os valores ? da SQL
				sqlPrep.setInt(1, rg.getCodigo());
				// Executando Query
				sqlPrep.execute();
				JOptionPane.showMessageDialog(null, "O valor foi revertido de volta para a instituição!");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"Ocorreu um erro, o valor pode não ter sido revertido para a insituição, contate o desenvolvedor e informe o código DR005!",
						"Erro inesperado", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		// Isso executará de qualquer forma, pois é onde exclui o registro
		// Código que irá executar no banco
		sql = "DELETE FROM registro WHERE idregistro = ?";
		try {
			// Declaração do PreparedStatement com sql
			sqlPrep = Conexao.conectar().prepareStatement(sql);
			// Definindo os valores ? da SQL
			sqlPrep.setInt(1, rg.getCodigo());
			// Executando Query
			sqlPrep.execute();
			JOptionPane.showMessageDialog(null, "Exlusão do registro realizada com sucesso!");
			sqlPrep.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, registro pode não ter sido deletado, contate o desenvolvedor e informe o código DR007!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Busca todas as informações (receitas e despesas) da tabela de registros
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 **/
	public List<Object> buscarRegistros(String tipoRegistro) throws SQLException, Exception {
		List<Object> registros = new ArrayList<Object>();
		try {
			// Seleciona qual consulta realizar através do tipo de registro que o usuário
			// está buscando
			switch (tipoRegistro) {
			case "Todos":
				sql = "SELECT idregistro, tipo_registro.nome, valor, quem.nome, descricao, data, instituicao.nome, classificacao.nome "
						+ "FROM registro, tipo_registro, quem, classificacao, instituicao "
						+ "WHERE registro.idusuario = " + UsuarioDao.idUser
						+ " AND (registro.idtipo=1 or registro.idtipo=2) "
						+ "AND tipo_registro.idtiporegistro=registro.idtipo " + "AND quem.idquem=registro.idquem "
						+ "AND classificacao.idclassificacao=registro.idclassificacao "
						+ "AND instituicao.idinstituicao=registro.idinstituicao " + "ORDER  BY data DESC";
				break;
			case "Receitas":
				sql = "SELECT idregistro, \"Receita\", valor, quem.nome, descricao, data, instituicao.nome, classificacao.nome "
						+ "FROM registro, quem, classificacao, instituicao " + "WHERE registro.idusuario ="
						+ UsuarioDao.idUser + " AND registro.idtipo=1 " + "AND quem.idquem=registro.idquem "
						+ "AND classificacao.idclassificacao=registro.idclassificacao "
						+ "AND instituicao.idinstituicao=registro.idinstituicao " + "ORDER  BY data DESC";
				break;
			case "Despesas":
				sql = "SELECT idregistro, \"Despesa\", valor, quem.nome, descricao, data, instituicao.nome, classificacao.nome "
						+ "FROM registro, quem, classificacao, instituicao " + "WHERE registro.idusuario ="
						+ UsuarioDao.idUser + " AND registro.idtipo=2 " + "AND quem.idquem=registro.idquem "
						+ "AND classificacao.idclassificacao=registro.idclassificacao "
						+ "AND instituicao.idinstituicao=registro.idinstituicao " + "ORDER  BY data DESC";
				break;
			default:
				String idQuem = String.valueOf(tipoRegistro.split("\\*")[0]);
				// idQuem diferente dessa linha é porque o usuário selecionou uma pessoa da
				// lista para exibir os registros
				if (!idQuem.equals("________")) {
					sql = "SELECT idregistro, tipo_registro.nome, valor, quem.nome, descricao, data, instituicao.nome, classificacao.nome "
							+ "FROM registro, tipo_registro, quem, classificacao, instituicao "
							+ "WHERE registro.idquem =" + idQuem + " AND registro.idusuario =" + UsuarioDao.idUser
							+ " AND quem.idquem=registro.idquem "
							+ "AND registro.idtipo = tipo_registro.idtiporegistro "
							+ "AND classificacao.idclassificacao=registro.idclassificacao "
							+ "AND instituicao.idinstituicao=registro.idinstituicao " + "ORDER BY data DESC;";
				} else { // Se idQuem é igual essa linha é a divisória entre as 3 opções padrões e as
							// pessoas das dívidas, ai se a pessoa seleciona essa linha, o programa
							// retornará todos os registros
					sql = "SELECT idregistro, tipo_registro.nome, valor, quem.nome, descricao, data, instituicao.nome, classificacao.nome "
							+ "FROM registro, tipo_registro, quem, classificacao, instituicao "
							+ "WHERE registro.idusuario = " + UsuarioDao.idUser
							+ " AND (registro.idtipo=1 or registro.idtipo=2) "
							+ "AND tipo_registro.idtiporegistro=registro.idtipo " + "AND quem.idquem=registro.idquem "
							+ "AND classificacao.idclassificacao=registro.idclassificacao "
							+ "AND instituicao.idinstituicao=registro.idinstituicao " + "ORDER  BY data DESC";
				}
				// JOptionPane.showMessageDialog(null, "Ocorreu um erro, teoricamente seria
				// impossível acessar esse caminho, contate o desenvolvedor e informe o código
				// DR003!", "Erro inesperado", JOptionPane.ERROR_MESSAGE);
				// sql = "SELECT \"ERRO\", \"DR003\", \"ERRO\", \"DR003\", \"ERRO\", \"DR003\",
				// \"ERRO\", \"DR003\"";
				break;
			}
			// Conecta no banco e executa a query
			state = Conexao.conectar().createStatement();
			rs = state.executeQuery(sql);
			// Percorre pelos valores do ResultSet, adicionando as linhas de com valores
			// coletados no ArrayList
			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2), "R$ " + rs.getString(3).replaceAll("\\.", ","),
						// replaceAll("\\.",",") =se deixar apenas o . ao invés de \\. o compilador
						// considera o . como um Metacharacter do Regex substituindo todos os caracteres
						// da String
						rs.getString(4), rs.getString(5),
						formatoBrasileiro.format(formatoAmericano.parse(rs.getString(6))),
						// O formato Americano é para transformar em date antes de conseguir converter
						// para brasileiro (não da para transformar e converter ao mesmo tempo)
						rs.getString(7), rs.getString(8) };
				registros.add(linha);
			}
			state.close();
			rs.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, não foi possível consultar as informações corretamente no banco de dados, contate o desenvolvedor e informe o código 'DR004'!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return registros;
	}

	/**
	 * Busca todas os registros compreendidos entre o periodo de datas passado,
	 * conforme a seleção do tipo de registro definida da tabela
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 **/
	public List<Object> buscarRegistrosPeriodo(String tipoRegistro, String dataInicio, String dataFim)
			throws SQLException, Exception {
		List<Object> registros = new ArrayList<Object>();
		try {
			// Seleciona qual consulta realizar através do tipo de registro que o usuário
			// está buscando
			switch (tipoRegistro) {
			case "Todos":
				sql = "SELECT idregistro, tipo_registro.nome, valor, quem.nome, descricao, data, instituicao.nome, classificacao.nome "
						+ "FROM registro, tipo_registro, quem, classificacao, instituicao "
						+ "WHERE registro.idusuario = " + UsuarioDao.idUser
						+ " AND (registro.idtipo=1 or registro.idtipo=2) "
						+ "AND tipo_registro.idtiporegistro=registro.idtipo " + "AND quem.idquem=registro.idquem   "
						+ "AND classificacao.idclassificacao=registro.idclassificacao "
						+ "AND instituicao.idinstituicao=registro.idinstituicao " + "AND (registro.data between '"
						+ dataInicio + "' AND '" + dataFim + "') " + "ORDER BY data DESC";
				break;
			case "Receitas":
				sql = "SELECT idregistro, \"Receita\", valor, quem.nome, descricao, data, instituicao.nome, classificacao.nome "
						+ "FROM registro, quem, classificacao, instituicao " + "WHERE registro.idusuario ="
						+ UsuarioDao.idUser + " AND registro.idtipo=1 " + "AND quem.idquem=registro.idquem "
						+ "AND classificacao.idclassificacao=registro.idclassificacao "
						+ "AND instituicao.idinstituicao=registro.idinstituicao " + "AND (registro.data between '"
						+ dataInicio + "' AND '" + dataFim + "') " + "ORDER  BY data DESC";
				break;
			case "Despesas":
				sql = "SELECT idregistro, \"Despesa\", valor, quem.nome, descricao, data, instituicao.nome, classificacao.nome "
						+ "FROM registro, quem, classificacao, instituicao " + "WHERE registro.idusuario ="
						+ UsuarioDao.idUser + " AND registro.idtipo=2 " + "AND quem.idquem=registro.idquem "
						+ "AND classificacao.idclassificacao=registro.idclassificacao "
						+ "AND instituicao.idinstituicao=registro.idinstituicao " + "AND (registro.data between '"
						+ dataInicio + "' AND '" + dataFim + "') " + "ORDER  BY data DESC";
				break;
			default:
				tipoRegistro = String.valueOf(tipoRegistro.split("\\*")[1]);
				sql = "SELECT idregistro, tipo_registro.nome, valor, quem.nome, descricao, data, instituicao.nome, classificacao.nome "
						+ "FROM registro, tipo_registro, quem, classificacao, instituicao "
						+ "WHERE registro.idusuario = " + UsuarioDao.idUser + " AND quem.nome = '" + tipoRegistro
						+ "' AND tipo_registro.idtiporegistro=registro.idtipo " + "AND quem.idquem=registro.idquem "
						+ "AND classificacao.idclassificacao=registro.idclassificacao "
						+ "AND instituicao.idinstituicao=registro.idinstituicao " + "AND (registro.data between '"
						+ dataInicio + "' AND '" + dataFim + "') " + "ORDER BY data DESC";
				break;
			}
			// Conecta no banco e executa a query
			state = Conexao.conectar().createStatement();
			rs = state.executeQuery(sql);
			// Percorre pelos valores do ResultSet, adicionando as linhas de com valores
			// coletados no ArrayList
			while (rs.next()) {
				Object[] linha = { rs.getString(1), rs.getString(2), "R$ " + rs.getString(3).replaceAll("\\.", ","),
						rs.getString(4), rs.getString(5),
						formatoBrasileiro.format(formatoAmericano.parse(rs.getString(6))),
						rs.getString(7), rs.getString(8) };
				registros.add(linha);
			}
			state.close();
			rs.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro, não foi possível consultar as informações corretamente no banco de dados, contate o desenvolvedor e informe o código 'DR004.1'!",
					"Erro inesperado", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return registros;
	}

	/**
	 * Apenas retorna qual será o próximo código de conta
	 * 
	 * @return
	 * @throws Exception
	 * 
	 *             public int RetornarProximoCodigoConta() throws Exception { try {
	 *             String sql = "SELECT MAX(id_conta)+1 AS id_conta FROM contas";
	 *             PreparedStatement sqlPrep =
	 *             Conexao.conectar().prepareStatement(sql); ResultSet rs =
	 *             sqlPrep.executeQuery(); if (rs.next()) { return
	 *             rs.getInt("id_conta"); } else { return 1; } } catch (Exception e)
	 *             { JOptionPane.showMessageDialog(null, e.getMessage()); return 1;
	 *             } }
	 */
}
