package Subdito;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import comunicacao.Dados;
import utils.Gui_Generica;

public class Gui_Subdito extends Gui_Generica {
	private static final long serialVersionUID = 1L;
	JTextField nomeRobot;
	JRadioButton rdbtnAbrirfecharRobot;
	private JTextField dist, ang, raio;
	private JPanel controleMenu;
	private JButton leftButton, rightButton, backButton, fowardButton, stopButton;
	protected String nomeRobotString;
	private Subdito subdito;
	
	private int distInt, angInt;
	private double raioDouble;
	
	public Gui_Subdito(Subdito subdito) {
		logText.setBounds(10, 299, 549, 217);
		comportamentoCheck.setBounds(10, 255, 254, 23);
		setTitle("Subdito");
		initialize();
		this.subdito = subdito;
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
		btnLimparLog.setEnabled(b);
		comportamentoCheck.setEnabled(b);
		nomeRobot.setEnabled(b);
		rdbtnAbrirfecharRobot.setEnabled(b);
	}

	private void initialize() {
		setLocation(800, 100);
		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 545, 62);
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Robot", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(panel);
		
		
		JLabel lblRobot = new JLabel("Nome do Robot");
		lblRobot.setBounds(12, 30, 118, 15);
		panel.add(lblRobot);
		
		nomeRobot = new JTextField();
		nomeRobot.setBounds(148, 25, 176, 25);
		nomeRobot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logText.append(nomeRobot.getText());
			}
		});
		panel.add(nomeRobot);
		nomeRobot.setColumns(10);
		
		rdbtnAbrirfecharRobot = new JRadioButton("Abrir/Fechar Robot");
		rdbtnAbrirfecharRobot.setBounds(366, 26, 157, 23);
		rdbtnAbrirfecharRobot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(rdbtnAbrirfecharRobot.isSelected()) {
						subdito.setEstado(Subdito.ABRIR_ROBOT);
						//subdito.getRobot().OpenEV3(nomeRobotString);
					}
					else {
						subdito.setEstado(Subdito.FECHAR_ROBOT);
						//subdito.getRobot().CloseEV3();
					}
					logText.append("isOpenClose"+rdbtnAbrirfecharRobot.isSelected()+"\n");
				} catch(Exception exp) {
					exp.printStackTrace();
					logText.append("Robot não conectado \n");
				}
				
			}
		});
		panel.add(rdbtnAbrirfecharRobot);
		
		controleMenu = new JPanel();
		controleMenu.setBounds(10, 82, 546, 128);
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
		
		dist = new JTextField("20");
		dist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					distInt = Integer.parseInt(dist.getText());				
				} catch(NumberFormatException e) {
					dist.setText(""+distInt);
				}
			}
		});
		dist.setColumns(10);
		dist.setBounds(127, 87, 114, 26);
		controleMenu.add(dist);
		
		ang = new JTextField("90");
		ang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					angInt = Integer.parseInt(ang.getText());				
				} catch(NumberFormatException e) {
					ang.setText(""+angInt);
				}
			} 
		});
		ang.setColumns(10);
		ang.setBounds(127, 54, 114, 26);
		controleMenu.add(ang);
		
		raio = new JTextField("10");
		raio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					raioDouble = Double.parseDouble(raio.getText());		
				} catch(NumberFormatException e) {
					raio.setText(""+raioDouble);
				}
			}
		});
		raio.setColumns(10);
		raio.setBounds(127, 16, 114, 26);
		controleMenu.add(raio);
		
		leftButton = new JButton("←");
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subdito.getRobot().CurvarEsquerda(raioDouble, angInt);
				subdito.getRobot().Parar(false);
				//rei.setEstado(Rei.ESQUERDA);
				logText.append("Curva Esquerda("+raioDouble + ", "+ angInt+")\n");
			}
		});
		leftButton.setBounds(294, 51, 73, 25);
		controleMenu.add(leftButton);
		
		rightButton = new JButton("→");
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subdito.getRobot().CurvarDireita(raioDouble, angInt);
				subdito.getRobot().Parar(false);
				//rei.setEstado(Rei.DIREITA);
				logText.append("Curva Direita(" + raioDouble + ", "+ angInt +")\n");
			}
		});
		rightButton.setBounds(464, 51, 73, 25);
		controleMenu.add(rightButton);
		
		stopButton = new JButton("Parar");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subdito.getRobot().Parar(true);
				//rei.setEstado(Rei.PARAR);
				logText.append("Parar");
			}
		});
		stopButton.setBounds(379, 51, 73, 25);
		controleMenu.add(stopButton);
		
		backButton = new JButton("↓");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subdito.getRobot().Reta(-distInt);
				subdito.getRobot().Parar(false);
				//rei.setEstado(Rei.TRAS);
				logText.append("Reta("+-distInt+")\n");
			}
		});
		backButton.setBounds(379, 86, 73, 25);
		controleMenu.add(backButton);
		
		fowardButton = new JButton("↑");
		fowardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				subdito.getRobot().Reta(distInt);
				subdito.getRobot().Parar(false);
				//rei.setEstado(Rei.FRENTE);
				logText.append("Reta("+distInt+")\n");
			}
		});
		fowardButton.setBounds(379, 18, 73, 25);
		controleMenu.add(fowardButton);
	}
}
