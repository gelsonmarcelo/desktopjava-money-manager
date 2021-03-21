package br.edu.ifc.videira.beans;

public class Transferencia {
	private int idInstituicaoOrigem;
	private int idInstituicaoDestino;
	private double valor;
	
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	
	public int getIdInstituicaoOrigem() {
		return idInstituicaoOrigem;
	}
	public void setIdInstituicaoOrigem(int idInstituicaoOrigem) {
		this.idInstituicaoOrigem = idInstituicaoOrigem;
	}
	
	public int getIdInstituicaoDestino() {
		return idInstituicaoDestino;
	}
	public void setIdInstituicaoDestino(int idInstituicaoDestino) {
		this.idInstituicaoDestino = idInstituicaoDestino;
	}
}
