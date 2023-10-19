
public class Rei implements iRei {
	int estado;
	Dados d;
	CriarMensagem cm;
	canalComunicacaoConsistente ccc;
	
	public Rei() {
		d = new Dados();
		cm = new CriarMensagem(d);
		ccc = new canalComunicacaoConsistente();
	}
	
	public void Ativo() {
		
	}
	
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
						if(ccc.abrirCanal())
							estado = CANAL_ABERTO;
					}
					break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			case FRENTE:
				ccc.getAndSetEscritor(cm.MsgFrente());
				break;
			case ESQUERDA:
				ccc.getAndSetEscritor(cm.MsgEsquerda());
				break;
			case DIREITA:
				ccc.getAndSetEscritor(cm.MsgDireita());
				break;
			case TRAS:
				ccc.getAndSetEscritor(cm.MsgRetaguarda());
				break;
			case PARAR:
				ccc.getAndSetEscritor(cm.MsgParar());
				break;
			case FECHAR_CANAL:
				ccc.fecharCanal();
				System.exit(0);
			}
		}
	}
}
