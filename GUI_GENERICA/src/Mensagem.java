
public class Mensagem {
	private int numero;
	private int comando;
	private int arg1;
	private int arg2;
	

	public Mensagem(int numero, int comando, int arg1, int arg2) {
		this.numero = numero;
		this.comando = comando;
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public int getArg(int i) {
		switch(i) {
			case 0: return numero;
			case 1: return comando;
			case 2: return arg1;
			case 3: return arg2;
			default: return 0;
		}
	}
	
	public String toString() {
		return String.format("Numero = %d, Comando = %d, Argumento1 = %d, Argumento2 = %d", numero, comando, arg1, arg2 );
	}

	public int getNumero() {
		return numero;
	}


	public void setNumero(int numero) {
		this.numero = numero;
	}


	public int getComando() {
		return comando;
	}


	public void setComando(int comando) {
		this.comando = comando;
	}


	public int getArg1() {
		return arg1;
	}


	public void setArg1(int arg1) {
		this.arg1 = arg1;
	}


	public int getArg2() {
		return arg2;
	}


	public void setArg2(int arg2) {
		this.arg2 = arg2;
	}
}
