package com.uyjulian.minecraft.XrayMod;

import com.mumfrey.liteloader.core.runtime.Obf;

public class XrayModObfTable extends Obf {
	//TODO: Minecraft 1.7.10
	public static XrayModObfTable[] Blocks = new XrayModObfTable[] {
		new XrayModObfTable("net.minecraft.block.Block", "aji"),
		new XrayModObfTable("net.minecraft.block.BlockSlab", "alj"),
		new XrayModObfTable("net.minecraft.block.BlockLiquid", "alw"),
		new XrayModObfTable("net.minecraft.block.BlockPane", "aoa"),
		new XrayModObfTable("net.minecraft.block.BlockWall", "aoi"),
		new XrayModObfTable("net.minecraft.block.BlockSnow", "ann"),
	};
	public static XrayModObfTable IBlockAccess = new XrayModObfTable("net.minecraft.world.IBlockAccess", "ahl");
	public static XrayModObfTable shouldSideBeRendered = new XrayModObfTable("func_149646_a", "a", "shouldSideBeRendered");

	protected XrayModObfTable(String seargeName, String obfName) {
		super(seargeName, obfName);
	}
	
	protected XrayModObfTable(String seargeName, String obfName, String mcpName) {
		super(seargeName, obfName, mcpName);
	}

}
