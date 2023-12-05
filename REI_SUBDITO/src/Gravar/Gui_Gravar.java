package Gravar;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import utils.Gui_Generica;

import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

public class Gui_Gravar extends Gui_Generica {
	private JTextField pathFicheiro;
	private JPanel gravarMenu;
	private JButton ficheiroButton;
	private Gravar gravar;
	private String path;
	
	public Gui_Gravar(Gravar gravar) {
		setTitle("Gravar");
		this.gravar = gravar;
		initialize();
	}
	
	public String getPath() {
		return path;
	}

	private void initialize() {
		setBounds(100, 100, 580, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		gravarMenu = new JPanel();
		gravarMenu.setLayout(null);
		gravarMenu.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Menu Gravar", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		gravarMenu.setBounds(12, 12, 546, 119);
		getContentPane().add(gravarMenu);
		
		JLabel lblNewLabel = new JLabel("Ficheiro onde gravar");
		lblNewLabel.setBounds(12, 28, 134, 15);
		gravarMenu.add(lblNewLabel);
		
		pathFicheiro = new JTextField();
		pathFicheiro.setColumns(10);
		pathFicheiro.setBounds(120, 23, 343, 25);
		gravarMenu.add(pathFicheiro);
		
		ficheiroButton = new JButton("...");
		ficheiroButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(".\\");
				
				int dialog = chooser.showOpenDialog(gravarMenu);
				
				if(dialog==JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					path = file.getPath();
					
					pathFicheiro.setText(path);
				}
				
			}
		});
		ficheiroButton.setBounds(488, 23, 35, 25);
		gravarMenu.add(ficheiroButton);
		
		JToggleButton gravarBtn = new JToggleButton("Gravar");
		gravarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(gravarBtn.isSelected()) {
					gravar.setEstado(Gravar.GRAVAR_FICHEIRO);
					gravarBtn.setText("Parar");
				} else {
					gravar.setEstado(Gravar.GRAVAR);
					gravarBtn.setText("Gravar");
				}
			}
		});
		gravarBtn.setBounds(118, 58, 134, 40);
		gravarMenu.add(gravarBtn);
		
		JToggleButton reproduzirBtn = new JToggleButton("Reproduzir");
		reproduzirBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(reproduzirBtn.isSelected()) {
					reproduzirBtn.setText("Parar");
				} else {
					reproduzirBtn.setText("Reproduzir");
				}
			}
		});
		reproduzirBtn.setBounds(329, 58, 134, 40);
		gravarMenu.add(reproduzirBtn);
	}
}
