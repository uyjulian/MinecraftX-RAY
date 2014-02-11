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
	
	//TODO: Minecraft 1.7.2
	private static final String blockClass = "net.minecraft.block.Block";
	private static final String blockClassObf = "ahu";
	private static final String blockSlabClass = "net.minecraft.block.BlockSlab";
	private static final String blockSlabClassObf = "ajv";
	private static final String blockLiquidClass = "net.minecraft.block.BlockLiquid";
	private static final String blockLiquidClassObf = "aki";
	private static final String blockPaneClass = "net.minecraft.block.BlockPane";
	private static final String blockPaneClassObf = "amm";
	private static final String blockWallClass = "net.minecraft.block.BlockWall";
	private static final String blockWallClassObf = "amu";
	private static final String blockSnowClass = "net.minecraft.block.BlockSnow";
	private static final String blockSnowClassObf = "alz";
	private static final String sideRenderMethod = "shouldSideBeRendered";
	private static final String sideRenderMethodSrg = "func_149646_a";
	private static final String sideRenderMethodObf = "a";
	private static final String sideRenderMethodSignature = "(Lnet/minecraft/world/IBlockAccess;IIII)Z";
	private static final String sideRenderMethodSignatureObf = "(Lafx;IIII)Z";
	
	private void addCallbacksForSideRenderMethod(String currentBlockClass, String currentBlockClassObf) {
		this.addCallback(currentBlockClass, sideRenderMethod, sideRenderMethodSignature, methodCallback);
		this.addCallback(currentBlockClass, sideRenderMethodSrg, sideRenderMethodSignature, methodCallback);
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
