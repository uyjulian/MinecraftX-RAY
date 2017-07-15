/* Copyright (c) 2014-2017, Julian Uy
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

import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.util.log.LiteLoaderLogger;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UyjuliansXrayModMain {
	public static UyjuliansXrayModMain currentModInstance;
	public Minecraft minecraftInstance;
	public static List<String> blockList = Arrays.asList("minecraft:air", "minecraft:stone", "minecraft:grass", "minecraft:dirt", "minecraft:cobblestone", "minecraft:bedrock", "minecraft:sand", "minecraft:gravel", "minecraft:sandstone", "minecraft:netherrack", "minecraft:flowing_water", "minecraft:water", "minecraft:snow_layer", "minecraft:ice", "minecraft:snow");
	public static List<KeyBinding> keyBinds = new ArrayList<KeyBinding>();
	public static String currentBlocklistName = "DefaultBlockList";
	public static boolean toggleXRay = false;
	public static String currentVersion = "1.0.1";
	private Boolean FirstTick = false;

	public UyjuliansXrayModMain() {
		if (currentModInstance == null) {
			currentModInstance = this;
			minecraftInstance = Minecraft.getMinecraft();
			XrayModConfiguration.init(minecraftInstance.mcDataDir.getPath());

			loadBlockList(currentBlocklistName);
			// Keybinding setup
			UyjuliansXrayModMain.keyBinds.add(new KeyBinding("Toggle X-ray",Keyboard.KEY_X, "Uyjulian's X-ray Mod"));
			for (KeyBinding currentKey : UyjuliansXrayModMain.keyBinds) {
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
			if (!FirstTick) {
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
			if (UyjuliansXrayModMain.keyBinds.get(0).isPressed()) { //X-ray key
				if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && !Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
					UyjuliansXrayModMain.printLineInLog("Toggle X-ray");
					toggleXRay = !toggleXRay;
					// Now refresh the world...
					minecraftInstance.renderGlobal.loadRenderers();
				}
				else {
					UyjuliansXrayModMain.printLineInLog("Open menu");
					// Display GUI...
					minecraftInstance.displayGuiScreen(new XrayModMainGui(null, minecraftInstance.gameSettings));
				}
			}
		}
	}

	public static boolean xrayEnabled() {
		return toggleXRay;
	}
	
	// Input/Output code
	
	public void loadBlockList(String blockListName) {
		UyjuliansXrayModMain.printLineInLog("Loading Block List name: " + blockListName);
		String[] tempBlockList = XrayModConfiguration.getBlockList(blockListName);
		if (!(tempBlockList == null)) {
			blockList = Arrays.asList(tempBlockList);
		}
		//Otherwise, just leave the existing block list contents
	}
	
	public void saveBlockList(String blockListName) {
		UyjuliansXrayModMain.printLineInLog("Saving block list name: " + blockListName);
		XrayModConfiguration.setBlockList(blockListName, blockList.toArray(new String[0]));
	}

	public static boolean checkBlockList(Block blockIn) {
        return blockList.contains(Block.REGISTRY.getNameForObject(blockIn).toString());
    }

	// Toolbox
	
	public static void printLineInLog(String lineToPrint) {
		LiteLoaderLogger.debug("[UjXr] %s", lineToPrint);
	}

}
