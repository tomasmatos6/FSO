package Rei;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import comunicacao.Dados;
import utils.Gui_Generica;

public class Gui_Rei extends Gui_Generica {
	private static final long serialVersionUID = 1L;
	private JTextField dist, ang, raio;
	private JPanel controleMenu,randomMenu;
	private JButton leftButton, rightButton, backButton, fowardButton, stopButton, btn8Comandos, btn16Comandos;
	private Dados dados;
	private Rei rei;
	
	
	
	public Gui_Rei(Rei rei) {
		logText.setBounds(10, 299, 549, 217);
		comportamentoCheck.setBounds(10, 255, 254, 23);
		this.rei = rei;
		dados = this.rei.getDados();
		setTitle("Rei");
		initialize();
	}
	
	public void toggleAll(Boolean b) {
		dist.setEnabled(b);
		ang.setEnabled(b);
		raio.setEnabled(b);
		rightButton.setEnabled(b);
		backButton.setEnabled(b);
		fowardButton.setEnabled(b);
		stopButton.setEnabled(b);
		leftButton.setEnabled(b);
		btn8Comandos.setEnabled(b);
		btn16Comandos.setEnabled(b);
		btnLimparLog.setEnabled(b);
	}
	
	private void initialize() {
		setLocation(100, 100);
		controleMenu = new JPanel();
		controleMenu.setBounds(13, 10, 546, 128);
		controleMenu.setLayout(null);
		controleMenu.setBorder(new TitledBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)), "Controlo do Robot", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		getContentPane().add(controleMenu);
		
		JLabel lblRaio = new JLabel("Raio");
		lblRaio.setBounds(12, 18, 70, 15);
		controleMenu.add(lblRaio);
		
		JLabel lblAng = new JLabel("Ângulo");
		lblAng.setBounds(12, 56, 70, 15);
		controleMenu.add(lblAng);
		
		JLabel lblDistncia = new JLabel("Distância");
		lblDistncia.setBounds(12, 96, 70, 15);
		controleMenu.add(lblDistncia);
		
		dist = new JTextField(""+dados.getDistancia());
		dist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					dados.setDistancia(Integer.parseInt(dist.getText()));				
				} catch(NumberFormatException e) {
					dist.setText(""+dados.getDistancia());
				}
			}
		});
		dist.setColumns(10);
		dist.setBounds(127, 87, 114, 26);
		controleMenu.add(dist);
		
		ang = new JTextField(""+dados.getAngulo());
		ang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					dados.setAngulo(Integer.parseInt(ang.getText()));				
				} catch(NumberFormatException e) {
					ang.setText(""+dados.getAngulo());
				}
			} 
		});
		ang.setColumns(10);
		ang.setBounds(127, 54, 114, 26);
		controleMenu.add(ang);
		
		raio = new JTextField(""+dados.getRaio());
		raio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					dados.setRaio(Integer.parseInt(raio.getText()));		
				} catch(NumberFormatException e) {
					raio.setText(""+dados.getRaio());
				}
			}
		});
		raio.setColumns(10);
		raio.setBounds(127, 16, 114, 26);
		controleMenu.add(raio);
		
		leftButton = new JButton("←");
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				robot.CurvarEsquerda(dados.getRaio(), dados.getAngulo());
//				robot.Parar(false);
				rei.setEstado(Rei.ESQUERDA);
				rei.acordar();
				logText.append("Curva Esquerda("+dados.getRaio()+ ", "+ dados.getAngulo()+")\n");
			}
		});
		leftButton.setBounds(294, 51, 73, 25);
		controleMenu.add(leftButton);
		
		rightButton = new JButton("→");
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				robot.CurvarDireita(dados.getRaio(), dados.getAngulo());
//				robot.Parar(false);
				rei.setEstado(Rei.DIREITA);
				rei.acordar();
				logText.append("Curva Direita("+dados.getRaio()+ ", "+ dados.getAngulo()+")\n");
			}
		});
		rightButton.setBounds(464, 51, 73, 25);
		controleMenu.add(rightButton);
		
		stopButton = new JButton("Parar");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				robot.Parar(true);
				rei.setEstado(Rei.PARAR);
				rei.acordar();
				logText.append("Parar");
			}
		});
		stopButton.setBounds(379, 51, 73, 25);
		controleMenu.add(stopButton);
		
		backButton = new JButton("↓");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				robot.Reta(-dados.getDistancia());
//				robot.Parar(false);
				//rei.setEstado(Rei.TRAS);
				logText.append("Reta("+-dados.getDistancia()+")\n");
			}
		});
		backButton.setBounds(379, 86, 73, 25);
		controleMenu.add(backButton);
		
		fowardButton = new JButton("↑");
		fowardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				robot.Reta(dados.getDistancia());
//				robot.Parar(false);
				rei.setEstado(Rei.FRENTE);
				rei.acordar();
				logText.append("Reta("+dados.getDistancia()+")\n");
			}
		});
		fowardButton.setBounds(379, 18, 73, 25);
		controleMenu.add(fowardButton);
		
		randomMenu = new JPanel();
		randomMenu.setBounds(10, 160, 546, 71);
		randomMenu.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Controlo do Robot Autom\u00E1tico", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(randomMenu);
		randomMenu.setLayout(null);
		
		btn8Comandos = new JButton("8 Comandos Aleatórios");
		btn8Comandos.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	rei.comandos8();
		    	rei.acordar();
		    }
		});
		btn8Comandos.setBounds(25, 28, 226, 31);
		randomMenu.add(btn8Comandos);
		
		btn16Comandos = new JButton("16 Comandos Aleatórios");
		btn16Comandos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rei.comandos16();
				rei.acordar();
			}
		});
		btn16Comandos.setBounds(300, 28, 226, 31);
		randomMenu.add(btn16Comandos);
		
		comportamentoCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comportamentoCheck.isSelected()) {
					rei.desbloquear();
				}
				else {
					rei.bloquear(true);
				}
			}
		});
	}
}
