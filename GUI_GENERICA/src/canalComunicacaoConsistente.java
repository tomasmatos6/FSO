import java.io.IOException;
import java.nio.channels.FileLock;

public class canalComunicacaoConsistente extends canalComunicacao {
	private int lastMsgNum = -1;
	public void getAndSetEscritor(Mensagem mensagem) {
		try {
			FileLock fl = canal.lock();
			Mensagem msg = receberMensagem(putIdx*16 % BUFFER_MAX);
			//Verificar se o leitor já leu a mensagem
			if(msg.getComando() == 4 || msg.getNumero() == 0) {
				enviarMensagem(mensagem, putIdx*16 % BUFFER_MAX);
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
			Mensagem msg = receberMensagem(getIdx*16 % BUFFER_MAX);
			// Verificar se esta a ler a mesma mensagem
			if(msg.getNumero() != lastMsgNum) {
				lastMsgNum = msg.getNumero();
				currentMsg = msg;
				// Mensagem com o mesmo numero e comando 4 que marca que a leitura foi realizada
				enviarMensagem(new Mensagem(lastMsgNum, 4, 0, 0), getIdx*16 % BUFFER_MAX);
				increaseGetIdx();
			}	
			fl.release();
		} catch(IOException e) {
			
		}
		return currentMsg;
	}
	
	public static void main(String[] args) {
		Mensagem msg = new Mensagem(0, 3, 10, 90);
		Mensagem msg1 = new Mensagem(1, 2, 11, 30);
		Mensagem msg2 = new Mensagem(2, 2, 12, 30);
		Mensagem msg3 = new Mensagem(3, 2, 13, 30);
		Mensagem msg4 = new Mensagem(4, 2, 14, 30);
		Mensagem msg5 = new Mensagem(5, 2, 15, 30);
		Mensagem msg6 = new Mensagem(6, 2, 16, 30);
		Mensagem msg7 = new Mensagem(7, 2, 17, 30);
		Mensagem msg8 = new Mensagem(8, 2, 18, 30);
		Mensagem msg9 = new Mensagem(9, 2, 19, 30);
		
		canalComunicacaoConsistente ccc = new canalComunicacaoConsistente();
		ccc.abrirCanal();
		ccc.getAndSetEscritor(msg);
		System.out.println(ccc.getAndSetLeitor());
		ccc.getAndSetEscritor(msg1);
		ccc.getAndSetEscritor(msg2);
		ccc.getAndSetEscritor(msg3);
		ccc.getAndSetEscritor(msg4);
		ccc.getAndSetEscritor(msg5);
		ccc.getAndSetEscritor(msg6);
		ccc.getAndSetEscritor(msg7);
		System.out.println(ccc.getAndSetLeitor());
		ccc.getAndSetEscritor(msg8);
		System.out.println(ccc.getAndSetLeitor());
		System.out.println(ccc.getAndSetLeitor());
		System.out.println(ccc.getAndSetLeitor());
		System.out.println(ccc.getAndSetLeitor());
		System.out.println(ccc.getAndSetLeitor());
		System.out.println(ccc.getAndSetLeitor());
		System.out.println(ccc.getAndSetLeitor());
		ccc.fecharCanal();
	}
}
