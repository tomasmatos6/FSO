package ReiSubdito;
import javax.swing.JCheckBox;

import utils.Gui_Generica;

public class Gui_ReiSubdito extends Gui_Generica {
	public Gui_ReiSubdito() {
		setTitle("REI_SUBDITO");
		
		JCheckBox reiAtivo = new JCheckBox("REI");
		reiAtivo.setBounds(12, 23, 93, 21);
		getContentPane().add(reiAtivo);
		
		JCheckBox subditoAtivo = new JCheckBox("SUBDITO");
		subditoAtivo.setBounds(226, 23, 93, 21);
		getContentPane().add(subditoAtivo);
		
		JCheckBox gravarAtivo = new JCheckBox("GRAVAR");
		gravarAtivo.setBounds(468, 23, 93, 21);
		getContentPane().add(gravarAtivo);
	}
	
	public static void main(String[] args) {
		new Gui_ReiSubdito();
	}
}
