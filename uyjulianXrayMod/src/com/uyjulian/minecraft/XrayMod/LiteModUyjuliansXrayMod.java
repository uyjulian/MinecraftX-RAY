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

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.Block;

import com.mumfrey.liteloader.InitCompleteListener;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.transformers.event.ReturnEventInfo;

public class LiteModUyjuliansXrayMod implements LiteMod, InitCompleteListener {
	
	public UyjuliansXrayModMain modInstance;
	
	@Override
	public String getName() {
		return "Uyjulian's X-ray Mod";
	}

	@Override
	public String getVersion() {
		return UyjuliansXrayModMain.currentVersion;
	}

	@Override
	public void init(File configPath) {
		modInstance = UyjuliansXrayModMain.getModInstance();
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
		modInstance.onTick(inGame);
	}

	@Override
	public void onInitCompleted(Minecraft minecraft, LiteLoader loader) {}
	
	private static boolean renderBlockProcessingIsActive = false;
	public static void renderBlockProcessing(ReturnEventInfo<RenderBlocks, Boolean> e, Block arg1, int arg2, int arg3, int arg4) {
		if (renderBlockProcessingIsActive) return; // To avoid infinite loops
		renderBlockProcessingIsActive = true;
		char currentBoolean = UyjuliansXrayModMain.blockIsInBlockList(arg1);

		if ((currentBoolean == 'b')) {
			try {
				e.setReturnValue(false); // Act like normal behavior...
			} 
			finally {}
		}
		else if (currentBoolean == 'a') {
			e.getSource().renderBlockAllFaces(arg1, arg2, arg3, arg4);
			try {
				e.setReturnValue(true); // Act like normal behavior...
			} 
			finally {}
		}
		renderBlockProcessingIsActive = false;
	}


}
