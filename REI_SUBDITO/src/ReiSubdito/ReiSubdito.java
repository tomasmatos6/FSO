package ReiSubdito;

import java.util.concurrent.Semaphore;

import Rei.Rei;
import Subdito.Subdito;
import comunicacao.BufferCircular;
import comunicacao.Dados;

public class ReiSubdito extends Thread {
	public static void main(String[] args) {
		BufferCircular bc = new BufferCircular();
		Semaphore trabalhoSubdito = new Semaphore(0);
		Dados dados = new Dados();
		
		ReiSubdito rs = new ReiSubdito();
		Rei rei = new Rei(bc, trabalhoSubdito, dados);
		Subdito subdito = new Subdito(bc, trabalhoSubdito);
		rei.start();
		subdito.start();

	}
}
