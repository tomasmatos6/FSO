package Subdito;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI_SUBDITO extends GUI_GENERICA {
	private static final long serialVersionUID = 1L;
	private JTextField nomeRobot;
	JRadioButton rdbtnAbrirfecharRobot;
	
	public GUI_SUBDITO(Subdito subdito) {
		super(subdito);
		this.setTitle("GUI Subdito");
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Robot", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 262, 545, 62);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblRobot = new JLabel("Nome do Robot");
		lblRobot.setBounds(12, 30, 118, 15);
		panel.add(lblRobot);
		
		nomeRobot = new JTextField();
		nomeRobot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dados.setNomeRobot(nomeRobot.getText());
				logText.append(nomeRobot.getText());
			}
		});
		nomeRobot.setBounds(148, 25, 176, 25);
		panel.add(nomeRobot);
		nomeRobot.setColumns(10);
		
		rdbtnAbrirfecharRobot = new JRadioButton("Abrir/Fechar Robot");
		rdbtnAbrirfecharRobot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!dados.isOpenClose()) {
						boolean open = dados.getRobot().OpenEV3(dados.getNomeRobot());
						if(open) rdbtnAbrirfecharRobot.setSelected(true);
						dados.setOpenClose(true);
						logText.append("isOpenClose"+rdbtnAbrirfecharRobot.isSelected()+"\n");
						subdito.setEstadoRobot(Subdito.LER_MEMORIA);
					}
					else {
						dados.getRobot().CloseEV3();
						dados.setOnOff(false);
						logText.append("isOpenClose"+rdbtnAbrirfecharRobot.isSelected()+"\n");
					}
				} catch(Exception exp) {
					logText.append("Robot não conectado \n");
				}
				
			}
		});
		rdbtnAbrirfecharRobot.setBounds(366, 26, 157, 23);
		panel.add(rdbtnAbrirfecharRobot);
		
		
	
	}
}
