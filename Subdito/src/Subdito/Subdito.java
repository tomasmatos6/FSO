package Subdito;

import java.util.ArrayList;

import CanalComunicacao.Mensagem;
import CanalComunicacao.canalComunicacaoConsistente;
import robot.RobotLegoEV3;

public class Subdito implements iSubdito {
	private static int VROBOT = 30;
	private int estado;
	private Dados d;
	private canalComunicacaoConsistente ccc;
	private Mensagem msg;
	private ArrayList<Mensagem> mensagens;
	private RobotLegoEV3 robot;
	
	public Subdito() {
		d = new Dados();
		ccc = new canalComunicacaoConsistente();
		estado = DESATIVO;
		mensagens = new ArrayList<Mensagem>();
		robot = d.getRobot();
	}
	
	private void comunicarRei() {
		while(estado != FECHAR_CANAL) {
			switch(estado) {
			case ATIVO:
				try {
					Thread.sleep(100);
					if(d.isOpenClose()) {
						estado = LER_CANAL;
					} else {
						if(ccc.abrirCanal(d.getCanalPath()))
							estado = LER_CANAL;
					}
					break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			case LER_CANAL:
				// LER MENSAGENS DO CANAL
				msg = ccc.getAndSetLeitor();
				if(msg != null) {
					estado = MEMORIZAR;
				}
				// SE NÃO HOUVER MENSAGENS
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			case MEMORIZAR:
				mensagens.add(msg);
				estado = INTERPRETAR_MENSAGENS;
			}
		}
	}
	
	private void comunicarRobot() {
		while(estado != FECHAR_CANAL) {
			switch(estado) {
			case DESATIVO:
				try {
					Thread.sleep(100);
					if(d.isComportamento()) 
						estado = ATIVO;
					break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			case ATIVO:
				try {
					Thread.sleep(100);
					if(d.isOnOff()) 
						estado = LER_MEMORIA;
					break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			case LER_MEMORIA:
				if(!mensagens.isEmpty()) {
					estado = INTERPRETAR_MENSAGENS;
				}
			case INTERPRETAR_MENSAGENS:
				msg = mensagens.remove(0);
				interpretarMensagens(msg.getComando(), msg.getArg1(), msg.getArg2());
				estado = DORMIR;
			case DORMIR:
				// reta = dist / vRobot
				// curva = raio * ang / vRobot
				int counter;
				switch(msg.getComando()) {
				case 0:
					counter = msg.getArg1() / VROBOT;
					break;
				case 1:
				case 2:
					counter = msg.getArg1() * msg.getArg2() / VROBOT;
					break;
				default:
					counter = 0;
					break;
				}
				
				while(counter != 0) {
					try {
						Thread.sleep(1);
						counter--;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				estado = LER_CANAL;
			}
		}
	}
	
	private void interpretarMensagens(int comando, int arg1, int arg2) {
		switch(comando) {
		case 0:
			robot.Parar(true);
			break;
		case 1:
			robot.Reta(arg1);
			robot.Parar(false);
			break;
		case 2:
			robot.CurvarEsquerda(arg1, arg2);
			robot.Parar(false);
			break;
		case 3:
			robot.CurvarDireita(arg1, arg2);
			robot.Parar(false);
			break;
		}
	}
	
	public void run() {
		comunicarRei();
		comunicarRobot();
	}
}
