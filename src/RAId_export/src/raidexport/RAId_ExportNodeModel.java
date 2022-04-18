package raidexport;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
 
/**
 * traduce R output files into one single file using R.
 *
 * @author Arnaud Sénécaut
 */
public class RAId_ExportNodeModel extends NodeModel {
    
    /**
	 * The logger is used to print info/warning/error messages to the KNIME console
	 * and to the KNIME log file. Retrieve it via 'NodeLogger.getLogger' providing
	 * the class of this node model.
	 */
	private static final NodeLogger LOGGER = NodeLogger.getLogger(RAId_ExportNodeModel.class);

	 
	private final SettingsModelString setting_fastaPath = new SettingsModelString("setting_fastaPath", "");
	//private final SettingsModelString setting_prefix = new SettingsModelString("setting_prefix", "unamed");
	private final SettingsModelString setting_sufix = new SettingsModelString("setting_sufix", "idXML");
	private final SettingsModelDouble setting_EValue = new SettingsModelDouble("setting_EValue", 0.001);

	
	/**Constructor of PortObject version*/
	protected RAId_ExportNodeModel() {
		super(
				new PortType[]{PortObject.TYPE}, 
				new PortType[]{PortObject.TYPE}
		);
	}
	
	/**execute of PortObject function*/
	protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
	throws Exception {
		//traduis l'entrée en URI via URIUtil
		URI input = URIUtil.portToURI(inData[0]);
		
		//appelle la fonction en commun avec la dataCell version
		URI output = meta_exec(input);
		
		//retourne un PortObject grace à URIUtil
		return new PortObject[] {URIUtil.uriToPort(output)};	
	}
	
	/**fonction configure pour PortObject*/
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] { inSpecs[0]};
	}
	
	
	
	
//	/**
//   * the constructor of datacell version
//	 */
//	protected RAId_ExportNodeModel() {
//		super(1, 1);
//	}

//	/**
//	 * the execute of datacell version
//	 * {@inheritDoc}
//	 */
//	@Override
//	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
//			throws Exception {
//      URI input = URIUtil.URIFromDataCell(inData[0].iterator().next().getCell(0));
//      URI output = meta_exec(input);
//
//  	BufferedDataContainer container = exec.createDataContainer(inData[0].getSpec());
//   	container.addRowToTable(new DefaultRow(new RowKey("URI"), URIUtil.DataCellFromURI(output)));
//		container.close();
//		return new BufferedDataTable[] {container.getTable()};
//	}

//	/**fonction configure pour dataCell*/
//	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
//		return new DataTableSpec[] { inSpecs[0]};
//	}
	
	
	
    /**fonction en commun, que l'on utilise un port object ou un dataCell
     */
	private URI meta_exec(URI inputRAIdFiles) throws Exception {
		LOGGER.warn("Lancement de RAId_Export !");
		//récupere le chemin du fichier
		File RAIdFile = new File(inputRAIdFiles);
		 
		
		//pour le chercher intégré dans le jar mais ne marche pas pour l'instant
		//File RAId_Export = new File (FileLocator.toFileURL(new URL(getClass().getResource("RAId_Export").toString().replaceAll(" ", "%20"))).toURI());
		
		
		///Lance la commande d'éxécution de R dans le répertoire spécifié
		 ProcessBuilder pb = new ProcessBuilder(
						"wsl.exe", 
						//chemin de RAId_Export
						//toWslPath(RAId_Export.getPath()),
						"./RAId_Export",
						//chemin du dossier contenant les fichier de RAId
						toWslPath(RAIdFile.getParent()) + "/",
						//le nom du fichier
						RAIdFile.getName(), 
						//the sufixe of the file
						setting_sufix.getStringValue(),
						//the evalue
						setting_EValue.getDoubleValue()+" ",
						//the fasta file
						toWslPath(setting_fastaPath.getStringValue())						
						);
		//pb.directory(new File("D:\\StageCNRS\\workSpace\\RAId_Export\\bin\\raidexport"));
		Process proc = pb.start();
		
		proc.waitFor();
		
		
		
        BufferedReader inputErr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String lineErr;
		String line;
		while ((line = input.readLine()) != null) LOGGER.warn(line);
		while ((lineErr = inputErr.readLine()) != null) LOGGER.warn("*** ERROR *** " + lineErr);
		
		
				
		LOGGER.warn("Valeur de sortie : " + proc.exitValue());
		
		//crée un URI vers le fichier créé
		URI finalOutputFile = URI.create(inputRAIdFiles.getPath() + "." + setting_sufix.getStringValue());
		
		return finalOutputFile;
	}
	


	 
	private DataTableSpec createOutputSpec(DataTableSpec inputTableSpec) {
		return new DataTableSpec(inputTableSpec.getColumnSpec(0));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		setting_fastaPath.saveSettingsTo(settings);
		setting_sufix.saveSettingsTo(settings);
		setting_EValue.saveSettingsTo(settings);
		//setting_prefix.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		setting_fastaPath.loadSettingsFrom(settings);
		setting_sufix.loadSettingsFrom(settings);
		setting_EValue.loadSettingsFrom(settings);
		//setting_prefix.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		setting_fastaPath.validateSettings(settings);
		setting_sufix.validateSettings(settings);
		setting_EValue.validateSettings(settings);
		//setting_prefix.validateSettings(settings);
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	@Override
	protected void reset() {
	}
	
	//Traduis un chemin windows en chemin wsl (exemple : D:\Users\Arnaud -> /mnt/d/Users/Arnaud) - Arnaud Sénécaut
		private String toWslPath (String path){
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


	
}

