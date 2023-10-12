import java.io.IOException;
import java.nio.channels.FileLock;

public class canalComunicacaoConsistente extends canalComunicacao {
	private int lastMsgNum = -1;
	public void getAndSetEscritor(Mensagem mensagem) {
		try {
			FileLock fl = canal.lock();
			Mensagem msg = receberMensagem();
			// Verificar se o leitor já leu a mensagem
			if(msg.getComando() == 4) {
				enviarMensagem(mensagem);
			}
			fl.release();
		} catch(IOException e) {
			
		}
		
	}
	
	public Mensagem getAndSetLeitor() {
		Mensagem currentMsg = null;
		try {
			FileLock fl = canal.lock();
			Mensagem msg = receberMensagem();
			// Verificar se esta a ler a mesma mensagem
			if(msg.getNumero() != lastMsgNum) {
				lastMsgNum = msg.getNumero();
				currentMsg = msg;
				// Mensagem com o mesmo numero e comando 4 que marca que a leitura foi realizada
				enviarMensagem(new Mensagem(lastMsgNum, 4, 0, 0));
			}	
			fl.release();
		} catch(IOException e) {
			
		}
		return currentMsg;
	}
	
	public static void main(String[] args) {
		Mensagem msg = new Mensagem(0, 3, 10, 90);
		canalComunicacaoConsistente ccc = new canalComunicacaoConsistente();
		ccc.abrirCanal();
		ccc.getAndSetEscritor(msg);
		Mensagem msg1 = new Mensagem(1, 2, 10, 30);
		
		System.out.println(ccc.getAndSetLeitor());
		ccc.getAndSetEscritor(msg1);
		System.out.println(ccc.getAndSetLeitor());
		ccc.fecharCanal();
	}
}
