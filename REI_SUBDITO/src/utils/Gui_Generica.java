package utils;

import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

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
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.SpinnerNumberModel;
import javax.swing.JScrollPane; 


public class Gui_Generica extends JFrame{
	public JTextArea logText;
	protected JButton btnLimparLog;
	protected JCheckBox comportamentoCheck;
	private JScrollPane scrollPane;
	
	/**
	 * Create the application.
	 */
	public Gui_Generica() {
		setTitle("Trab 1 - GUI");
		// instancia das minhas variaveis
		initialize();	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 580, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		
		
		comportamentoCheck = new JCheckBox("Bloquear/Desbloquear processo");
		comportamentoCheck.setSelected(true);
		
		comportamentoCheck.setBounds(10, 255, 254, 23);
		getContentPane().add(comportamentoCheck);
		
		JLabel lblLog = new JLabel("Log");
		lblLog.setBounds(10, 279, 39, 15);
		getContentPane().add(lblLog);
		
		btnLimparLog = new JButton("Limpar Log");
		btnLimparLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logText.removeAll();
			}
		});
		btnLimparLog.setBounds(10, 528, 549, 25);
		getContentPane().add(btnLimparLog);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 299, 549, 217);
		getContentPane().add(scrollPane);
		logText = new JTextArea();
		scrollPane.setViewportView(logText);
		
		
		
		setVisible(true);
	}
}
