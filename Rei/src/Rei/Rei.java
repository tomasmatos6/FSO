package Rei;

import CanalComunicacao.CriarMensagem;
import CanalComunicacao.canalComunicacaoConsistente;

public class Rei implements iRei {
	private int estado;
	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
	Dados d;
	CriarMensagem cm;
	canalComunicacaoConsistente ccc;
	
	public Rei() {
		d = new Dados();
		cm = new CriarMensagem(d);
		ccc = new canalComunicacaoConsistente();
		estado = DESATIVO;
	}
	
	public void Ativo() {
		
	}
	// Método Run
	public void run() {
		while(estado != FECHAR_CANAL) {
			switch(estado) {
			case DESATIVO:
				try {
					Thread.sleep(100);
					if(d.isOnOff()) 
						estado = ATIVO;
					break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			case ATIVO:
				try {
					Thread.sleep(100);
					if(d.isOpenClose()) {
						estado = CANAL_ABERTO;
					} else {
						if(ccc.abrirCanal(d.getCanalPath()))
							estado = CANAL_ABERTO;
					}
					break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
}
