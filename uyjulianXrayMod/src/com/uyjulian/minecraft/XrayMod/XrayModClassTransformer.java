package com.uyjulian.minecraft.XrayMod;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import com.mumfrey.liteloader.transformers.Callback;
import com.mumfrey.liteloader.transformers.Callback.CallbackType;
import com.mumfrey.liteloader.transformers.CallbackInjectionTransformer;

import static org.objectweb.asm.Opcodes.*;
import net.minecraft.launchwrapper.IClassTransformer;

public class XrayModClassTransformer extends CallbackInjectionTransformer {

	@Override
	protected void addCallbacks() {
		this.addCallback("net.minecraft.block.Block", "shouldSideBeRendered", "(Lnet/minecraft/world/IBlockAccess;IIII)Z", new Callback(CallbackType.RETURN, "shouldSideBeRendered", "com.uyjulian.minecraft.XrayMod.UyjuliansXrayModMain"));
		this.addCallback("net.minecraft.block.Block", "func_149646_a", "(Lnet/minecraft/world/IBlockAccess;IIII)Z", new Callback(CallbackType.RETURN, "shouldSideBeRendered", "com.uyjulian.minecraft.XrayMod.UyjuliansXrayModMain"));
		this.addCallback("ahu", "a", "(Lafx;IIII)Z", new Callback(CallbackType.RETURN, "shouldSideBeRendered", "com.uyjulian.minecraft.XrayMod.UyjuliansXrayModMain"));
	}

}
