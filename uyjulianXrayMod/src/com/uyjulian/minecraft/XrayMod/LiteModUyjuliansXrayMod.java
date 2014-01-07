package com.uyjulian.minecraft.XrayMod;

import java.io.File;

import net.minecraft.client.Minecraft;

import com.mumfrey.liteloader.InitCompleteListener;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.core.LiteLoader;

public class LiteModUyjuliansXrayMod implements LiteMod, InitCompleteListener {
	
	public UyjuliansXrayModMain modInstance;

	@Override
	public String getName() {
		return "!X-RAY";
	}

	@Override
	public String getVersion() {
		return "1.6.4";
	}

	@Override
	public void init(File configPath) {
		modInstance = UyjuliansXrayModMain.getModInstance();
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {
	}

	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
		modInstance.onTick(inGame);
	}

	@Override
	public void onInitCompleted(Minecraft minecraft, LiteLoader loader) {
	}

}
