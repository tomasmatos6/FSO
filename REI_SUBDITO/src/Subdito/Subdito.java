package Subdito;

import java.util.concurrent.Semaphore;

import Gravar.Gravar;
import comunicacao.BufferCircular;
import comunicacao.Mensagem;
import utils.myRobotLego;

public class Subdito extends Thread implements iSubdito {
	private static double VROBOT = 20.0;
	private int estado = DESLIGADO;
	private Mensagem mensagem;
	private BufferCircular bufferCircular;
	private Semaphore livreMensagem, ocupadaMensagem, haTrabalho, acessoMensagem, bloqueado;
	private myRobotLego robot;
	private int comando, arg1, arg2;
	private int tempEstado;
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
	
	public void toggleGui(Boolean b) {
		gui.setVisible(b);
	}
	
	public void bloquear(Boolean b) {
		tempEstado = estado;
		estado = BLOQUEADO;
		gui.toggleAll(false);
		if(tempEstado == ESPERAR_TRABALHO)
			acordar();
	}
	
	public void desbloquear() {
		estado = tempEstado;
		gui.toggleAll(true);
		bloqueado.release();
	}
	
	public void acordar() {
		haTrabalho.release();
	}
	
	public void setGravar(Gravar gravar) {
		this.robot = gravar;
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
	
	private double dormir() {
		// reta = dist / vRobot
		// curva = raio * ang / vRobot
		double counter;
		switch(comando) {
		case 1:
			counter = arg1 / VROBOT;
			break;
		case 2:
		case 3:
			counter = arg2 * Math.PI/180 * arg1 / VROBOT;
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
						gui.logText.append(gui.nomeRobot.getText() + " Conectado!");
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
					if(estado != BLOQUEADO)
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
				double counter = dormir();
				try {
					Thread.sleep((long)(1000*counter));
					counter--;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				estado = ESPERAR_TRABALHO;
				break;
			}
		}
	}
}
