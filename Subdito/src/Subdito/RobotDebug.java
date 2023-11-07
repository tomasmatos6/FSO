package Subdito;

public class RobotDebug {
	public RobotDebug() {
		System.out.println("Robot Criado");
	}
	
	public boolean OpenEV3(String nome) {
		System.out.println("Open " + nome);
		return true;
	}
	
	public void CloseEV3() {
		System.out.println("Close");
	}
	
	public void Reta(int dist) {
		System.out.println("Reta " + dist);
	}
	
	public void CurvarEsquerda(int raio, int angulo) {
		System.out.println("CurvarEsquerda " + raio + " " + angulo);
	}
	
	public void CurvarDireita(int raio, int angulo) {
		System.out.println("CurvarDireita " + raio + " " + angulo);
	}

	public void Parar(boolean assincrono) {
		System.out.println("Parar " + assincrono);
	}
}
