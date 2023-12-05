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
import utils.myRobotLego;

public class Gravar extends myRobotLego implements iGravar {
	private int VROBOT = 30;
	private boolean gravar, reproduzir;
	private RobotLegoEV3 robot;
	private int estado = ESPERAR_TRABALHO;
	private Semaphore trabalho, acessoFicheiro, ocupadoFicheiro;
	private Gui_Gravar gui;
	private ArrayList<String> comandos;
	private Writer writer;
	private InputStreamReader reader;
	private int counter;
	
	// DUVIDAS
	// PODE-SE USAR BUFFEREDREADER & BUFFEREDWRITER
	// O QUE ACONTECER QUANDO RECEBO UM COMANDO COM O REPRODUZIR ATIVO
	
	public Gravar(BufferCircular bc) {
		robot  = new RobotLegoEV3();
		trabalho  = new Semaphore(0);
		acessoFicheiro = new Semaphore(1);
		ocupadoFicheiro = new Semaphore(0);
		gravar = false;
		reproduzir = false;
		gui = new Gui_Gravar(this);
		comandos = new ArrayList<String>();
		try {
			writer = new OutputStreamWriter(new FileOutputStream(gui.getPath()));
			reader = new InputStreamReader(new FileInputStream(gui.getPath()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean isGravar() {
		return gravar;
	}


	public void setGravar(boolean gravar) {
		this.gravar = gravar;
	}


	public boolean isReproduzir() {
		return reproduzir;
	}


	public void setReproduzir(boolean reproduzir) {
		this.reproduzir = reproduzir;
	}


	public int getEstado() {
		return estado;
	}


	public void setEstado(int estado) {
		this.estado = estado;
	}


	public void reta(int distancia) {
		if(gravar) {
			try {
				acessoFicheiro.acquire();
				writer.write(String.format("reta %d",distancia));
				acessoFicheiro.release();
				ocupadoFicheiro.release();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else if(reproduzir) {
			 
		}
		else {
			robot.Reta(distancia); 
		}
	}
	
	public void CurvarEsquerda(double raio, int ang) {
		if(gravar) {
			try {
				acessoFicheiro.acquire();
				writer.write(String.format("esquerda %d %d",raio,ang));
				acessoFicheiro.release();
				ocupadoFicheiro.release();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}	
		else if (reproduzir) {
			
		}
		else {
			robot.CurvarEsquerda(raio, ang);
		}
		estado = ESPERAR_TRABALHO;
	}
	
	public void CurvarDireita(double raio, int ang) {
		if(gravar) {
			try {
				acessoFicheiro.acquire();
				writer.write(String.format("direita %d %d",raio,ang));
				acessoFicheiro.release();
				ocupadoFicheiro.release();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(reproduzir) {
			
		}
		else {
			robot.CurvarDireita(raio, ang);
		}
	}
	
	public void Parar(boolean b) {
		if(gravar) {
			try {
				acessoFicheiro.acquire();
				writer.write(String.format("parar %b",b));
				acessoFicheiro.release();
				ocupadoFicheiro.release();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(reproduzir) {
			
		}
		else {
			robot.Parar(b);
		}
	}
	
	private int stringToComando(String s) {
		String[] aux = s.split(" ");
		int counter = 0;
		int dist;
		double raio;
		int ang;
		
		switch(aux[0]) {
		case "0":
			robot.Parar(Boolean.parseBoolean(aux[1]));
			break;
		case "1":
			dist = Integer.parseInt(aux[1]);
			robot.Reta(dist);
			counter = dist / VROBOT;
			break;
		case "2":
			raio = Double.parseDouble(aux[1]);
			ang = Integer.parseInt(aux[2]);
			robot.CurvarEsquerda(raio, ang);
			counter = (int) (Math.ceil(raio * Math.PI/180) * ang / VROBOT);
			break;
		case "3":
			raio = Double.parseDouble(aux[1]);
			ang = Integer.parseInt(aux[2]);
			robot.CurvarDireita(Double.parseDouble(aux[1]),Integer.parseInt(aux[2]));
			counter = (int) (Math.ceil(raio * Math.PI/180) * ang / VROBOT);
			break;
		}
		return counter;
	}
	
	public void run() {
		while(true) {
			switch(estado) {
			case ESPERAR_TRABALHO:
				try {
					trabalho.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case GRAVAR:
				gravar = true;
				break;
			case LER_FICHEIRO:
				reproduzir = true;
				// LER FICHEIRO
				try {
					acessoFicheiro.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				acessoFicheiro.release();
				estado = REPRODUZIR;
				break;
			case REPRODUZIR:
				if(!comandos.isEmpty())
					try {
						ocupadoFicheiro.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					counter = stringToComando(comandos.remove(0));
				estado = DORMIR;
				break;
			case DORMIR:
				while(counter != 0) {
					try {
						Thread.sleep(1);
						counter--;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(!comandos.isEmpty())
					estado = REPRODUZIR;
				estado = ESPERAR_TRABALHO;
			}
		}
	}
}
