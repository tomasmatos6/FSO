package ReiSubdito;
import javax.swing.JCheckBox;

import utils.Gui_Generica;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Gui_ReiSubdito extends Gui_Generica {
	public Gui_ReiSubdito(ReiSubdito rs) {
		setTitle("REI_SUBDITO");
		
		JCheckBox reiAtivo = new JCheckBox("REI");
		reiAtivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rs.reiControl(reiAtivo.isSelected());
			}
		});
		reiAtivo.setBounds(12, 23, 93, 21);
		getContentPane().add(reiAtivo);
		
		JCheckBox subditoAtivo = new JCheckBox("SUBDITO");
		subditoAtivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rs.subditoControl(subditoAtivo.isSelected());
			}
		});
		subditoAtivo.setBounds(226, 23, 93, 21);
		getContentPane().add(subditoAtivo);
		
		JCheckBox gravarAtivo = new JCheckBox("GRAVAR");
		gravarAtivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rs.gravarControl(gravarAtivo.isSelected());
			}
		});
		gravarAtivo.setBounds(468, 23, 93, 21);
		getContentPane().add(gravarAtivo);
	}
}
