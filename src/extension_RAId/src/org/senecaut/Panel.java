package org.senecaut;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Panel extends JPanel {
	
	GridBagConstraints constraints;
	
	String name;
	
	public Panel() {
		super();
		this.name = "Default name";
		constraints = new GridBagConstraints();
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.init();
		this.make();
	}
	
	public Panel(String name) {
		this();
		this.name = name;
		this.setBorder(BorderFactory.createTitledBorder(name));
	}
	
	public void init() {};
	
	public void make() {};
	
	public String[] getCommand() {
		return new String[] {};
	}
}
