import java.util.concurrent.Semaphore;

public class BufferCircular {
	final int dimensaoBuffer = 8;
	Mensagem[] bufferCircular;
	int putBuffer, getBuffer;
	// o semáforo elementosLivres indica se há posições livres para inserir Mensagens
	// o semáforo acessoElemento garante exclusão mútua no acesso a um elemento
	// o semáforo elementosOcupados indica se há posições com Mensagens válidas
	Semaphore elementosLivres, acessoElemento, elementosOcupados;
	public BufferCircular(){
		bufferCircular= new Mensagem[dimensaoBuffer];
		putBuffer= 0;
		getBuffer= 0;
		elementosLivres= new Semaphore(dimensaoBuffer);
		elementosOcupados= new Semaphore(0);
		acessoElemento= new Semaphore(1);
	}
	
	public void inserirElemento(Mensagem msg){
		try {
			elementosLivres.acquire();
			acessoElemento.acquire();
			bufferCircular[putBuffer]= msg;
			putBuffer= ++putBuffer % dimensaoBuffer;
			acessoElemento.release();
		} catch (InterruptedException e) {}
			elementosOcupados.release();
		}
	
		public Mensagem removerElemento() {
			Mensagem msg = null;
			try {
				elementosOcupados.acquire();
				acessoElemento.acquire();
			} catch (InterruptedException e) {}
			msg = bufferCircular[getBuffer];
			getBuffer= ++getBuffer % dimensaoBuffer;
			acessoElemento.release();
			elementosLivres.release();
			return msg;
		}
	
}
