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

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class XrayModMainGui extends GuiScreen {
	private GuiScreen parentScreen;
	private GuiTextField profileNameTextBox;

	public XrayModMainGui(GuiScreen parentScreen, GameSettings currentGameSettings) {
		this.parentScreen = parentScreen;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 50 + 12, I18n.format("gui.done", new Object[0])));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 75 + 12, "Select blocks"));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 100 + 12, "Switch to the profile in the textbox"));
		this.profileNameTextBox = new GuiTextField(this.fontRendererObj, this.width / 2 - 100, this.height / 4 + 125 + 12, 200, 20);
		this.profileNameTextBox.setMaxStringLength(32);
		this.profileNameTextBox.setFocused(true);
		this.profileNameTextBox.setText(UyjuliansXrayModMain.getModInstance().currentBlocklistName);
		((GuiButton)this.buttonList.get(2)).enabled = ((this.profileNameTextBox.getText().length() > 0));
	}
	
	@Override
	protected void actionPerformed(GuiButton currentButton) {
		if (currentButton.enabled) {
			if (currentButton.id == 0) {
				this.mc.displayGuiScreen(this.parentScreen);
			}
			if (currentButton.id == 1) {
				this.mc.displayGuiScreen(new XrayModChooserGui());
			}
			if (currentButton.id == 2) {
				UyjuliansXrayModMain.getModInstance().loadBlockList(this.profileNameTextBox.getText());
				this.mc.renderGlobal.loadRenderers();
				this.mc.displayGuiScreen(this.parentScreen);
			}
			
		}
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		this.profileNameTextBox.mouseClicked(par1, par2, par3);
		super.mouseClicked(par1, par2, par3);
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		this.profileNameTextBox.textboxKeyTyped(par1, par2);
		((GuiButton)this.buttonList.get(2)).enabled = ((this.profileNameTextBox.getText().length() > 0));
		super.keyTyped(par1, par2);
		
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		drawCenteredString(this.fontRendererObj, "X-Ray options", this.width / 2, this.height / 4 + 25 + 12, 16777215);
		this.profileNameTextBox.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

}
