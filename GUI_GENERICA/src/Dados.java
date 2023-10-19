import robot.RobotLegoEV3;

public class Dados {
	private int raio, angulo, distancia;
	private String nomeRobot, canalPath;
	private boolean onOff, openClose;
	private RobotLegoEV3 robot;
	
	public Dados() {
		raio=10;
		angulo=90;
		distancia=20;
		nomeRobot="EV8";
		onOff=false;
		openClose = false;
		canalPath = "";
		//robot = new RobotLegoEV3();
	}
	
	public String getCanalPath() {
		return canalPath;
	}

	public void setCanalPath(String canalPath) {
		this.canalPath = canalPath;
	}

	public boolean isOpenClose() {
		return openClose;
	}

	public void setOpenClose(boolean openClose) {
		this.openClose = openClose;
	}

	public RobotLegoEV3 getRobot() {
		return robot;
	}

	public void setRobot(RobotLegoEV3 robot) {
		this.robot = robot;
	}

	public boolean isOnOff() {
		return onOff;
	}
	public void setOnOff(boolean onOff) {
		this.onOff = onOff;
	}
	public int getAngulo() {
		return angulo;
	}
	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}
	public int getDistancia() {
		return distancia;
	}
	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}
	public String getNomeRobot() {
		return nomeRobot;
	}
	public void setNomeRobot(String nomeRobot) {
		this.nomeRobot = nomeRobot;
	}
	public int getRaio() {
		return raio;
	}
	public void setRaio(int raio) {
		this.raio = raio;
	}
	
}
