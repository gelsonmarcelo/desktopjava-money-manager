package br.edu.ifc.videira.beans;

public class Instituicao {

	private int codigo;
	private String nome;
	private double saldo;
	private int idTipo;
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = Integer.parseInt(codigo);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo, boolean saldoNegativo) {
		this.saldo = Double.parseDouble(saldo.replaceAll("\\.", "").replaceAll(",", ".").replace("R$ ", ""));
		if(saldoNegativo) {
			this.saldo*=-1;
		}
	}

	public int getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(String idTipo) {
		this.idTipo = Integer.parseInt(idTipo.split("\\*")[0]);
	}

	public Instituicao() {
	}

}
