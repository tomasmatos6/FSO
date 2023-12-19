package utils;
import robot.RobotLegoEV3;

public class myRobotLego {
	//protected RobotDebug robot;
	protected RobotLegoEV3 robot;
	public myRobotLego(RobotDebug robot) {
		//this.robot = robot;
	}
	
	public myRobotLego(RobotLegoEV3 robot) {
		this.robot = robot;
	}
	
	public boolean OpenEV3(String s) {
		return robot.OpenEV3(s);
	}
	
	public void CloseEV3() {
		robot.CloseEV3();
	}
	
	public void Parar(boolean sinc) {
		robot.Parar(sinc);
	}
	
	public void Reta(int distancia) {
		robot.Reta(distancia);
	}
	
	public void CurvarEsquerda(double raio, int angulo) {
		robot.CurvarEsquerda(raio, angulo);
	}
	
	public void CurvarDireita(double raio, int angulo) {
		robot.CurvarDireita(raio, angulo);
	}
}
