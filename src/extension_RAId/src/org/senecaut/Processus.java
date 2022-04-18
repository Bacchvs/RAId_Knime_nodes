package org.senecaut;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;

/**utilitaire de lancement de processus 
 * permettant de lancer RAId et de lire le retour
 * @author Arnaud
 *
 */
public class Processus implements Runnable{

	final ExecutionContext exec;
	NodeLogger LOGGER;
	String[] command;
	
	
	Processus(NodeLogger LOGGER, final ExecutionContext exec, String[] command ) throws Exception{
		
		this.LOGGER = LOGGER;
		this.command = command;
		this.exec = exec;
		
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


///////////// faire de commande une String[], c'est accépté par le builder qui prends un (String...)
	@Override
	public void run() {
		LOGGER.warn("Lancement de la commande : " + toString());
		ProcessBuilder pb = new ProcessBuilder(
				Options_Panel.concat(
						new String[] {"wsl.exe", "./RAId"}, 
						command
						)
				);

		
		Process process;
		try {
			process = pb.start();
		
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	 
			String line;
			while ((line = input.readLine()) != null) {
				LOGGER.warn(line);
				if (line.endsWith("Finished making output file header.")) {
					LOGGER.warn("##PROCESS ENDING##");
				} 
				else if (line.contains("Percent jobs completed")) { // The output contains the % complete
				
					int percentDone = Integer.parseInt(line.substring(line.lastIndexOf("=") + 1, line.length() - 1));

					exec.setProgress(percentDone/100.0, "Job Running...");
					if (percentDone == 95) {
						LOGGER.warn("##PROCESS FINISHING##");
					}
				}
			}
			LOGGER.warn("RETOUR DE PROCESSUS : " + process.exitValue());
			process.waitFor();
			
			
			LOGGER.warn("!!!!!!!!!!!!!!!!!!!! POST RUNNING CONCULSION !!!!!!!!!!!!!!!!!!");
			BufferedReader err = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String li;
			while ((li = err.readLine()) != null) {
				LOGGER.warn(li);
			}
			
			
		}catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Erreur du processus -> " + e.getMessage());				
		}
		 
		
	}	
	
	
	public String toString() {
		String ret = "wsl.exe ./RAId";
		
		for (String s : command) {
			ret += " " + s;
		}
		
		return ret;
	}

}
