package makeblastdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
 
import java.net.*;

import javax.swing.JOptionPane;

/**
 * <code>NodeModel</code> for the "Make_Blast_DB" node.
 *
 * @author Arnaud Sénécaut
 */
public class Make_Blast_DBNodeModel extends NodeModel {
    
	private static final NodeLogger LOGGER = NodeLogger.getLogger(Make_Blast_DBNodeModel.class);

	//private final SettingsModelString setting_extension = new SettingsModelString("setting_extension", "");
	
	private File input, output;
    /**
     * Constructor for the node model.
     */
    protected Make_Blast_DBNodeModel() {
		super(
				new PortType[]{PortObject.TYPE}, 
				new PortType[]{PortObject.TYPE}
		);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {

    	
    	//récupere l'URI du fichier en entrée
    	input = new File (
    			URIUtil.portToURI(inData[0])
    			);
    	
    	//crée la sortie, un dossier qui contiendra les différents fichiers db
    	String inputPrefix = input.getName().substring(0, input.getName().lastIndexOf('.'));
    	String pathFolder = input.getParent() + "\\" + inputPrefix + "-RAId_Formated_DB\\";
    	File folder = new File (pathFolder);
    	
    	//si le dossier n'existe pas, (c'est la premiere fois quon le lance) 
    	//alors il le crée.
    	if (!folder.exists()) folder.mkdir();
    	
    	output = new File(
    			pathFolder +
    			inputPrefix
    			);
    	
    	//verifie les entrées et lance le programme.
    	if (verifyInput()) runFormatter();
    	
    	//ressort uniquement le .seq comme ca on peu le relier à RAIdExport
    	URI seqOutput = new File(output.getAbsolutePath() + ".seq").toURI();
    	
        return new PortObject[]{URIUtil.uriToPort(seqOutput)};
    }
    
    private boolean verifyInput() {
 		
 		if(!new File("./RAId").exists()){
 			JOptionPane.showMessageDialog(null, "<html> Warning: The RAId file could not be found, please ensure<br> that you are running RAId from the correct directory</html>");
 			return false;
 		}
 		if (input == null || output == null) { // File not selected
 			JOptionPane.showMessageDialog(null, "Please select a input and output file");
 			return false;
 		}
 		if (!input.exists()) { // File does not exist
 			JOptionPane.showMessageDialog(null, "Input file cannot be found");
 			return false;
 		}
 		File file = new File(output.getAbsolutePath() + ".seq");
 		if (file.exists()) { // The output file already exists
 			int response = JOptionPane.showConfirmDialog(null, "Do you want to overwrite " + output.getName()  + ".seq?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
 			if (response == JOptionPane.NO_OPTION) {
 				return false;
 			} else if (response == JOptionPane.YES_OPTION) {
 				return true;
 			} else if (response == JOptionPane.CLOSED_OPTION) {
 				return false;
 			}
 		}
 		return true;
 	}
 	
 	private void runFormatter() throws Exception {
 		
 		ProcessBuilder pb = 
 				new ProcessBuilder(
 						"wsl.exe ",
 						"./RAId", 
 						"-fp", 
 						toWslPath(input.getAbsolutePath()),
 						toWslPath(output.getAbsolutePath())
 						);
 		
 		
 		Process proc = pb.start();
 		proc.waitFor();
 		
 	    BufferedReader inputErr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
 	    BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
 	
 	    String line;
 		while ((line = input.readLine()) != null) LOGGER.warn("Blast db : " + line);
 		
 		String lineErr;
 		while ((lineErr = inputErr.readLine()) != null) LOGGER.warn("*** ERROR Blast db : " + lineErr); 		
 	}

 	
 	//Traduis un chemin windows en chemin wsl (exemple : D:\Users\Arnaud -> /mnt/d/Users/Arnaud) - Arnaud Sénécaut
 	static String toWslPath (String path){
 		System.out.print("#### utilisation de toWslPath : " + path + "  --->  ");

 		String wslPath = "/mnt/";

 		String Disque = String.valueOf(path.charAt(0)); // C, D, E, F ....

 		if (!path.contains(":\\")){
 			//si on entre juste le nom du fichier sans le chemin, ca le met dans /tmp
 			// :\ est le discriminant que j'ai choisis, mais ce n'est pas le meilleur.
 			wslPath = "/tmp/" + path;
 		}
 		else {
 			path = path.replaceAll(Disque + ":", ""); // retire le D: du chemin en laissant le \
 			path = path.replace('\\', '/'); //remplace les \ par des /
 			wslPath += Disque.toLowerCase() + path; // on se retrouve avec /mnt/disqueEnMinuscule/reste/du/chemin
 		}

 				System.out.println(wslPath);
 		return wslPath;
 	}

 	
    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {
        return inSpecs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         // TODO: generated method stub
    	//setting_extension.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    	//setting_extension.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	//setting_extension.validateSettings(settings);
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }

}

