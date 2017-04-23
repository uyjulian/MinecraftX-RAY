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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.BitSet;
import java.util.List;

@Mixin(BlockModelRenderer.class)
public abstract class MixinBlockModelRenderer {

    @Shadow
    private void renderQuadsFlat(IBlockAccess blockAccessIn, IBlockState stateIn, BlockPos posIn, int brightnessIn, boolean ownBrightness, VertexBuffer buffer, List<BakedQuad> list, BitSet bitSet) {}

    @Inject(method="renderModel", at=@At("HEAD"), cancellable=true)
    private void onRenderModel(IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, VertexBuffer buffer, boolean checkSides, CallbackInfoReturnable<Boolean> ci) {
        if (UyjuliansXrayModMain.xrayEnabled()) {
            if (!UyjuliansXrayModMain.checkBlockList(stateIn.getBlock())) {
                try {
                    long rand = MathHelper.getPositionRandom(posIn);
                    boolean flag = false;
                    BitSet bitset = new BitSet(3);

                    for (EnumFacing enumfacing : EnumFacing.values()) {
                        List<BakedQuad> list = modelIn.getQuads(stateIn, enumfacing, rand);

                        if (!list.isEmpty() && (!checkSides || UyjuliansXrayModMain.checkBlockList(worldIn.getBlockState(posIn.offset(enumfacing)).getBlock()))) {
                            int i = 15 << 20 | 15 << 4;
                            this.renderQuadsFlat(worldIn, stateIn, posIn, i, false, buffer, list, bitset);
                            flag = true;
                        }
                    }

                    List<BakedQuad> list1 = modelIn.getQuads(stateIn, null, rand);

                    if (!list1.isEmpty()) {
                        this.renderQuadsFlat(worldIn, stateIn, posIn, -1, true, buffer, list1, bitset);
                        flag = true;
                    }

                    ci.setReturnValue(flag);
                } catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block model while using uyjulian's X-ray mod");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
                    CrashReportCategory.addBlockInfo(crashreportcategory, posIn, stateIn);
                    throw new ReportedException(crashreport);
                }
            }
            else {
                ci.setReturnValue(false);
            }
        }
    }
}
