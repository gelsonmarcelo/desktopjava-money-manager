package br.edu.ifc.videira.beans;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Registro {

	private int codigo;
	private double valor;
	private String descricao;
	private String data;
	private int idQuem;
	private String nomeQuem;
	private Double saldoQuem;
	private int idTipo;
	private int idClassificacao;
	private int idInstituicao;
	private String nomeInstituicao;
	private Double saldoInstituicao;
	private String dataInicio;
	private String dataFim;
	protected SimpleDateFormat formatoPadrao = new SimpleDateFormat("yyyy-MM-dd");
		
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = formatoPadrao.format(data);
	}

	public int getIdQuem() {
		return idQuem;
	}

	public void setIdQuem(int idQuem) {
		this.idQuem = idQuem;
	}
	
	/**
	 * 1-Receita, 2-despesa, 3-devem, 4-devo
	 */
	public int getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(int idTipo) {
		this.idTipo = idTipo;
	}

	public int getIdClassificacao() {
		return idClassificacao;
	}

	public void setIdClassificacao(int idClassificacao) {
		this.idClassificacao = idClassificacao;
	}

	public int getIdInstituicao() {
		return idInstituicao;
	}

	public void setIdInstituicao(int idInstituicao) {
		this.idInstituicao = idInstituicao;
	}

	public Registro() {
	}

	public String getNomeQuem() {
		return nomeQuem;
	}

	public void setNomeQuem(String nomeQuem) {
		this.nomeQuem = nomeQuem;
	}

	public Double getSaldoQuem() {
		return saldoQuem;
	}

	public void setSaldoQuem(Double saldoQuem) {
		this.saldoQuem = saldoQuem;
	}

	public String getNomeInstituicao() {
		return nomeInstituicao;
	}

	public void setNomeInstituicao(String nomeInstituicao) {
		this.nomeInstituicao = nomeInstituicao;
	}

	public Double getSaldoInstituicao() {
		return saldoInstituicao;
	}

	public void setSaldoInstituicao(Double saldoInstituicao) {
		this.saldoInstituicao = saldoInstituicao;
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
