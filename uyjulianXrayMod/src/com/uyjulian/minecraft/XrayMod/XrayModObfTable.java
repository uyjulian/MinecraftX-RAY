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

import com.mumfrey.liteloader.core.runtime.Obf;

public class XrayModObfTable extends Obf {
	//TODO: Minecraft 1.8
	public static final XrayModObfTable Block = new XrayModObfTable("net/minecraft/block/Block", "atr");
	public static final XrayModObfTable Block_shouldSideBeRendered = new XrayModObfTable("func_176225_a", "a", "shouldSideBeRendered");
	public static final XrayModObfTable BlockModelRenderer = new XrayModObfTable("net/minecraft/client/renderer/BlockModelRenderer", "cln");
	public static final XrayModObfTable BlockModelRenderer_renderModel = new XrayModObfTable("func_178267_a", "a", "renderModel");
	
	public static final XrayModObfTable IBlockAccess = new XrayModObfTable("net/minecraft/world/IBlockAccess", "ard");
	public static final XrayModObfTable IBakedModel = new XrayModObfTable("net/minecraft/client/resources/model/IBakedModel", "cxe");
	public static final XrayModObfTable IBlockState = new XrayModObfTable("net/minecraft/block/state/IBlockState", "bec");
	public static final XrayModObfTable BlockPos = new XrayModObfTable("net/minecraft/util/BlockPos", "dt");
	public static final XrayModObfTable WorldRenderer = new XrayModObfTable("net/minecraft/client/renderer/WorldRenderer", "civ");
	public static final XrayModObfTable EnumFacing = new XrayModObfTable("net/minecraft/util/EnumFacing", "ej");
	
	protected XrayModObfTable(String seargeName, String obfName) {
		super(seargeName.replaceAll("/", "."), obfName.replaceAll("/", "."));
	}
	
	protected XrayModObfTable(String seargeName, String obfName, String mcpName) {
		super(seargeName.replaceAll("/", "."), obfName.replaceAll("/", "."), mcpName.replaceAll("/", "."));
	}

}
