package Subdito;

import java.util.ArrayList;

import CanalComunicacao.Mensagem;
import CanalComunicacao.canalComunicacaoConsistente;
import robot.RobotLegoEV3;

public class Subdito implements iSubdito {
	private static int VROBOT = 30;
	private int estado_rei, estado_robot;
	private Dados d;
	private canalComunicacaoConsistente ccc;
	private Mensagem msg_canal, msg_memoria;
	private ArrayList<Mensagem> mensagens;
	private RobotLegoEV3 robot;
	private GUI_SUBDITO gui_subdito;
	
	public Subdito() {
		d = new Dados();
		ccc = new canalComunicacaoConsistente(this);
		estado_rei = DESATIVO;
		estado_robot = DESATIVO;
		mensagens = new ArrayList<Mensagem>();
		robot = d.getRobot();
		gui_subdito = new GUI_SUBDITO(this);
	}
	
	public Dados getDados() {
		return this.d;
	}
	
	public void setEstadoRei(int estado) {
		this.estado_rei = estado;
	}
	
	public void setEstadoRobot(int estado) {
		this.estado_robot = estado;
	}
	
	private void comunicarRei() {
		//System.out.println("Estado Rei = " + estado_rei);
		switch(estado_rei) {
		case DESATIVO:
			try {
				Thread.sleep(100);
//					if(d.isOpenClose()) {
//						estado_rei = ABRIR_CANAL;
//					}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case ABRIR_CANAL:
			if(ccc.abrirCanal(d.getCanalPath()))
				estado_rei = LER_CANAL;
			break;
		case LER_CANAL:
			// LER MENSAGENS DO CANAL
			msg_canal = ccc.getAndSetLeitor();
			
			if(msg_canal != null) {
				//System.out.println("LER CANAL = " + msg);
				estado_rei = MEMORIZAR;	
			}
			// SE NÃO HOUVER MENSAGENS
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case MEMORIZAR:
			mensagens.add(msg_canal);
			System.out.println(mensagens);
			estado_rei = LER_CANAL;
			break;
		}
	}
	
	private void comunicarRobot() {
		System.out.println("Estado Robot = " + estado_robot);
		switch(estado_robot) {
		case DESATIVO:
			try {
				Thread.sleep(100);
//					if(d.isComportamento()) 
//						estado_robot = ATIVO;
				break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		case ATIVO:
			try {
				Thread.sleep(100);
//					if(d.isOnOff()) 
//						estado_robot = LER_MEMORIA;
				break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		case LER_MEMORIA:
			if(!mensagens.isEmpty()) {
				msg_memoria = mensagens.remove(0);
				estado_robot = INTERPRETAR_MENSAGENS;
			}
			else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			break;
		case INTERPRETAR_MENSAGENS:
			
			interpretarMensagens(msg_memoria.getComando(), msg_memoria.getArg1(), msg_memoria.getArg2());
			System.out.println(msg_memoria.getComando());
			estado_robot = DORMIR;
			break;
		case DORMIR:
			// reta = dist / vRobot
			// curva = raio * ang / vRobot
			int counter;
			switch(msg_memoria.getComando()) {
			case 0:
				counter = msg_memoria.getArg1() / VROBOT;
				break;
			case 1:
			case 2:
				counter = (int) (Math.ceil(msg_memoria.getArg1() * Math.PI/180) * msg_memoria.getArg2() / VROBOT);
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
			estado_robot = LER_MEMORIA;
			break;
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
		while(estado_rei != FECHAR_CANAL || estado_robot != FECHAR_CANAL) {
			comunicarRei();
			comunicarRobot();
		}
	}
	
	public static void main(String[] args) {
		Subdito subdito = new Subdito();
		subdito.run();
	}
}
