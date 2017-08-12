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

package com.uyjulian.minecraft.XrayMod.mixin;

import com.uyjulian.minecraft.XrayMod.UyjuliansXrayModMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/block/state/BlockStateContainer$StateImplementation")
public abstract class MixinStateImplementation {
    @Shadow
    public Block getBlock() { return null; }

    @Inject(method="getAmbientOcclusionLightValue()F", at=@At("HEAD"), cancellable=true)
    private void onGetAmbientOcclusionLightValue(CallbackInfoReturnable<Float> ci) {
        if (UyjuliansXrayModMain.enabled()) {
            ci.setReturnValue(1.0f);
        }
    }

    @Inject(method="getPackedLightmapCoords(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)I", at=@At("HEAD"), cancellable=true)
    private void onGetPackedLightmapCoords(IBlockAccess source, BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        if (UyjuliansXrayModMain.enabled()) {
            ci.setReturnValue(15 << 20 | 15 << 4);
        }
    }

    @Inject(method="getRenderType()Lnet/minecraft/util/EnumBlockRenderType;", at=@At("HEAD"), cancellable=true)
    private void onGetRenderType(CallbackInfoReturnable<EnumBlockRenderType> ci) {
        if (UyjuliansXrayModMain.enabled() && UyjuliansXrayModMain.checkBlockList(this.getBlock())) {
            ci.setReturnValue(EnumBlockRenderType.INVISIBLE);
        }
    }

    @Inject(method="shouldSideBeRendered(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", at=@At("HEAD"), cancellable=true)
    private void onShouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing, CallbackInfoReturnable<Boolean> ci) {
        if (UyjuliansXrayModMain.xrayEnabled()) {
            ci.setReturnValue((!UyjuliansXrayModMain.checkBlockList(this.getBlock())) && UyjuliansXrayModMain.checkBlockList(blockAccess.getBlockState(pos.offset(facing)).getBlock()));
        }
        else if (UyjuliansXrayModMain.caveFinderEnabled()) {
            ci.setReturnValue((!UyjuliansXrayModMain.checkBlockList(this.getBlock())) && blockAccess.isAirBlock(pos.offset(facing)));
        }
        else if (UyjuliansXrayModMain.specialMode1Enabled()) {
            if (!UyjuliansXrayModMain.checkBlockList(this.getBlock())) {
                boolean shouldRender = true;
                for (EnumFacing facing1 : new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST}) {
                    if (blockAccess.isAirBlock(pos.offset(facing1))) {
                        shouldRender = true;
                    }
                    else {
                        shouldRender = false;
                        break;
                    }
                }
                ci.setReturnValue(shouldRender);
            } else {
                ci.setReturnValue(false);
            }
        }
    }
}
