package org.senecaut;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.knime.core.node.defaultnodesettings.SettingsModelString;

@SuppressWarnings("serial")
public class Options_Panel extends Panel {
	
	private Panel enzymes_panel;
	private Panel statistics_panel;
	private Panel cores_panel;
	
	private ArrayList<String> statistics_type;
	
	public Options_Panel() {
		super();
	}
	
	
	public void init() {
		enzymes_panel = new Enzymes_Panel();
		statistics_panel = new Statistics_Panel();
		cores_panel = new Cores_Panel();
	}

	@Override
	public void make() {
        add(enzymes_panel, constraints);
        constraints.gridy++;
        add(statistics_panel, constraints);
        constraints.gridy++;       
        add(cores_panel, constraints); 
	}
	
	public String[] getCommand() {
		return concat(
				concat(
						enzymes_panel.getCommand(),
						statistics_panel.getCommand()
						),
				cores_panel.getCommand()
				);
	}
	
	/**concatène deux tableau de string*/
	public static String[] concat(String[] A, String[] B) {
		   int aLen = A.length;
		   int bLen = B.length;
		   String[] C= new String[aLen+bLen];
		   System.arraycopy(A, 0, C, 0, aLen);
		   System.arraycopy(B, 0, C, aLen, bLen);
		   return C;
		}
}
