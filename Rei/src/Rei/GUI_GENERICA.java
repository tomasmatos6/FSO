package Rei;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import robot.RobotLegoEV3;

import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

import java.io.BufferedReader; 
import java.io.FileReader; 
import java.io.IOException; 


public class GUI_GENERICA extends JFrame{
	private JTextField pathFicheiro;
	private JTextField dist, ang, raio;
	protected JTextArea logText;
	private JPanel canalMenu;
	private JRadioButton openClose;
	private JPanel controleMenu;
	private JButton ficheiroButton, leftButton, rightButton, backButton, fowardButton, stopButton, btnLimparLog;
	private JCheckBox comportamentoCheck;
	private Rei rei;
	
	// minhas variaveis
	protected Dados dados;
	private RobotLegoEV3 robot;
	
	
	public void run() {
		
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Rei rei = new Rei();
		GUI_GENERICA frame = new GUI_GENERICA(rei);
	}

	// inicialização das minhas variáveis
	public void initVariaveis() {
		dados = new Dados();
		robot = dados.getRobot();
	}
	
	/**
	 * Create the application.
	 */
	public GUI_GENERICA(Rei rei) {
		setTitle("Trab 1 - GUI");
		// instancia das minhas variaveis
		initVariaveis();
		initialize();	
		this.rei = rei;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 580, 680);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		canalMenu = new JPanel();
		canalMenu.setLayout(null);
		canalMenu.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Canal de Comunica\u00E7\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		canalMenu.setBounds(12, 12, 546, 93);
		getContentPane().add(canalMenu);
		
		JLabel lblNewLabel = new JLabel("Ficheiro do Canal");
		lblNewLabel.setBounds(12, 28, 134, 15);
		canalMenu.add(lblNewLabel);
		
		pathFicheiro = new JTextField();
		pathFicheiro.setColumns(10);
		pathFicheiro.setBounds(164, 23, 299, 25);
		canalMenu.add(pathFicheiro);
		
		ficheiroButton = new JButton("...");
		ficheiroButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(".\\GUI_GENERICA\\");
				
				int dialog = chooser.showOpenDialog(canalMenu);
				
				if(dialog==JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String path = file.getPath();
					dados.setCanalPath(path);
					pathFicheiro.setText(path);
				}
				
			}
		});
		ficheiroButton.setBounds(488, 23, 35, 25);
		canalMenu.add(ficheiroButton);
		
		JLabel lblNMsg = new JLabel("Nº msg");
		lblNMsg.setBounds(12, 65, 70, 15);
		canalMenu.add(lblNMsg);
		
		JSpinner nMsg = new JSpinner();
		nMsg.setBounds(164, 60, 70, 25);
		canalMenu.add(nMsg);
		
		openClose = new JRadioButton("Abrir Canal");
		openClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		openClose.setBounds(351, 60, 112, 25);
		canalMenu.add(openClose);
		
		controleMenu = new JPanel();
		controleMenu.setLayout(null);
		controleMenu.setBorder(new TitledBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)), "Controlo do Robot", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		controleMenu.setBounds(12, 121, 546, 127);
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
		leftButton.setEnabled(false);
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				robot.CurvarEsquerda(dados.getRaio(), dados.getAngulo());
//				robot.Parar(false);
				rei.setEstado(Rei.ESQUERDA);
				logText.append("Curva Esquerda("+dados.getRaio()+ ", "+ dados.getAngulo()+")\n");
			}
		});
		leftButton.setBounds(294, 51, 73, 25);
		controleMenu.add(leftButton);
		
		rightButton = new JButton("→");
		rightButton.setEnabled(false);
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				robot.CurvarDireita(dados.getRaio(), dados.getAngulo());
//				robot.Parar(false);
				rei.setEstado(Rei.DIREITA);
				logText.append("Curva Direita("+dados.getRaio()+ ", "+ dados.getAngulo()+")\n");
			}
		});
		rightButton.setBounds(464, 51, 73, 25);
		controleMenu.add(rightButton);
		
		stopButton = new JButton("Parar");
		stopButton.setEnabled(false);
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				robot.Parar(true);
				rei.setEstado(Rei.PARAR);
				logText.append("Parar");
			}
		});
		stopButton.setBounds(379, 51, 73, 25);
		controleMenu.add(stopButton);
		
		backButton = new JButton("↓");
		backButton.setEnabled(false);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				robot.Reta(-dados.getDistancia());
//				robot.Parar(false);
				rei.setEstado(Rei.TRAS);
				logText.append("Reta("+-dados.getDistancia()+")\n");
			}
		});
		backButton.setBounds(379, 86, 73, 25);
		controleMenu.add(backButton);
		
		fowardButton = new JButton("↑");
		fowardButton.setEnabled(false);
		fowardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				robot.Reta(dados.getDistancia());
//				robot.Parar(false);
				rei.setEstado(Rei.FRENTE);
				logText.append("Reta("+dados.getDistancia()+")\n");
			}
		});
		fowardButton.setBounds(379, 18, 73, 25);
		controleMenu.add(fowardButton);
		
		comportamentoCheck = new JCheckBox("Ativar/Desativar comportamento");
		comportamentoCheck.setBounds(8, 333, 254, 23);
		getContentPane().add(comportamentoCheck);
		
		JLabel lblLog = new JLabel("Log");
		lblLog.setBounds(12, 364, 39, 15);
		getContentPane().add(lblLog);
		
		btnLimparLog = new JButton("Limpar Log");
		btnLimparLog.setBounds(12, 613, 549, 25);
		getContentPane().add(btnLimparLog);
		
		logText = new JTextArea();
		logText.setBounds(12, 384, 549, 217);
		getContentPane().add(logText);
		
		if(dados.isOnOff()) {
			fowardButton.setEnabled(true);
			backButton.setEnabled(true);
			leftButton.setEnabled(true);
			rightButton.setEnabled(true);
			stopButton.setEnabled(true);
		}
		
		setVisible(true);
	}
}
