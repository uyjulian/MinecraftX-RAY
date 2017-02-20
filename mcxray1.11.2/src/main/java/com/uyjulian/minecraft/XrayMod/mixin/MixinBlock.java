package com.uyjulian.minecraft.XrayMod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.uyjulian.minecraft.XrayMod.UyjuliansXrayModMain;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

@Mixin(Block.class)
public abstract class MixinBlock {
	@Inject(method="shouldSideBeRendered", at=@At("HEAD"), cancellable=true)
	private void onShouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> ci) {
		char currentBoolean = UyjuliansXrayModMain.blockIsInBlockList((Block)(Object)this, blockAccess, pos, side);
		if (currentBoolean != 'c') {
			try {
				ci.setReturnValue(currentBoolean == 'a');
			} 
			catch(Exception ex) {
				
			}
		}
	}
}
