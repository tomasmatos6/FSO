package Rei;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.management.monitor.Monitor;
import javax.swing.SwingUtilities;

import comunicacao.BufferCircular;
import comunicacao.CriarMensagem;
import comunicacao.Dados;
import comunicacao.Mensagem;

public class Rei extends Thread implements iRei {
	private BufferCircular bufferCircular;
	private Mensagem mensagem;
	private Semaphore haTrabalho, livreMensagem, ocupadaMensagem, acessoMensagem, bloqueado, trabalhoRei;
	private int estado = ESPERAR_TRABALHO;
	private CriarMensagem cm;
	private Dados dados;
	private Gui_Rei gui;
	private int tempEstado;
	private Repeticao repetir;
	int counter;
	
	enum Repeticao {
		OFF,
		REPETIR8,
		REPETIR16
	}
	
	public Rei(BufferCircular bc, Semaphore ht, Dados dados) {
		bufferCircular = bc;
		haTrabalho = ht;
		mensagem = new Mensagem();
		livreMensagem = new Semaphore(1);
		ocupadaMensagem = new Semaphore(0);
		acessoMensagem = new Semaphore(1);
		bloqueado = new Semaphore(0);
		trabalhoRei = new Semaphore(0);
		this.dados = dados;
		cm = new CriarMensagem(dados);
		gui = new Gui_Rei(this);
		repetir = Repeticao.OFF;
	}
	
	public int getEstado() {
		return estado;
	}

	public void acordar() {
		trabalhoRei.release();
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public void bloquear() {
		tempEstado = estado;
		estado = BLOQUEADO;
		acordar();
	}
	
	public void desbloquear() {
		estado = tempEstado;
		gui.toggleAll(true);
		bloqueado.release();
	}
	
	public void comandos8() {
		repetir = Repeticao.REPETIR8;
		counter = 7;
		estado = COMANDOS8;
	}
	
	public void comandos16() {
		repetir = Repeticao.REPETIR16;
		counter = 15;
		estado = COMANDOS16;
	}
	
	public void setMensagem(Mensagem m) {
		try {
			livreMensagem.acquire();
			acessoMensagem.acquire();
		} catch(InterruptedException e) {}
		mensagem = m;
		acessoMensagem.release();
		ocupadaMensagem.release();
	}
	
	private void inserirMensagem(Mensagem m) {
		try {
			ocupadaMensagem.acquire();
			acessoMensagem.acquire();
		} catch (InterruptedException e) {}
		bufferCircular.inserirElemento(mensagem);
		acessoMensagem.release();
		livreMensagem.release();
		haTrabalho.release();
	}
	
	private int valor_aleatorio(int val1, int val2) {
		int valor_aleatorio = new Random().nextInt(val2 - val1 + 1) + val1;
		return valor_aleatorio;
	}
	private int comando_aleatorio() {
		return valor_aleatorio(1, 3);
	}
	private int randDistancia() {
		return valor_aleatorio(10, 50);
	}
	private int randAngulo() {
		return valor_aleatorio(0, 360);
	}
	private int randRaio() {
		return valor_aleatorio(0, 30);
	}
	
	public String escolherComando() {
		int comando = comando_aleatorio();
		dados.setDistancia(randDistancia());
		dados.setAngulo(randAngulo());
		dados.setAngulo(randRaio());
		String log = "";
		switch(comando) {
		case 1:
			estado = Rei.FRENTE;
			log = String.format("Reta(%d) \n", dados.getDistancia());
			break;
		case 2:
			estado = Rei.DIREITA;
			log = String.format("CurvaDireita(%d, %d) \n", dados.getAngulo(), dados.getRaio());
			break;
		case 3:
			estado = Rei.ESQUERDA;
			log = String.format("CurvaEsquerda(%d, %d) \n", dados.getAngulo(), dados.getRaio());
			break;
		}
		return log;
	}
	
	public void run() {
		while (true) {
			//System.out.println("Estado Rei = " + estado);
			switch(estado) {
			// BLOQUEADO
			case BLOQUEADO:
				try {
					gui.toggleAll(false);
					bloqueado.acquire();
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			// ESPERAR TRABALHO
			case ESPERAR_TRABALHO:
				try {
					trabalhoRei.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			// FRENTE
			case FRENTE:
				setMensagem(cm.MsgFrente());
				inserirMensagem(mensagem);
				haTrabalho.release();
				if(repetir == Repeticao.REPETIR8)
					estado = COMANDOS8;
				else if(repetir == Repeticao.REPETIR16)
					estado = COMANDOS16;
				else
					estado = ESPERAR_TRABALHO;
				break;
			// ESQUERDA
			case ESQUERDA:
				setMensagem(cm.MsgEsquerda());
				inserirMensagem(mensagem);
				haTrabalho.release();
				if(repetir == Repeticao.REPETIR8)
					estado = COMANDOS8;
				else if(repetir == Repeticao.REPETIR16)
					estado = COMANDOS16;
				else
					estado = ESPERAR_TRABALHO;
				break;
			// DIREITA
			case DIREITA:
				setMensagem(cm.MsgDireita());
				inserirMensagem(mensagem);
				haTrabalho.release();	
				if(repetir == Repeticao.REPETIR8)
					estado = COMANDOS8;
				else if(repetir == Repeticao.REPETIR16)
					estado = COMANDOS16;
				else
					estado = ESPERAR_TRABALHO;
				break;
			case PARAR:
				setMensagem(cm.MsgParar());
				inserirMensagem(mensagem);
				haTrabalho.release();
				estado = ESPERAR_TRABALHO;
				break;
			case COMANDOS8:
				if(counter == 0)
					repetir = Repeticao.OFF;
				counter--;
				gui.logText.append(escolherComando());
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        break;
			case COMANDOS16:
				if(counter == 0)
					repetir = Repeticao.OFF;
				counter--;
				escolherComando();
				gui.logText.append(escolherComando());
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        break;
			}
		}
	}

	public Dados getDados() {
		return dados;
	}

	public void setDados(Dados dados) {
		this.dados = dados;
	}
}
