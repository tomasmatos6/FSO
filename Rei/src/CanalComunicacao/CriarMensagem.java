package CanalComunicacao;

import Rei.Dados;

public class CriarMensagem {
	int numero;
	Mensagem msg;
	Dados dados;
	
	
	public CriarMensagem(Dados d) {
		dados = d;
		numero = 0;
		msg = new Mensagem(0,0,0,0);
	}
	
	public Mensagem MsgFrente() {
		numero++;
		msg.setNumero(numero);
		msg.setComando(iMensagem.RETA);
		msg.setArg1(dados.getDistancia());
		msg.setArg2(0);
		return msg;
	}
	
	public Mensagem MsgRetaguarda() {
		numero++;
		msg.setNumero(numero);
		msg.setComando(iMensagem.RETA);
		msg.setArg1(-dados.getDistancia());
		msg.setArg2(0);
		return msg;
	}
	
	public Mensagem MsgEsquerda() {
		numero++;
		msg.setNumero(numero);
		msg.setComando(iMensagem.ESQUERDA);
		msg.setArg1(dados.getRaio());
		msg.setArg2(dados.getAngulo());
		return msg;
	}
	
	public Mensagem MsgDireita() {
		numero++;
		msg.setNumero(numero);
		msg.setComando(iMensagem.DIREITA);
		msg.setArg1(dados.getRaio());
		msg.setArg2(dados.getAngulo());
		return msg;
	}
	
	public Mensagem MsgParar() {
		numero++;
		msg.setNumero(numero);
		msg.setComando(iMensagem.PARAR);
		msg.setArg1(0);
		msg.setArg2(0);
		return msg;
	}
	
	public Mensagem MsgLida() {
		msg.setNumero(0);
		msg.setComando(iMensagem.LIDO);
		msg.setArg1(0);
		msg.setArg2(0);
		return msg;
	}
}
