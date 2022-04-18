package org.senecaut;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class Cores_Panel extends Panel {
	
	private JComboBox<Integer> listeNbCores;

	public Cores_Panel() {
		super("Cores");
	}
	
	
	public void init() {
		listeNbCores = new JComboBox<Integer>();
		for (int i = 1; i<=Runtime.getRuntime().availableProcessors(); i++) {
			listeNbCores.addItem(i);
		}
	}
	
	public void make () {
		constraints.gridx = 0;
		add(new JLabel("Number of cores : "));
		constraints.gridx ++;
		add(listeNbCores);
	}

	public String getCommand() {
		return "-nc " +listeNbCores.getSelectedItem().toString();
	}
}
