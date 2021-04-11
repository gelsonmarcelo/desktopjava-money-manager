package br.edu.ifc.videira.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	private int codigo;
	private String operacao;
	private String descricao;
	private int usuario;
	private String dataInicio;
	private String dataFim;
	protected SimpleDateFormat formatoPadrao = new SimpleDateFormat("yyyy-MM-dd");
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getOperacao() {
		return operacao;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getUsuario() {
		return usuario;
	}
	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}
	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		//Converte do formato Date para String e define o valor na variável, já no formato para ir até o banco
		this.dataInicio = formatoPadrao.format(dataInicio);
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		//Converte do formato Date para String e define o valor na variável, já no formato para ir até o banco
		this.dataFim = formatoPadrao.format(dataFim);
	}
}
