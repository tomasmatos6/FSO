package Rei;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;

public class GUI_REI extends GUI_GENERICA {
	public GUI_REI(Rei rei) {
		super(rei);
		this.setTitle("GUI Rei");
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Controlo do Robot Autom\u00E1tico", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 258, 546, 71);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btn8Comandos = new JButton("8 Comandos Aleatórios");
		btn8Comandos.setBounds(25, 28, 226, 31);
		panel.add(btn8Comandos);
		
		JButton btn16Comandos = new JButton("16 Comandos Aleatórios");
		btn16Comandos.setBounds(300, 28, 226, 31);
		panel.add(btn16Comandos);
	}
	public static void main(String[] args) {
		Rei rei = new Rei();
		GUI_REI gui = new GUI_REI(rei);
	}
}
