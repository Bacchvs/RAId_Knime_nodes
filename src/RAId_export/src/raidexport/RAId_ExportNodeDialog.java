package raidexport;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * @author Arnaud Sénécaut
 */
public class RAId_ExportNodeDialog extends DefaultNodeSettingsPane {


    protected RAId_ExportNodeDialog() {
        super();
        
        DialogComponentFileChooser fastaFileChooser = new DialogComponentFileChooser(new SettingsModelString("setting_fastaPath", ""), ".", 0, false, ".fasta", "");
		fastaFileChooser.setBorderTitle("Select a fasta file");
		addDialogComponent(fastaFileChooser);
		
		
		
		
		addDialogComponent(
				new DialogComponentStringSelection(
						new SettingsModelString("setting_sufix", "idXML"), 
						"Select Extension : ",
						Arrays.asList(new String[] {"idXML", "mzid", "PepXML"})
						)
				);
		
		addDialogComponent(
				new DialogComponentNumberEdit(
						new SettingsModelDouble("setting_EValue", 0.001), 
						"Eval : "
				));
	
		//addDialogComponent(new DialogComponentTextBox());
		
    }
}

