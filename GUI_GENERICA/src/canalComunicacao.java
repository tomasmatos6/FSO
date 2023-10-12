import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class canalComunicacao {
	// ficheiro
	private File ficheiro;
	// canal que liga o conteúdo do ficheiro ao Buffer
	protected FileChannel canal;
	// buffer
	private MappedByteBuffer buffer;
	// dimensão máxima em bytes do buffer
	final int BUFFER_MAX= 128;
	// Indíces para o buffer circular
	int putIdx = 0;
	int getIdx = 0;
	// construtor onde se cria o canal
	public canalComunicacao(){ 
		ficheiro=null;
		canal= null;
		buffer= null;
	}
	// abre o canal de comunicação
	public boolean abrirCanal(){
		//cria um ficheiro com o nome comunicacao.dat
		ficheiro = new File("comunicacao.dat");
		//cria um canal de comunicação de leitura e escrita
		try {
		canal = new RandomAccessFile(ficheiro, "rw").getChannel();
		} catch (FileNotFoundException e) { return false;}
		// mapeia para memória o conteúdo do ficheiro
		try {
		buffer = canal.map(FileChannel.MapMode.READ_WRITE, 0, BUFFER_MAX);
		} catch (IOException e) { return false;}
		return true;
	}
	
	// recebe uma mensagem convertendo-a numa String
	public Mensagem receberMensagem() {
		buffer.position(0);
		int numero = buffer.getInt();
		int comando = buffer.getInt();
		int arg1 = buffer.getInt();
		int arg2 = buffer.getInt();
		Mensagem msg = new Mensagem(numero, comando, arg1, arg2);
		return new Mensagem(numero, comando, arg1, arg2);
	}
	
	
	// envia uma String como mensagem
	public void enviarMensagem(Mensagem msg) {
		int c;
		buffer.position(0);
		/**
		for(int i=0;i<4;i++) {
			c = msg.getArg(i);
			buffer.putInt(c);
		}
		*/
		buffer.putInt(msg.getNumero());
		buffer.putInt(msg.getComando());
		buffer.putInt(msg.getArg1());
		buffer.putInt(msg.getArg2());
		buffer.putInt('\0');
	}
	
	
	// fecha o canal de comunicação
	public void fecharCanal() {
		if (canal!=null)
			try {
			canal.close();
			} catch (IOException e) { canal= null; }
	}
}
