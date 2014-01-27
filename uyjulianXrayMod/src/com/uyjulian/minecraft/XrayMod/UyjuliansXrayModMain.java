package com.uyjulian.minecraft.XrayMod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.mumfrey.liteloader.util.ModUtilities;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.IBlockAccess;

/*
 * Feel free to mess around with the source.
 */

public class UyjuliansXrayModMain {
	public static UyjuliansXrayModMain currentModInstance;
	public Minecraft minecraftInstance;
	public static int[] blockList = new int[] {1, 2, 3, 4, 7, 12, 13, 24, 87, 8, 9, 78, 79, 80};
	public static List<KeyBinding> keyBinds = new ArrayList<KeyBinding>();
	public static String currentBlocklistName = "DefaultBlockList";
	public static boolean toggleXRay = false;
	public static boolean toggleCaveFinder = false;
	
	public UyjuliansXrayModMain() {
		if (currentModInstance == null) {
			currentModInstance = this;
			minecraftInstance = Minecraft.getMinecraft();
			loadBlockList(currentBlocklistName);
			// Keybinding setup
			this.keyBinds.add(new KeyBinding("[Uyjulian's X-ray Mod] Toggle X-ray",Keyboard.KEY_X, null));
			this.keyBinds.add(new KeyBinding("[Uyjulian's X-ray Mod] Toggle Cave Finder",Keyboard.KEY_V, null));
			for (KeyBinding currentKey : this.keyBinds) {
				if (currentKey != null) {
					ModUtilities.registerKey(currentKey);
				}
			}
		}
		else {
			printLineInLog("X-ray mod already inited... Looks like there might be a glitch in my mod... This shouldn't happen!");
		}
	}
	
	public static UyjuliansXrayModMain getModInstance() {
		if (currentModInstance == null) {
			return new UyjuliansXrayModMain();
		}
		else {
			return currentModInstance;
		}
	}
	
	// Tick stuff
	
	public void onTick(boolean inGame) {
		if ((minecraftInstance.inGameHasFocus) && (inGame)) {
			if (this.keyBinds.get(0).isPressed()) {
				if (!Keyboard.isKeyDown(29) && !Keyboard.isKeyDown(157)) {
					UyjuliansXrayModMain.printLineInLog("Toggling X-ray...");
					toggleXRay = !toggleXRay;
					toggleCaveFinder = false;
					// Now refresh the world...
					minecraftInstance.renderGlobal.loadRenderers();
				}
				else {
					UyjuliansXrayModMain.printLineInLog("Displaying menu...");
					// Display GUI...
					minecraftInstance.displayGuiScreen(new XrayModMainGui(null, minecraftInstance.gameSettings));
				}
			}
			if (this.keyBinds.get(1).isPressed()) {
				UyjuliansXrayModMain.printLineInLog("Toggling Cave Finder...");
				toggleCaveFinder = !toggleCaveFinder;
				toggleXRay = false;
				// Now refresh the world...
				minecraftInstance.renderGlobal.loadRenderers();
			}
		}
	}
	
	// I/O stuff
	
	public void loadBlockList(String blockListName) {
		UyjuliansXrayModMain.printLineInLog("Please wait, loading Block List name: " + blockListName);
		try {
			currentBlocklistName = blockListName;
			int[] blockListBuffer = new int[1024];
			File blockListFile = new File(minecraftInstance.mcDataDir.getPath() + File.separator + "XRayProfiles", blockListName + ".XRayProfile");
			if (blockListFile.exists()) {
				UyjuliansXrayModMain.printLineInLog("The block list exists! Loading block list...");
				BufferedReader currentBufferedReader = new BufferedReader(new FileReader(blockListFile));
				String currentLine;
				int i;
				for (i = 0; (currentLine = currentBufferedReader.readLine()) != null; ++i) {
					blockListBuffer[i] = Integer.parseInt(currentLine);
				}
				currentBufferedReader.close();
				blockList = new int[i];
				System.arraycopy(blockListBuffer,0,blockList,0,i);
				UyjuliansXrayModMain.printLineInLog("Read complete!");
			}
			else {
				UyjuliansXrayModMain.printLineInLog("Oh, the block list doesn't exist... Oh well!");
			}
		}
		catch (Exception currentException) {
			UyjuliansXrayModMain.printLineInLog("Oops, looks like there was an error reading the block list! Printing stack trace now...");
			currentException.printStackTrace();
		}
	}
	
	public void saveBlockList(String blockListName) {
		UyjuliansXrayModMain.printLineInLog("Please wait, saving block list name: " + blockListName);
		try {
			currentBlocklistName = blockListName;
			File blockListFolder = new File(minecraftInstance.mcDataDir, "XRayProfiles");
			boolean canMakeBlockListFolder = blockListFolder.mkdir();
			File blockListFile = new File(blockListFolder, blockListName + ".XRayProfile");
			BufferedWriter currentBufferedWriter = new BufferedWriter(new FileWriter(blockListFile));
			int[] blockListBuffer = blockList;
			int blockListLength = blockList.length;
			for (int i = 0; i < blockListLength; ++i) {
				currentBufferedWriter.write(blockListBuffer[i] + "\r\n");
			}
			currentBufferedWriter.close();
			UyjuliansXrayModMain.printLineInLog("Write complete!");
		}
		catch (Exception currentException) {
			UyjuliansXrayModMain.printLineInLog("Oops, looks like there was an error writing the block list! Printing stack trace now...");
			currentException.printStackTrace();
		}
	}
	
	// Make things bright stuff
	
	public void disableBrightLight() {
		//minecraftInstance.thePlayer.removePotionEffect( 16 );
		//minecraftInstance.gameSettings.gammaSetting = 0.2F;
	}
	
	public void enableBrightLight() {
		//minecraftInstance.thePlayer.addPotionEffect( new PotionEffect( 16, 99999999, 255, true ) );
		//minecraftInstance.gameSettings.gammaSetting = 782;
	}
	
	// Check then render blocks stuff
	
	/*
	 * NOTE: Boolean (the object) throws an exception on null.
	 * 
	 * Confused on what this means?
	 * b means the side is not going to be rendered.
	 * a means the side is going to be rendered.
	 * c means the side will be processed by normal means.
	 */
	public static boolean shouldSideBeRendered(boolean returnValue, int ref, Block currentBlock, IBlockAccess par1BlockAccess, int par2, int par3, int par4, int par5) {
		if (toggleXRay || toggleCaveFinder) {
			int blockID = Block.getIdFromBlock(currentBlock);
			int[] blockListBuffer = blockList;
			int blockListLength = blockList.length;
			int i, currentID;
			for (i = 0; i < blockListLength; ++i) {
				currentID = blockListBuffer[i];
				if (currentID == blockID) {
					if (toggleCaveFinder) {
						if (currentID != 1) {
							return false;
						}
					}
					else {
						return false;
					}
				}
			}
			if (!toggleCaveFinder) {
				if (blockListLength != 0) {
					return true;
				}
			}
		}
		return returnValue;
	}
	
	// Misc stuff
	
	public static void printLineInLog(String lineToPrint) {
		System.out.println("[Uyjulian's X-ray Mod] " + lineToPrint);
	}
}
