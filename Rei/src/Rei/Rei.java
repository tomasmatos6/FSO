package Rei;

import CanalComunicacao.CriarMensagem;
import CanalComunicacao.canalComunicacaoConsistente;

public class Rei implements iRei {
	private Dados dados;
	private CriarMensagem cm;
	private canalComunicacaoConsistente ccc;
	private GUI_REI gui_rei;
	
	public Rei() {
		dados = new Dados();
		cm = new CriarMensagem(dados);
		ccc = new canalComunicacaoConsistente(this);
		estado = DESATIVO;
		gui_rei = new GUI_REI(this);
	}
	
	private int estado;
	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	public Dados getDados() {
		return dados;
	}
	// Método Run
	public void run() {
		while(estado != FECHAR_CANAL) {
			switch(estado) {
			case DESATIVO:
				try {
					Thread.sleep(100);
					
					if(dados.isComportamento()) 
						estado = ATIVO;
					break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			case ATIVO:
				try {
					Thread.sleep(100);
					if(dados.isOnOff())
						estado = ABRIR_CANAL;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			case ABRIR_CANAL:
				try {
					Thread.sleep(100);	
					if(ccc.abrirCanal(dados.getCanalPath()))
						estado = CANAL_ABERTO;
					break;
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			case CANAL_ABERTO:
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case FRENTE:
				ccc.getAndSetEscritor(cm.MsgFrente());
				estado = CANAL_ABERTO;
				break;
			case ESQUERDA:
				ccc.getAndSetEscritor(cm.MsgEsquerda());
				estado = CANAL_ABERTO;
				break;
			case DIREITA:
				ccc.getAndSetEscritor(cm.MsgDireita());
				estado = CANAL_ABERTO;
				break;
			case TRAS:
				ccc.getAndSetEscritor(cm.MsgRetaguarda());
				estado = CANAL_ABERTO;
				break;
			case PARAR:
				ccc.getAndSetEscritor(cm.MsgParar());
				estado = CANAL_ABERTO;
				break;
			case FECHAR_CANAL:
				ccc.fecharCanal();
				estado = FECHAR_CANAL;
			}
		}
	}
	
	public static void main(String[] args) {
		Rei rei = new Rei();
		rei.run();
	}
}
