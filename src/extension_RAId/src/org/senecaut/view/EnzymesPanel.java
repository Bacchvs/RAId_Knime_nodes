package org.senecaut.view;

import java.util.LinkedHashMap;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;

class EnzymesPanel extends Panel {
	private static final long serialVersionUID = -8398766650598579697L;
	private LinkedHashMap<String, String> enzymes, maxMissedClevageSites, enzymeClevageType;
	private JComboBox<String> enzymeSelection, maxMissedSelection, clevageTypeSelection;

	@Override
	public void setUpPanel() {
		setUpInputs();
		this.add(new JLabel("Enzyme Type"), constraints);

		constraints.gridx++;
		this.add(enzymeSelection, constraints);

		// Select maximum missed clevage sites
		constraints.gridx = 0;
		constraints.gridy++;
		this.add(new JLabel("Maximum Missed Clevage Sites"), constraints);

		constraints.gridx++;
		this.add(maxMissedSelection, constraints);

		// Select enzyme clevage type
		constraints.gridx = 0;
		constraints.gridy++;
		this.add(new JLabel("Cleavage Type"), constraints);

		constraints.gridx++;
		this.add(clevageTypeSelection, constraints);

		this.setBorder(BorderFactory.createTitledBorder("Enzymes"));
	}

	@Override
	public String submitJob() {
		StringBuilder s = new StringBuilder();
		String ez = "-ez " + enzymes.get((String) enzymeSelection.getSelectedItem()) + " ";
		String nmcs = "-nmcs " + ((String) maxMissedSelection.getSelectedItem()) + " ";
		String ect = "-ect " + enzymeClevageType.get((String) clevageTypeSelection.getSelectedItem()) + " ";
		return s.append(ez).append(nmcs).append(ect).toString();
	}

	private void setUpInputs() {
		enzymes = new LinkedHashMap<String, String>();
		maxMissedClevageSites = new LinkedHashMap<String, String>();
		enzymeClevageType = new LinkedHashMap<String, String>();
		// Set up enzymes, default: Trypsin (K,R)
		enzymes.put("Trypsin (K,R)", "1");
		enzymes.put("Lys-C (K)", "2");
		enzymes.put("Arg-C (R)", "3");
		enzymes.put("GluC-Phosphate (E,D)", "4");
		enzymes.put("GluC-Bicarbonate (E)", "5");
		enzymes.put("PepsinA (L,F)", "6");
		enzymes.put("Chymotrypsin (F,Y,W,L)", "7");
		enzymes.put("Cyanogen bromide (M)", "8");
		enzymes.put("Cyanogen bromide + Trypsin (M,K,R)", "9");
		enzymes.put("Chymotrypsin + Trypsin (F,Y,W,L,K,R)", "10");
		enzymes.put("V8-DE (N,D,E,Q)", "11");
		enzymes.put("V8-E (E,Q)", "12");
		enzymes.put("Trypsin + PepsinA (K,R,F,L)", "13");
		enzymes.put("Lys-N (K)", "14");
		enzymes.put("Asp-N (D,N)", "15");
		enzymes.put("Asp-N_ambic (D,E)", "16");

		enzymeSelection = new JComboBox<String>(enzymes.keySet().toArray(new String[0]));
		enzymeSelection.setSelectedIndex(0);

		// Set up max missed clevage sites option: 0 - 5, default: 2
		for (int i = 0; i <= 5; i++) {
			maxMissedClevageSites.put(String.valueOf(i), String.valueOf(i));
		}

		maxMissedSelection = new JComboBox<String>(maxMissedClevageSites.keySet().toArray(new String[0]));
		maxMissedSelection.setSelectedIndex(2);

		// Set up enzyme clevage type options, default: fully clevaged
		enzymeClevageType.put("fully enzymatic", "0");
		enzymeClevageType.put("semi-enzymatic", "1");

		clevageTypeSelection = new JComboBox<String>(enzymeClevageType.keySet().toArray(new String[0]));
		clevageTypeSelection.setSelectedIndex(0);
	}

	@Override
	public void setDefaults() {
		enzymeSelection.setSelectedIndex(0);
		maxMissedSelection.setSelectedIndex(2);
		clevageTypeSelection.setSelectedIndex(0);
	}

	@Override
	public void saveSettings(Properties props) {
		props.setProperty("enzymeType", String.valueOf((enzymeSelection.getSelectedIndex())));
		props.setProperty("maxMissedClevageSites", String.valueOf(maxMissedSelection.getSelectedIndex()));
		props.setProperty("enzymeClevageType", String.valueOf(clevageTypeSelection.getSelectedIndex()));
	}

	@Override
	public void loadSettings(Properties props) {
		enzymeSelection.setSelectedIndex(Integer.parseInt(props.getProperty("enzymeType")));
		maxMissedSelection.setSelectedIndex(Integer.parseInt(props.getProperty("maxMissedClevageSites")));
		clevageTypeSelection.setSelectedIndex(Integer.parseInt(props.getProperty("enzymeClevageType")));
	}

	@Override
	public void setParameters() {
		// Do nothing
		
	}
}
