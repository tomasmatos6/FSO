package Rei;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.TimerTask;
import java.awt.event.ActionEvent;

public class GUI_REI extends GUI_GENERICA {
	public int valor_aleatorio(int val1, int val2) {
		int valor_aleatorio = new Random().nextInt(val2 - val1 + 1) + val1;
		return valor_aleatorio;
	}
	public int comando_aleatorio() {
		return valor_aleatorio(1, 3);
	}
	public int randDistancia() {
		return valor_aleatorio(10, 50);
	}
	public int randAngulo() {
		return valor_aleatorio(0, 360);
	}
	public int randRaio() {
		return valor_aleatorio(0, 30);
	}
	
	private String escolherComando(Rei rei) {
		int comando = comando_aleatorio();
		dados.setDistancia(randDistancia());
		dados.setAngulo(randAngulo());
		dados.setAngulo(randRaio());
		String log = "";
		switch(comando) {
		case 1:
			rei.setEstado(Rei.FRENTE);
			log = String.format("Reta(%d) \n", dados.getDistancia());
			break;
		case 2:
			rei.setEstado(Rei.DIREITA);
			log = String.format("CurvaDireita(%d, %d) \n", dados.getAngulo(), dados.getRaio());
			break;
		case 3:
			rei.setEstado(Rei.ESQUERDA);
			log = String.format("CurvaEsquerda(%d, %d) \n", dados.getAngulo(), dados.getRaio());
			break;
		}
		return log;
	}
	
	public GUI_REI(Rei rei) {
		super(rei);
		this.setTitle("GUI Rei");
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Controlo do Robot Autom\u00E1tico", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 258, 546, 71);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btn8Comandos = new JButton("8 Comandos Aleatórios");
		btn8Comandos.addActionListener(new ActionListener() {
			int counter = 0; // Iniciar o counter
		    
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        java.util.Timer timer = new java.util.Timer();
		        
		        // Criar uma rotina para correr o código repetidamente a cada 200 milisegundos
		        timer.scheduleAtFixedRate(new TimerTask() {
		            @Override
		            public void run() {
		                if (counter < 8) {
		                	// Usar SwingUtilities.invokeLater para correr este código na Event Dispatch Thread
		                    SwingUtilities.invokeLater(new Runnable() {
		                        @Override
		                        public void run() {
		                            logText.append(escolherComando(rei));
		                        }
		                    });
		                    counter++;
		                } else {
		                    timer.cancel();
		                }
		            }
		        }, 0, 200); // Schedule the task to run every 200 milliseconds
		    }
		});
		btn8Comandos.setBounds(25, 28, 226, 31);
		panel.add(btn8Comandos);
		
		JButton btn16Comandos = new JButton("16 Comandos Aleatórios");
		btn16Comandos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < 16; i++) {
					logText.append(escolherComando(rei));
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btn16Comandos.setBounds(300, 28, 226, 31);
		panel.add(btn16Comandos);
	}
	public static void main(String[] args) {
		Rei rei = new Rei();
		GUI_REI gui = new GUI_REI(rei);
	}
}
