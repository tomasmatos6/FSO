package ReiSubdito;

import java.util.concurrent.Semaphore;

import Gravar.Gravar;
import Rei.Rei;
import Subdito.Subdito;
import comunicacao.BufferCircular;
import comunicacao.Dados;
import robot.RobotLegoEV3;
import utils.RobotDebug;
import utils.myRobotLego;

public class ReiSubdito extends Thread {
	Rei rei;
	Subdito subdito;
	Gravar gravar;
	BufferCircular bc;
	Semaphore trabalhoSubdito;
	Dados dados;
	
	public ReiSubdito() {
		bc = new BufferCircular();
		trabalhoSubdito = new Semaphore(0);
		dados = new Dados();
	}
	
	public void reiControl(Boolean b) {
		if(b) {
			if(rei == null) {
				rei = new Rei(bc, trabalhoSubdito, dados);
				rei.start();
			}
			else {
				rei.desbloquear();
			}
		}
		else {
			rei.bloquear();
		}
	}
	
	public void subditoControl(Boolean b) {
		if(b) {
			if(subdito == null) {
				subdito = new Subdito(bc, trabalhoSubdito, gravar);
				subdito.start();
			}
			else {
				subdito.desbloquear();
			}
		}
		else {
			subdito.bloquear();
		}
	}
	
	public void gravarControl(Boolean b) {
		if(b) {
			if(gravar == null) {
				gravar = new Gravar(new RobotDebug());
				if(subdito != null) 
					subdito.setRobot(gravar);
				new Thread(gravar).start();
			}
			else {
				gravar.desbloquear();
			}
		}
		else {
			gravar.bloquear();
		}
	}
	
	public static void main(String[] args) {
		new Gui_ReiSubdito(new ReiSubdito());
	}
}
