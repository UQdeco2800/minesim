package minesim;


import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;



public class SettingsHandler {

	private static HashMap<String, String> loadedSettings = new HashMap<>();
	private static Logger logger = Logger.getLogger(SettingsHandler.class);
	
	/**
	 * Saves the settings to a text file. Overrides what is currently in the txt file
	 * @param settings an array list of strings of the settings current states.
	 */
	public static void saveSettings(List<String> settings, String path) throws IOException{
		File file = new File(path);
		
		if(!file.exists()){
			file.createNewFile();
		}	
		
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		fileWriter.write("");
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		for(int i = 0; i < settings.size(); i++ ){
			bufferedWriter.append(settings.get(i));
			if(settings.size()-1!= i){
				bufferedWriter.newLine();
			}
		}
		bufferedWriter.close();
	 }

	/**
	 * Save settings to default location
	 * @param settings the settings to save
	 * @throws IOException
	 */
	public static void saveSettings(List<String> settings) throws IOException {
		saveSettings(settings, "../minesim/settings.txt");
	}

	/**
	 * Reads the settings.txt file and returns the settings states as a HashMap
	 * @return Returns a HashMap of the games settings
	 */
	public static HashMap<String, String> loadSettings(String location) {
		HashMap<String, String> result;
		try {
			result = parseFile(location);
		} catch (Exception e) {
			try {
				result = parseFile("../minesim/safe_defaults.txt");
			} catch (Exception ex) {
				logger.error("Could not load fail safe defaults!", ex);
				return new HashMap<>();
			}
		}
		return result;
	}

	/**
	 * Load settings from default location
	 * @return settings as hashmap
	 */
	public static HashMap<String, String> loadSettings() {
		return loadSettings("../minesim/settings.txt");
	}

	/**
	 * Parses a file and returns the settings in a hashmap
	 * @param filename - the file to process
	 * @return a hashmap of property/value pairs
	 */
	private static HashMap<String, String> parseFile(String filename) throws Exception {
		HashMap<String, String> settings = new HashMap<>();
		try {
			Scanner scanner = new Scanner(new FileReader(filename));
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				try {
					settings.put(line.split(" = ")[0], line.split(" = ")[1]);
				} catch (Exception e) {
					throw new Exception(); //bad format
				}
			}
			scanner.close();
		} catch (IOException e) {
			//don't care
		}
		loadedSettings = settings;
		return settings;
	}

	/**
	 * Get loaded settings
	 * @return loaded settings
	 */
	public static HashMap<String, String> getLoadedSettings() {
		return loadedSettings;
	}
}
