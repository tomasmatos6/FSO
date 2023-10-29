package CanalComunicacao;

import Subdito.Dados;

public class CriarMensagem {
	int numero;
	Mensagem msg;
	Dados dados;
	
	
	public CriarMensagem(Dados d) {
		dados = d;
		numero = 0;
	}
	
	public Mensagem MsgFrente() {
		numero++;
		return new Mensagem(numero, iMensagem.RETA, dados.getDistancia(), 0);
	}
	
	public Mensagem MsgRetaguarda() {
		numero++;
		return new Mensagem(numero, iMensagem.RETA, -dados.getDistancia(), 0)
;	}
	
	public Mensagem MsgEsquerda() {
		numero++;
		return new Mensagem(numero, iMensagem.ESQUERDA, dados.getRaio(), dados.getAngulo());
	}
	
	public Mensagem MsgDireita() {
		numero++;
		return new Mensagem(numero, iMensagem.DIREITA, dados.getRaio(), dados.getAngulo());
	}
	
	public Mensagem MsgParar() {
		numero++;
		return new Mensagem(numero, iMensagem.PARAR, 0, 0);
	}
	
	public Mensagem MsgLida() {
		numero++;
		return new Mensagem(0, iMensagem.LIDO, 0, 0);
	}
}
