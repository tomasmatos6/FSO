package Subdito;

import java.util.concurrent.Semaphore;

import Gravar.Gravar;
import comunicacao.BufferCircular;
import comunicacao.Mensagem;
import utils.myRobotLego;

public class Subdito extends Thread implements iSubdito {
	private static int VROBOT = 30;
	private int estado = DESLIGADO;
	private Mensagem mensagem;
	private BufferCircular bufferCircular;
	private Semaphore livreMensagem, ocupadaMensagem, haTrabalho, acessoMensagem, bloqueado;
	private myRobotLego robot;
	private int comando, arg1, arg2;
	
	private Gui_Subdito gui;
	
	public Subdito(BufferCircular bc, Semaphore ht, Gravar gravar) {
		bufferCircular = bc;
		haTrabalho = ht;
		mensagem = new Mensagem();
		livreMensagem = new Semaphore(1);
		ocupadaMensagem = new Semaphore(0);
		acessoMensagem = new Semaphore(1);
		bloqueado = new Semaphore(0);
		robot = gravar;
		gui = new Gui_Subdito(this);
	}
	
	public myRobotLego getRobot() {
		return robot;
	}

	public void setRobot(myRobotLego robot) {
		this.robot = robot;
	}

	public Mensagem getMensagem() {
		try {
			ocupadaMensagem.acquire();
			acessoMensagem.acquire();
		} catch (InterruptedException e) {}
		Mensagem m = mensagem;
		acessoMensagem.release();
		livreMensagem.release();
		return m;
	}
	
	public void obterMensagem() {
		try {
			haTrabalho.acquire();
			livreMensagem.acquire();
		} catch(InterruptedException e) {}
		Mensagem m = bufferCircular.removerElemento();
		try {
			acessoMensagem.acquire();
		} catch(InterruptedException e) {}
		mensagem = m;
		acessoMensagem.release();
		ocupadaMensagem.release();
	}
	
	private void interpretarMensagens(int comando, int arg1, int arg2) {
		switch(comando) {
		case 0:
			robot.Parar(true);
			break;
		case 1:
			robot.Reta(arg1);
			//robot.Parar(false);
			break;
		case 2:
			robot.CurvarEsquerda(arg1, arg2);
			//robot.Parar(false);
			break;
		case 3:
			robot.CurvarDireita(arg1, arg2);
			//robot.Parar(false);
			break;
		}
	}
	
	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	private int dormir() {
		// reta = dist / vRobot
		// curva = raio * ang / vRobot
		int counter;
		switch(comando) {
		case 1:
			counter = arg1 / VROBOT;
			break;
		case 2:
		case 3:
			counter = (int) (Math.ceil(arg1 * Math.PI/180) * arg2 / VROBOT);
			break;
		default:
			counter = 0;
			break;
		}
		return counter;
	}

	public void run() {
		while(true) {
			//System.out.println("Estado Subdito = " + estado);
			switch(estado) {
			case BLOQUEADO:
				try {
					bloqueado.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case DESLIGADO:
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case ABRIR_ROBOT:
				if(gui.nomeRobot.getText() != null) {
					if(robot.OpenEV3(gui.nomeRobot.getText())) {
						estado = ESPERAR_TRABALHO;
						break;
					}
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case ESPERAR_TRABALHO:
				try {
					haTrabalho.acquire();
					estado = LER_COMANDOS;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case LER_COMANDOS:
				obterMensagem();
				estado = INTERPRETAR_COMANDOS;
				break;
			case INTERPRETAR_COMANDOS:
				Mensagem tempMsg = getMensagem();
				gui.logText.append(""+tempMsg+"\n");
				comando = tempMsg.getComando();
				arg1 = tempMsg.getArg1();
				arg2 = tempMsg.getArg2();
				
				estado = ENVIAR_ROBOT;
				break;
			case ENVIAR_ROBOT:
				interpretarMensagens(comando, arg1, arg2);
				//acessoMensagem.release();
				estado = DORMIR;
				break;
			case DORMIR:
				int counter = dormir();
				
				while(counter != 0) {
					try {
						Thread.sleep(1);
						counter--;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				estado = ESPERAR_TRABALHO;
				break;
			}
		}
	}
}
