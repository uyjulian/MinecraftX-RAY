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

import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.util.ModUtilities;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;


public class UyjuliansXrayModMain {
	public static UyjuliansXrayModMain currentModInstance;
	public Minecraft minecraftInstance;
	public static String[] blockList = new String[] {"minecraft:stone", "minecraft:grass", "minecraft:dirt", "minecraft:cobblestone", "minecraft:bedrock", "minecraft:sand", "minecraft:gravel", "minecraft:sandstone", "minecraft:netherrack", "minecraft:flowing_water", "minecraft:water", "minecraft:snow_layer", "minecraft:ice", "minecraft:snow"};
	public static List<KeyBinding> keyBinds = new ArrayList<KeyBinding>();
	public static String currentBlocklistName = "DefaultBlockList";
	public static boolean toggleXRay = false;
	public static boolean toggleCaveFinder = false;
	public static boolean toggleSpecialMode1 = false;
	public static String currentVersion = "4";
	private Boolean FirstTick = false;
	public static boolean crashProtection = false;
	
	public UyjuliansXrayModMain() {
		if (currentModInstance == null) {
			currentModInstance = this;
			minecraftInstance = Minecraft.getMinecraft();
			XrayModConfiguration.init(minecraftInstance.mcDataDir.getPath());
			String crashprotprop = XrayModConfiguration.getProperty("throwagain");
			if (crashprotprop != null) {
				if (crashprotprop.equals("false")) {
					crashProtection = true;
				}
			}
			loadBlockList(currentBlocklistName);
			// Keybinding setup
			this.keyBinds.add(new KeyBinding("Toggle X-ray",Keyboard.KEY_X, "Uyjulian's X-ray Mod"));
			this.keyBinds.add(new KeyBinding("Toggle Cave Finder",Keyboard.KEY_V, "Uyjulian's X-ray Mod"));
			this.keyBinds.add(new KeyBinding("Toggle Special Mode 1",Keyboard.KEY_C, "Uyjulian's X-ray Mod"));
			for (KeyBinding currentKey : this.keyBinds) {
				if (currentKey != null) {
					LiteLoader.getInput().registerKeyBinding(currentKey);
				}
			}
		}
		else {
			printLineInLog("There seems to be something odd going on...");
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
	
	// Ticking/keys
	
	public void onTick(boolean inGame) {
		if ((minecraftInstance.inGameHasFocus) && (inGame)) {
			if (FirstTick == false) {
				FirstTick = true;
				boolean dontcheckupdate = false;
				String updatenotify = XrayModConfiguration.getProperty("updatenotify");
				if (updatenotify != null) {
					if (updatenotify.equals("false")) {
						dontcheckupdate = true;
					}
				}
				if (!dontcheckupdate) {
					startUpdateChecker();
				}
			}
			if (this.keyBinds.get(0).isPressed()) { //X-ray key
				if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && !Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
					UyjuliansXrayModMain.printLineInLog("Toggle X-ray");
					toggleXRay = !toggleXRay;
					toggleCaveFinder = false;
					// Now refresh the world...
					minecraftInstance.renderGlobal.loadRenderers();
				}
				else {
					UyjuliansXrayModMain.printLineInLog("Open menu");
					// Display GUI...
					minecraftInstance.displayGuiScreen(new XrayModMainGui(null, minecraftInstance.gameSettings));
				}
			}
			if (this.keyBinds.get(1).isPressed()) { //Cave finder key
				UyjuliansXrayModMain.printLineInLog("Toggle cave finder");
				toggleCaveFinder = !toggleCaveFinder;
				toggleXRay = false;
				// Now refresh the world...
				minecraftInstance.renderGlobal.loadRenderers();
			}
			if (this.keyBinds.get(2).isPressed()) { //Special mode key
				UyjuliansXrayModMain.printLineInLog("Toggle special mode");
				toggleSpecialMode1 = !toggleSpecialMode1;
				toggleXRay = false;
				// Now refresh the world...
				minecraftInstance.renderGlobal.loadRenderers();
			}
		}
	}
	
	// Input/Output code
	
	public void loadBlockList(String blockListName) {
		UyjuliansXrayModMain.printLineInLog("Loading Block List name: " + blockListName);
		String[] tempBlockList = XrayModConfiguration.getBlockList(blockListName);
		if (!(tempBlockList == null)) {
			blockList = tempBlockList; 
		}
		//Otherwise, just leave the existing block list contents
	}
	
	public void saveBlockList(String blockListName) {
		UyjuliansXrayModMain.printLineInLog("Saving block list name: " + blockListName);
		XrayModConfiguration.setBlockList(blockListName, blockList);
	}
	
	// Code for
	
	/*
	 * Confused on what this means?
	 * b means the side is not going to be rendered. (aka false)
	 * a means the side is going to be rendered. (aka true)
	 * c means the side will be processed by normal means.
	 * 
	 * Some really ugly hack just to use one byte. Wow.
	 */
	public static char blockIsInBlockList(Block currentBlock, IBlockAccess iba, BlockPos bps, EnumFacing ef) {
		if (toggleXRay || toggleCaveFinder || toggleSpecialMode1) {
			String blockID = Block.REGISTRY.getNameForObject(currentBlock).toString();
			String[] blockListBuffer = blockList;
			int blockListLength = blockListBuffer.length;
			int i; 
			String currentID;
			for (i = 0; i < blockListLength; ++i) {
				currentID = blockListBuffer[i];
				if (currentID.equals(blockID)) { 
					if (toggleCaveFinder) {	//Only display stone in cave finder mode, 
						if (!(blockID.equals("minecraft:stone"))) { 
							return 'b'; //Don't display this side
						}
					}
					else {
						return 'b';//Don't display this side
					}
				}
			}
			if (toggleSpecialMode1) {
				BlockPos north = bps.north();
				BlockPos east = bps.east();
				BlockPos south = bps.south();
				BlockPos west = bps.west();
				if (iba.isAirBlock(north) && iba.isAirBlock(east) && iba.isAirBlock(south) && iba.isAirBlock(west)) {
					return 'a';//Don't display this side
				}
				else {
					return 'b';
				}
			}
			if (!toggleCaveFinder) {
				if (blockListLength != 0) { //Nothing in the list, young lads.
					return 'a'; //Display if detected
				}
			}
		}
		return 'c'; //Normal behavior
	}
	
	// Toolbox
	
	public static void printLineInLog(String lineToPrint) {
		System.out.println("[" + "UjXr" + "] " + lineToPrint);
	}
	public static void putLineInChat(String lineToPrint) {
		getModInstance().minecraftInstance.thePlayer.addChatMessage(new TextComponentString("§l§o§6[" + "UjXr" + "]§r " + lineToPrint));
	}
}
