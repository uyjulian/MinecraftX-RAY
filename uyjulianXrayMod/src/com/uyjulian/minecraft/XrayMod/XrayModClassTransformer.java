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

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import com.mumfrey.liteloader.transformers.Callback;
import com.mumfrey.liteloader.transformers.Callback.CallbackType;
import com.mumfrey.liteloader.transformers.CallbackInjectionTransformer;

import static org.objectweb.asm.Opcodes.*;
import net.minecraft.launchwrapper.IClassTransformer;

public class XrayModClassTransformer extends CallbackInjectionTransformer {
	
	private static final Callback methodCallback = new Callback(CallbackType.RETURN, "shouldSideBeRendered", "com.uyjulian.minecraft.XrayMod.UyjuliansXrayModMain");
	
	//TODO: Minecraft 1.7.10
	//TODO: I should make a script that you feed joined srg and then you get this :-)
	//TODO: Ask Mumfrey to allow you to for example: use Block in callback instead of BlockSlab BlockLiquid etc
	private static final String blockClass = "net.minecraft.block.Block";
	private static final String blockClassObf = "aji";
	private static final String blockSlabClass = "net.minecraft.block.BlockSlab";
	private static final String blockSlabClassObf = "alj";
	private static final String blockLiquidClass = "net.minecraft.block.BlockLiquid";
	private static final String blockLiquidClassObf = "alw";
	private static final String blockPaneClass = "net.minecraft.block.BlockPane";
	private static final String blockPaneClassObf = "aoa";
	private static final String blockWallClass = "net.minecraft.block.BlockWall";
	private static final String blockWallClassObf = "aoi";
	private static final String blockSnowClass = "net.minecraft.block.BlockSnow";
	private static final String blockSnowClassObf = "ann";
	private static final String iBlockAccessClass = "net/minecraft/world/IBlockAccess"; //you must use / here!
	private static final String iBlockAccessClassObf = "ahl";
	private static final String sideRenderMethod = "shouldSideBeRendered";
	private static final String sideRenderMethodSrg = "func_149646_a";
	private static final String sideRenderMethodObf = "a";
	private static final String sideRenderMethodSignature = "(L" + iBlockAccessClass + ";IIII)Z";
	private static final String sideRenderMethodSignatureObf = "(L" + iBlockAccessClassObf + ";IIII)Z";
	
	private void addCallbacksForSideRenderMethod(String currentBlockClass, String currentBlockClassObf) {
		this.addCallback(currentBlockClass,    sideRenderMethod,    sideRenderMethodSignature,    methodCallback);
		this.addCallback(currentBlockClass,    sideRenderMethodSrg, sideRenderMethodSignature,    methodCallback);
		this.addCallback(currentBlockClassObf, sideRenderMethodObf, sideRenderMethodSignatureObf, methodCallback);
	}
	
	@Override
	protected void addCallbacks() {
		addCallbacksForSideRenderMethod(blockClass,       blockClassObf);
		addCallbacksForSideRenderMethod(blockSlabClass,   blockSlabClassObf);
		addCallbacksForSideRenderMethod(blockLiquidClass, blockLiquidClassObf);
		addCallbacksForSideRenderMethod(blockPaneClass,   blockPaneClassObf);
		addCallbacksForSideRenderMethod(blockWallClass,   blockWallClassObf);
		addCallbacksForSideRenderMethod(blockSnowClass,   blockSnowClassObf);
	}

}
