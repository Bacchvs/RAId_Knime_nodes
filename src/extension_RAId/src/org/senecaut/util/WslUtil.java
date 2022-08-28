package org.senecaut.util;
import java.io.*;
import java.lang.*;

public class WSLUtil{
    //Traduis un chemin windows en chemin wsl (exemple : D:\Users\Arnaud -> /mnt/d/Users/Arnaud) - Arnaud Sénécaut
     // les nouvelles versions de WSL ont permit l'ajout d'une commande réalisant cette tache
		private static String toWslPath (String path){
			System.out.print("#### utilisation de toWslPath : " + path + "  --->  ");

			ProcessBuilder processBuilder = new ProcessBuilder();


			processBuilder.command("bash","-c", "wslpath "+path);
			
			String wslPath = "";

			try {

				Process process = processBuilder.start();

				BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
					
					wslPath = reader.readLine();

					System.out.println(wslPath);
				}catch(Exception e){
					e.printStackTrace();
				}

            System.out.println(wslPath);
			return wslPath;
		}
}