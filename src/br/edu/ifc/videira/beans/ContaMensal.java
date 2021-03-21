package br.edu.ifc.videira.beans;

public class ContaMensal {

	private int codigo;
	private String descricao;
	private int diaVencimento;
	private int idQuem;
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getDiaVencimento() {
		return diaVencimento;
	}

	public void setDiaVencimento(int diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	public int getIdQuem() {
		return idQuem;
	}

	public void setIdQuem(int idQuem) {
		this.idQuem = idQuem;
	}

	public ContaMensal() {
	}

}
