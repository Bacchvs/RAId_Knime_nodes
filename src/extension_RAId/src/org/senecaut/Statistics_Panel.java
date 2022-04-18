package org.senecaut;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class Statistics_Panel extends Panel {

	private ArrayList<String> statistics_type;
	
	private String[] names;
	
	private ArrayList<JCheckBox> jckb_statistics;
	
	private JComboBox<String> jcb_statistics;
	
	
	
	public Statistics_Panel(){
		super("Statistics");
	}
	
	public void init() {		
 	///// menu statistics
 		/// type de statistique
		statistics_type = new ArrayList<String>();
 		
		statistics_type.add("CLT-extended RAId score");
		statistics_type.add("Gumbel EVD RAId score");
		statistics_type.add("Gumbel EVD Kscore");
		statistics_type.add("Gumbel EVD Hyperscore");
		statistics_type.add("Gumbel EVD XCorr");
		statistics_type.add("APPS RAId score");
		statistics_type.add("APPS Kscore");
		statistics_type.add("APPS Hyperscore");
		statistics_type.add("APPS XCorr");
		
		names = new String[]
				{
				"a-H", "a", "a+H", "b-NH3", "b-H2O", "b-H", "b", "b+H", "c-H", "c", "c+H",
				"x-H", "x", "x+H", "y-NH3", "y-H2O", "y-H", "y", "y+H", "z-H", "z", "Z+H"
				};
	}

	@Override
	public void make() {
    ///menu Statistics
        add(new JLabel("Statistics"), constraints);
        
        constraints.gridx++;
		jcb_statistics = new JComboBox<String>(statistics_type.toArray(new String[0]));
        add(jcb_statistics, constraints);
        
        constraints.gridx = 0;
        constraints.gridy++;
        
        
        
        ////////// serie to score
        add(new JLabel("Serie to score"), constraints);
        
        constraints.gridx++;
        Panel p_checkBoxes = new Panel();
        p_checkBoxes.constraints.insets = new Insets(0, 0, 0, 0);
        
        jckb_statistics = new ArrayList<JCheckBox>();
        for (int i = 0; i<names.length; i++) {
        	JCheckBox jcb = new JCheckBox(names[i]);
        	jckb_statistics.add(jcb);
        	p_checkBoxes.add(jcb, p_checkBoxes.constraints);
        	if (i==10) {
        		p_checkBoxes.constraints.gridx = 0;
        		p_checkBoxes.constraints.gridy = 1;
        	}
        	else p_checkBoxes.constraints.gridx++;
        }
        
        add(p_checkBoxes, constraints);
	}

	public String[] getCommand() {
		String statisticsChecked = "-ssr";
		String[] sb = new String[] {};
		String[] ex = {"-ex",  "1",  "-dsv", "0"}; // Default, CLT Rvalue
		int option = jcb_statistics.getSelectedIndex()-1; // statistics selected by the user

		switch (option) {
		case 0: // Gumbel EVD Rvalue
			ex[1] = "4";
			//ex = "-ex 4 -dsv 0 ";
			statisticsChecked = "-ssr";
			break;
		case 1: // Kscore
			ex[1] = "4"; ex[3] = "1";
			//ex = "-ex 4 -dsv 1 ";
			statisticsChecked = "-ssk";
			break;
		case 2: // Hyperscore
			ex[1] = "4"; ex[3] = "2";
			//ex = "-ex 4 -dsv 2 ";
			statisticsChecked = "-ssh";
			break;
		case 3: // XCorr
			ex[1] = "4"; ex[3] = "3";
			//ex = "-ex 4 -dsv 3 ";
			statisticsChecked = "-ssx";
			break;
		case 4: // APS Rvalue
			ex[1] = "2"; ex[3] = "0";
			//ex = "-ex 2 -dsv 0 ";
			statisticsChecked = "-ssr";
			break;
		case 5: // Kscore
			ex[1] = "2"; ex[3] = "1";
			//ex = "-ex 2 -dsv 1 ";
			statisticsChecked = "-ssk";
			break;
		case 6: // Hyperscore
			ex[1] = "2"; ex[3] = "2";
			//ex = "-ex 2 -dsv 2 ";
			statisticsChecked = "-ssh";
			break;
		case 7: // XCorr
			ex[1] = "2"; ex[3] = "3";
			//ex = "-ex 2 -dsv 3 ";
			statisticsChecked = "-ssx";
			break;
		}
		
		
		
		String ions = "";
		int boxesChecked = 0;
		for (JCheckBox box : jckb_statistics) {
			if (box.isSelected()) {
				ions += box.getText() + ",";
				boxesChecked++;
			}
		}
		
		//if (boxesChecked == 0)statisticsChecked = "";
		sb = Options_Panel.concat(sb, new String[] {statisticsChecked, ions});
		
		return Options_Panel.concat(ex, sb);
		}
	}

