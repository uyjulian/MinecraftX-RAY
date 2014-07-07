package com.uyjulian.minecraft.XrayMod;

import com.mumfrey.liteloader.core.runtime.Obf;

public class XrayModObfTable extends Obf {
	public static XrayModObfTable Block = new XrayModObfTable("net.minecraft.block.Block", "aji");
	public static XrayModObfTable shouldSideBeRendered = new XrayModObfTable("func_149646_a", "a", "shouldSideBeRendered");

	protected XrayModObfTable(String seargeName, String obfName) {super(seargeName, obfName);}
	
	protected XrayModObfTable(String seargeName, String obfName, String mcpName) {super(seargeName, obfName, mcpName);}

}
