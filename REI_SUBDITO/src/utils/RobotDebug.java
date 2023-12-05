package utils;


public class RobotDebug {
	boolean conectado = false;
	
	public RobotDebug() {
		System.out.println("Robot Criado");
	}
	
	public boolean OpenEV3(String nome) {
		System.out.println("Open " + nome);
		conectado = true;
		return conectado;
	}
	
	public void CloseEV3() {
		System.out.println("Close");
		conectado = false;
	}
	
	public void Reta(int dist) {
		if(conectado)
			System.out.println("Reta " + dist);
		else
			System.out.println("Robot não conectado");
	}
	
	public void CurvarEsquerda(double raio, int angulo) {
		System.out.println("CurvarEsquerda " + raio + " " + angulo);
	}
	
	public void CurvarDireita(double raio, int angulo) {
		System.out.println("CurvarDireita " + raio + " " + angulo);
	}

	public void Parar(boolean assincrono) {
		System.out.println("Parar " + assincrono);
	}
}
