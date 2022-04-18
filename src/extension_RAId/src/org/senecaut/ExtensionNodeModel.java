package org.senecaut;
  
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.knime.core.data.DataCell;
 
import org.senecaut.util.URIUtil;

import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;/**
 * The node model to the demo default dialog.
 * Senextension ?
 * Senex 
 */

public class ExtensionNodeModel extends NodeModel {

	NodeLogger LOGGER;
	
 
	//une instance du factory qui permet de chercher les derniers parametres inscrits
	private final ExtensionNodeFactory FACTORY;
	
	protected ExtensionNodeModel(ExtensionNodeFactory extensionNodeFactory) {
		 super(
				 new PortType[] {PortObject.TYPE, PortObject.TYPE}, 
				 new PortType[] {PortObject.TYPE}
				 );
		 
		LOGGER = NodeLogger.getLogger(ExtensionNodeModel.class);
		

		FACTORY = extensionNodeFactory;
		LOGGER.warn("### NODE créé");
	}
	

//	protected BufferedDataTable[] execute(final BufferedDataTable[] input, final ExecutionContext exec)
//			throws Exception {
//		LOGGER.warn("### Lancement du node"); 
//		
//		URI rawFileURI = URIUtil.URIFromDataCell(input[0].iterator().next().getCell(0));
//		URI dbFileURI = URIUtil.URIFromDataCell(input[1].iterator().next().getCell(0));
//		
//		File rawFile = new File(rawFileURI);
//		File dbFile = new File(dbFileURI);
//
//		////////
//		Processus.class.getResource("RAId").toURI();
//		
//		///////
//		LOGGER.warn("commande : " + FACTORY.DIALOG.option_panel.getCommand());
//		Processus p = new Processus(LOGGER, "nul");
//		p.run();
//		 
//		sp = input[0].getSpec();
//		
//		BufferedDataContainer container = exec.createDataContainer(sp);
//		
//   		container.addRowToTable(new DefaultRow(new RowKey("URI"), URIUtil.DataCellFromURI(rawFileURI)));
//		container.close();
//				return new BufferedDataTable[] {container.getTable()};
//	}
	
	
	protected PortObject[] execute(final PortObject[] input, final ExecutionContext exec) 
			throws Exception
			  {
			LOGGER.warn("### Lancement du node"); 
			
			File rawFile = new File (URIUtil.portToURI(input[0]));
			 
			File dbFile = new File (URIUtil.portToURI(input[1]));
			
			//retire l'extension du fichier raw pour en faire un nom générique de sortie
			String pathOfRawFile =  rawFile.getAbsolutePath();
			String pathOfRAIdFiles = pathOfRawFile.substring(0, pathOfRawFile.lastIndexOf('.'));
			File sortie = new File(pathOfRAIdFiles);
			
			
			//LOGGER.warn(rawFile.getAbsolutePath() + " " + dbFile.getAbsolutePath());
			
			
			//crée la partie de la commande qui spécifie les fichiers
			String[] files = new String[] { 
					"-ip", Processus.toWslPath(rawFile.getAbsolutePath()),
					"-db", Processus.toWslPath(dbFile.getAbsolutePath()) ,
					"-op", Processus.toWslPath(sortie.getParent()) + "/" ,
					"-of", Processus.toWslPath(sortie.getName())
			};
			
			
			//crée et lance le processus
			Processus p = new Processus(LOGGER,
					exec,
					Options_Panel.concat(files, 
							FACTORY.DIALOG.option_panel.getCommand()
							)
					);
			p.run(); 
		 
		//retourne le nom générique des fichiers de sortie
			return new PortObject[] {URIUtil.uriToPort(sortie.toURI())};
	}
	
//	DataTableSpec sp;
	
//	 private DataTableSpec getSpec()
//	    {
//	       DataTableSpecCreator creator = new DataTableSpecCreator();
//	
//	       creator.addColumns(new DataColumnSpecCreator("URI", StringCellFactory.TYPE).createSpec());
//	      
//	        return sp; 
//	    }
	 
	 
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) {
		return new PortObjectSpec[] { inSpecs[0]};
	}
	
	
	

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
	 
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		// TODO Auto-generated method stub
		//LOGGER.warn("####LOG loadValidatedSettingsFrom : " + settings);
	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub
		
	}



	
}