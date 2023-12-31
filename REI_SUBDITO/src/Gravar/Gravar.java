package Gravar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import comunicacao.BufferCircular;
import robot.RobotLegoEV3;
import utils.RobotDebug;
import utils.myRobotLego;

public class Gravar extends myRobotLego implements Runnable, iGravar {
	private double VROBOT = 20.0;
	private boolean gravar;
	private int estado;
	private Semaphore trabalho, bloqueado, haGravar;
	private Gui_Gravar gui;
	private ArrayList<String> comandos;
	private Writer writer;
	private InputStreamReader reader;
	private int counter;
	private int dist, ang;
	private double raio;
	private boolean b;
	private int tempEstado; 
	
	public Gravar(RobotLegoEV3 robot) {
		super(robot);
		trabalho = new Semaphore(0);
		bloqueado = new Semaphore(0);
		haGravar = new Semaphore(0);
		estado = ESPERAR_TRABALHO;
		gravar = false;
		gui = new Gui_Gravar(this);
		comandos = new ArrayList<String>();
	}
	
	
	public boolean isGravar() {
		return gravar;
	}


	public void setGravar(boolean gravar) {
		this.gravar = gravar;
	}

	public int getEstado() {
		return estado;
	}


	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	public void acordar() {
		trabalho.release();
	}
	
	public void bloquear(Boolean b) {
		tempEstado = estado;
		estado = BLOQUEADO;
		gui.toggleAll(false);
		acordar();
	}
	
	public void desbloquear() {
		estado = tempEstado;
		gui.toggleAll(true);
		bloqueado.release();
	}
	
	public void toggleGui(Boolean b) {
		gui.setVisible(b);
	}
	
	public void pararGravar() {
		estado = ESPERAR_TRABALHO;
		haGravar.release();
	}
	

	public synchronized void Reta(int distancia) {
		estado = RETA;
		this.dist = distancia;
		super.Reta(distancia);
		haGravar.release();
	}
	
	public synchronized void CurvarEsquerda(double raio, int ang) {
		estado = ESQUERDA;
		this.raio = raio;
		this.ang = ang;
		super.CurvarEsquerda(raio, ang);
		haGravar.release();
	}
	
	public synchronized void CurvarDireita(double raio, int ang) {
		estado = DIREITA;
		this.raio = raio;
		this.ang = ang;
		super.CurvarDireita(raio, ang);
		haGravar.release();
	}
	
	public synchronized void Parar(boolean b) {
		estado = PARAR;
		this.b = b;
		super.Parar(b);
		haGravar.release();
	}
	
	private void lerFicheiro() {
        StringBuilder line = new StringBuilder();
        int character;

        try {
			while ((character = reader.read()) != -1) {
			    if (character == '\n') {
			    	comandos.add(line.toString());
			        //System.out.println("Linha lida: " + line.toString());
			        line.setLength(0);
			    } else {
			        line.append((char) character);
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Verifica se há uma última linha não finalizada
        if (line.length() > 0) {
        	comandos.add(line.toString());
            System.out.println("Linha lida: " + line.toString());
        }
	}
	
	private synchronized void reproduzir() {
		double counter;
		while(!comandos.isEmpty()) {
			counter = stringToComando(comandos.remove(0));
				try {
					Thread.sleep((long) (1000*counter));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	private double stringToComando(String s) {
		String[] aux = s.split(" ");
		double counter = 1;
		int distAux;
		double raioAux;
		int angAux;
		
		switch(aux[0]) {
		case "parar":
			robot.Parar(Boolean.parseBoolean(aux[1]));
			break;
		case "reta":
			distAux = Integer.parseInt(aux[1]);
			robot.Reta(distAux);
			counter = distAux / VROBOT;
			break;
		case "esquerda":
			raioAux = Double.parseDouble(aux[1]);
			angAux = Integer.parseInt(aux[2]);
			robot.CurvarEsquerda(raioAux, angAux);
			counter = angAux * (Math.PI/180) * raioAux / VROBOT;
			break;
		case "direita":
			raioAux = Double.parseDouble(aux[1]);
			angAux = Integer.parseInt(aux[2]);
			robot.CurvarDireita(Double.parseDouble(aux[1]),Integer.parseInt(aux[2]));
			counter = angAux * (Math.PI/180) * raioAux / VROBOT;
			break;
		}
		return counter;
	}
	
	public void run() {
		while(true) {
			switch(estado) {
			case BLOQUEADO:
				try {
					bloqueado.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case ESPERAR_TRABALHO:
				try {
					trabalho.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case ESPERAR_GRAVAR:
				try {
					haGravar.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case GRAVAR:
				gravar = true;
				try {
					writer = new OutputStreamWriter(new FileOutputStream(gui.getPath()));		
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				estado = ESPERAR_GRAVAR;
				break;
			case LER_FICHEIRO:
				try {
					reader = new InputStreamReader(new FileInputStream(gui.getPath()));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lerFicheiro();
				estado = REPRODUZIR;
				break;
			case REPRODUZIR:
				if(!comandos.isEmpty())
					reproduzir();
				estado = ESPERAR_TRABALHO;
				break;
			case PARAR:
				if(gravar) {
					try {
						writer.write(String.format("parar %b\n",b));
						writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					estado = ESPERAR_GRAVAR;
					break;
				}
				else 
					estado = ESPERAR_TRABALHO;
				break;
			case RETA:
				if(gravar) {
					try {
						writer.write(String.format("reta %d\n", dist));
						writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					estado = ESPERAR_GRAVAR;
				}
				else
					estado = ESPERAR_TRABALHO;
				break;
			case ESQUERDA:
				if(gravar) {
					try {
						writer.write(String.format("esquerda %2.1f %d\n",raio,ang));
						writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					estado = ESPERAR_GRAVAR;
					
				}
				else
					estado = ESPERAR_TRABALHO;
				break;
			case DIREITA:
				if(gravar) {
					try {
						writer.write(String.format("direita %2.1f %d\n",raio,ang));
						writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					estado = ESPERAR_GRAVAR;
				}
				else
					estado = ESPERAR_TRABALHO;
				break;
			}
		}
	}
}
