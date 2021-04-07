package br.edu.ifc.videira.beans;

public class Quem {

	private int codigo;
	private String nome;
	private double saldo;
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
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
//			System.out.println(this.saldo);
		}
	}

	public Quem() {
	}

}
