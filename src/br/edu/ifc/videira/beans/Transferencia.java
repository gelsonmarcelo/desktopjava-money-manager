package br.edu.ifc.videira.beans;

public class Transferencia {
	private int idInstituicaoOrigem;
	private int idInstituicaoDestino;
	private double valor;
	
	public double getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = Double.parseDouble(valor.replaceAll("\\.", "").replaceAll(",", ".").replace("R$ ", ""));
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
