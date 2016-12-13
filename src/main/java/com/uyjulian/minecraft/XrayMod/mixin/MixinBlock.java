package com.uyjulian.minecraft.XrayMod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.uyjulian.minecraft.XrayMod.UyjuliansXrayModMain;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

@Mixin(Block.class)
public abstract class MixinBlock {
	@Inject(method="shouldSideBeRendered", at=@At("HEAD"), cancellable=true)
	private void onShouldSideBeRendered(IBlockAccess arg1, BlockPos arg2, EnumFacing arg3, CallbackInfoReturnable<Boolean> ci) {
		char currentBoolean = UyjuliansXrayModMain.blockIsInBlockList((Block)(Object)this, arg1, arg2, arg3);
		if (currentBoolean != 'c') {
			try {
				ci.setReturnValue(currentBoolean == 'a');
			} 
			catch(Exception ex) {
				
			}
		}
	}
}
