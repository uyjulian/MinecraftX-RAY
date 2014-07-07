/* Copyright (c) 2014, Julian Uy
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

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

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;


public class UyjuliansXrayModMain {
	public static UyjuliansXrayModMain currentModInstance;
	public Minecraft minecraftInstance;
	public static String[] blockList = new String[] {"minecraft:stone", "minecraft:grass", "minecraft:dirt", "minecraft:cobblestone", "minecraft:bedrock", "minecraft:sand", "minecraft:gravel", "minecraft:sandstone", "minecraft:netherrack", "minecraft:flowing_water", "minecraft:water", "minecraft:snow_layer", "minecraft:ice", "minecraft:snow"};
	public static List<KeyBinding> keyBinds = new ArrayList<KeyBinding>();
	public static String currentBlocklistName = "DefaultBlockList";
	public static boolean toggleXRay = false;
	public static boolean toggleCaveFinder = false;
	private Boolean FirstTick = false;
	
	@SuppressWarnings("deprecation")
	public UyjuliansXrayModMain() {
		if (currentModInstance == null) {
			currentModInstance = this;
			minecraftInstance = Minecraft.getMinecraft();
			loadBlockList(currentBlocklistName);
			// Keybinding setup
			this.keyBinds.add(new KeyBinding("Toggle X-ray",Keyboard.KEY_X, "Uyjulian's X-ray Mod"));
			this.keyBinds.add(new KeyBinding("Toggle Cave Finder",Keyboard.KEY_V, "Uyjulian's X-ray Mod"));
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
	
	private void startUpdateChecker() {
		new Thread(new XrayModUpdateChecker()).start();
	}
	
	// Tick stuff
	
	public void onTick(boolean inGame) {
		if ((minecraftInstance.inGameHasFocus) && (inGame)) {
			if (FirstTick == false) {
				FirstTick = true;
				startUpdateChecker();
			}
			if (this.keyBinds.get(0).isPressed()) {
				if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && !Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
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
			String[] blockListBuffer = new String[4096];
			File blockListFile = new File(minecraftInstance.mcDataDir.getPath() + File.separator + "XRayProfiles", blockListName + ".XRayProfileNew");
			if (blockListFile.exists()) {
				UyjuliansXrayModMain.printLineInLog("The block list exists! Loading block list...");
				BufferedReader currentBufferedReader = new BufferedReader(new FileReader(blockListFile));
				String currentLine;
				int i;
				for (i = 0; (currentLine = currentBufferedReader.readLine()) != null; ++i) {
					blockListBuffer[i] = currentLine;
				}
				currentBufferedReader.close();
				blockList = new String[i];
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
			File blockListFile = new File(blockListFolder, blockListName + ".XRayProfileNew");
			BufferedWriter currentBufferedWriter = new BufferedWriter(new FileWriter(blockListFile));
			String[] blockListBuffer = blockList;
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
	 * NOTE: Boolean (the object) throws an exception on null. Don't use that!
	 * 
	 * Confused on what this means?
	 * b means the side is not going to be rendered. (aka false)
	 * a means the side is going to be rendered. (aka true)
	 * c means the side will be processed by normal means.
	 * 
	 * Still confused? Too bad.
	 */
	public static char shouldSideBeRendered(Block currentBlock) {
		if (toggleXRay || toggleCaveFinder) {
			String blockID = Block.blockRegistry.getNameForObject(currentBlock);
			String[] blockListBuffer = blockList;
			int blockListLength = blockListBuffer.length;
			int i; 
			String currentID;
			for (i = 0; i < blockListLength; ++i) {
				currentID = blockListBuffer[i];
				if (currentID.equals(blockID)) { //You must use .equals(), not ==, that screwed me over e_e
					if (toggleCaveFinder) {
						if (!(blockID.equals("minecraft:stone"))) { //Only display stone in cave finder mode
							return 'b';
						}
					}
					else {
						return 'b';
					}
				}
			}
			if (!toggleCaveFinder) {
				if (blockListLength != 0) { //Nothing in the list, young lads.
					return 'a'; //Display if detected
				}
			}
		}
		return 'c';
	}
	
	// Misc stuff
	
	public static void printLineInLog(String lineToPrint) {
		System.out.println("[Uyjulian's X-ray Mod] " + lineToPrint);
	}
	public static void putLineInChat(String lineToPrint) {
		getModInstance().minecraftInstance.thePlayer.addChatMessage(new ChatComponentText("§l§o§6[Uyjulian's X-ray Mod]§r " + lineToPrint));
	}
}
