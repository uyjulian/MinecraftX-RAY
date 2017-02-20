package com.uyjulian.minecraft.XrayMod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public class XrayModConfiguration {
	public Properties properties;
	public static XrayModConfiguration instance;
	public String dataPath;
	public String filePath;
	public static final String blockListIdentify = "XRBL";
	public static void init(String mcDataPath) {
		instance = new XrayModConfiguration();
		instance.properties = new Properties();
		instance.dataPath = mcDataPath;
		instance.filePath = instance.dataPath + File.separator + "config" + File.separator + "UyjuliansXrayMod.cfg";
		instance.loadProperties();
	}
	
	public static String getProperty(String key) {
		return instance.properties.getProperty(key);
	}
	
	public static void setProperty(String key, String value) {
		instance.properties.setProperty(key, value);
		instance.saveProperties();
	}
	
	public static String[] getBlockList(String blockListName) {
		String currentProperty = getProperty(blockListIdentify + blockListName);
		if (!(currentProperty == null)) {
			return currentProperty.split("[\\r\\n]+");
		}
		return null;
	}
	
	public static void setBlockList(String blockListName, String[] blockListToSave) {
		StringBuilder currentStringBuilder = new StringBuilder();
		int blockListLength = blockListToSave.length;
		for (int i = 0; i < blockListLength; ++i) {
			currentStringBuilder.append(blockListToSave[i] + "\r\n");
		}
		setProperty(blockListIdentify + blockListName, currentStringBuilder.toString());
	}
	
	public boolean loadProperties() {
		UyjuliansXrayModMain.printLineInLog("Attempting to read properties file...");
		boolean err = false;
		try {
			File blockListFile = new File(filePath);
			if (blockListFile.exists()) {
				UyjuliansXrayModMain.printLineInLog("Reading config file...");
				BufferedReader currentBufferedReader = new BufferedReader(new FileReader(blockListFile));
				instance.properties.load(currentBufferedReader);
				currentBufferedReader.close();
				UyjuliansXrayModMain.printLineInLog("Config file loaded!");
			}
			else {
				UyjuliansXrayModMain.printLineInLog("The config file doesn't exist!");
			}
		}
		catch (Exception currentException) {
			UyjuliansXrayModMain.printLineInLog("There was an error loading the config file.");
			currentException.printStackTrace();
			err = true;
		}
		return err;
	}
	
	public boolean saveProperties() {
		UyjuliansXrayModMain.printLineInLog("Attempting to write properties file...");
		boolean err = false;
		try {
			File configFolder = new File(instance.dataPath, "config");
			boolean canMakeConfigFolder = configFolder.mkdir() || configFolder.exists();
			if (canMakeConfigFolder) {
				File configFile = new File(filePath);
				BufferedWriter currentBufferedWriter = new BufferedWriter(new FileWriter(configFile));
				instance.properties.store(currentBufferedWriter, "Uyjulian's X-ray Mod Config");
				currentBufferedWriter.close();
			}
			else {
				UyjuliansXrayModMain.printLineInLog("Could not create config folder, check the permissions for the Minecraft data folder!");
			}
			UyjuliansXrayModMain.printLineInLog("Config file saved!");
		}
		catch (Exception currentException) {
			UyjuliansXrayModMain.printLineInLog("There was an error saving the config file.");
			currentException.printStackTrace();
		}
		return err;
	}
	
	
	

}
