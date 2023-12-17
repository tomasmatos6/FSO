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
	
	public void threadControl(Thread thread) {
		if(thread == null) {
			thread.start();
		}
	}
	
	public static void main(String[] args) {
		BufferCircular bc = new BufferCircular();
		Semaphore trabalhoSubdito = new Semaphore(0);
		Dados dados = new Dados();
		
		ReiSubdito rs = new ReiSubdito();
		Rei rei = new Rei(bc, trabalhoSubdito, dados);
		RobotDebug robot = new RobotDebug();
		//RobotLegoEV3 robot = new RobotLegoEV3();
		Gravar gravar = new Gravar(robot);
		Subdito subdito = new Subdito(bc, trabalhoSubdito, gravar);
		Thread x = new Thread(gravar);
		rei.start();
		subdito.start();
		x.start();
	}
}
