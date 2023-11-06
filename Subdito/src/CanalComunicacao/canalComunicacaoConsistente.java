package CanalComunicacao;
import java.io.IOException;
import java.nio.channels.FileLock;

import Subdito.Subdito;

public class canalComunicacaoConsistente extends canalComunicacao {
	public canalComunicacaoConsistente(Subdito subdito) {
		super(subdito);
		// TODO Auto-generated constructor stub
	}

	private int lastMsgNum = -1;
	public void getAndSetEscritor(Mensagem mensagem) {
		try {
			FileLock fl = canal.lock();
			Mensagem msg = receberMensagem(getPutIdx());
			//Verificar se o leitor já leu a mensagem
			if(msg.getComando() == 4 || msg.getNumero() == 0) {
				enviarMensagem(mensagem, getPutIdx());
				increasePutIdx();
			}
			fl.release();
		} catch(IOException e) {
			
		}
		
	}
	
	public Mensagem getAndSetLeitor() {
		Mensagem currentMsg = null;
		try {
			FileLock fl = canal.lock();
			Mensagem msg = receberMensagem(getGetIdx());
			// Verificar se esta a ler a mesma mensagem
			if(msg.getNumero() != lastMsgNum) {
				lastMsgNum = msg.getNumero();
				currentMsg = msg;
				// Mensagem com o mesmo numero e comando 4 que marca que a leitura foi realizada
				enviarMensagem(new Mensagem(lastMsgNum, 4, 0, 0), getGetIdx());
				increaseGetIdx();
			}	
			fl.release();
		} catch(IOException e) {
			
		}
		return currentMsg;
	}
}
