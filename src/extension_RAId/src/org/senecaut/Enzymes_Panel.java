package org.senecaut;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class Enzymes_Panel extends Panel {
	private ArrayList<String> enzymes;
	private ArrayList<String> clivages_sites;
	private ArrayList<String> clivages_type;
	
	private JComboBox<String> jcb_enzyme;
    private JComboBox<Integer> jcb_clivages_sites;
    private JComboBox<String> jcb_clivages_type;
    
	
	
	public Enzymes_Panel(){
		super("Enzymes");	
	}
	
	
	public void init() {	
		enzymes = new ArrayList<String>();
		clivages_sites = new ArrayList<String>();
		clivages_type = new ArrayList<String>();
	///// enzymes
    	enzymes.add("Trypsin (K,R)");
    	enzymes.add("Lys-C (K)");
    	enzymes.add("Arg-C (R)");
    	enzymes.add("GluC-Phosphate (E,D)");
    	enzymes.add("GluC-Bicarbonate (E)");
    	enzymes.add("PepsinA (L,F)");
    	enzymes.add("Chymotrypsin (F,Y,W,L)");
    	enzymes.add("Cyanogen bromide (M)");
    	enzymes.add("Cyanogen bromide + Trypsin (M,K,R)");
    	enzymes.add("Chymotrypsin + Trypsin (F,Y,W,L,K,R)");
    	enzymes.add("V8-DE (N,D,E,Q)");
    	enzymes.add("V8-E (E,Q)");
    	enzymes.add("Trypsin + PepsinA (K,R,F,L)");
    	enzymes.add("Lys-N (K)");
    	enzymes.add("Asp-N (D,N)");
    	enzymes.add("Asp-N_ambic (D,E)");
    	
    	
    	////// clivages_sites
 		for (int i = 0; i <= 5; i++) {
 			clivages_sites.add(String.valueOf(i));
 		}
 		
 		/////clivage_type
 		clivages_type.add("fully enzymatic");
 		clivages_type.add("semi-enzymatic");
	}

	@Override
	public void make() {
	//// menu enzyme
		//enzyme
        jcb_enzyme = new JComboBox<String>(enzymes.toArray(new String[enzymes.size()]));
        
        //nombre de site de clivages
        jcb_clivages_sites = new JComboBox<Integer>(new Integer[] {0, 1, 2, 3, 4, 5});
        jcb_clivages_sites.setSelectedIndex(2);
        
        // type de clivage
        jcb_clivages_type = new JComboBox<String>(clivages_type.toArray(new String[2]));
        
        //mise en place
        //enz type
        constraints.gridx = 0;
        add(new JLabel("Enzyme Type"), constraints);
        constraints.gridx++;
        add(jcb_enzyme);
        
        //site
        constraints.gridx = 0;
		constraints.gridy++;
		add(new JLabel("Maximum Missed Clevage Sites"), constraints);
		constraints.gridx++;
        add(jcb_clivages_sites, constraints);
        
        //cliv type
        constraints.gridx = 0;
		constraints.gridy++;
		add(new JLabel("Cleavage Type"), constraints);
		constraints.gridx++;
        add(jcb_clivages_type, constraints);
	}


	@Override
	public String[] getCommand() {
		return 
				new String[] {
						"-ez", String.valueOf(jcb_enzyme.getSelectedIndex() +1),
						"-nmcs", String.valueOf(jcb_clivages_sites.getSelectedItem()),
						"-ect", String.valueOf(jcb_clivages_type.getSelectedIndex())
		};
	}
}
