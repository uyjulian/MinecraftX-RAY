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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.settings.GameSettings;

public class XrayModMainGui extends GuiScreen implements GuiYesNoCallback {
	private GuiScreen parentScreen;
	private GuiTextField profileNameTextBox;
	private GuiTextField configKeyTextBox;
	private GuiTextField configValueTextBox;
	private URI clickedURI;
	public int chooseScreen = 0;

	public XrayModMainGui(GuiScreen parentScreen, GameSettings currentGameSettings) {
		this.parentScreen = parentScreen;
	}
	
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		
		
		this.buttonList.clear();
		if (chooseScreen == 0) { //default
			this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 0 + 12,  "Select blocks to see in X-ray view"));
			this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 25 + 12, "Switch to the profile in the textbox"));
			this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 75 + 12, "Go to MCF topic for support/updates"));
			this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 100 + 12,"Change settings"));
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 125 + 12,"Return to the game"));
			this.profileNameTextBox = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, this.height / 4 + 50 + 12, 200, 20);
			this.profileNameTextBox.setMaxStringLength(32);
			this.profileNameTextBox.setFocused(true);
			UyjuliansXrayModMain.getModInstance();
			this.profileNameTextBox.setText(UyjuliansXrayModMain.currentBlocklistName);
			this.buttonList.get(2).enabled = ((this.profileNameTextBox.getText().length() > 0));
		}
		else if (chooseScreen == 1) { //option chooser
			this.configKeyTextBox = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, this.height / 4 + 00 + 12, 200, 20);
			this.configValueTextBox = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, this.height / 4 + 25 + 12, 200, 20);
			this.configKeyTextBox.setMaxStringLength(32);
			this.configValueTextBox.setMaxStringLength(32);
			this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 50 + 12, "Set option"));
		}
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
			if (currentButton.id == 3) {
				try {
					URI currentURI = new URI("http://bit.ly/x-ray-mod");
	                if (this.mc.gameSettings.chatLinksPrompt)
	                {
	                    this.clickedURI = currentURI;
	                    this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, "http://bit.ly/x-ray-mod", 0, false));
	                }
	                else
	                {
	                    this.OpenURI(currentURI);
	                }
				} 
				catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
			if (currentButton.id == 4) {
				XrayModMainGui newGuiObj = new XrayModMainGui(this, mc.gameSettings);
				newGuiObj.chooseScreen = 1;
				this.mc.displayGuiScreen(newGuiObj);
			}
			if (currentButton.id == 5) {
				if ((this.configKeyTextBox.getText().length() > 0) && (this.configValueTextBox.getText().length() > 0)) {
					XrayModConfiguration.setProperty(this.configKeyTextBox.getText(), this.configValueTextBox.getText());
				}
				this.mc.displayGuiScreen(this.parentScreen);
			}
			
		}
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		if (this.profileNameTextBox != null) {
			this.profileNameTextBox.mouseClicked(par1, par2, par3);
		}
		if (this.configKeyTextBox != null) {
			this.configKeyTextBox.mouseClicked(par1, par2, par3);
		}
		if (this.configValueTextBox != null) {
			this.configValueTextBox.mouseClicked(par1, par2, par3);
		}
		try {
			super.mouseClicked(par1, par2, par3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		if (this.profileNameTextBox != null) {
			this.profileNameTextBox.textboxKeyTyped(par1, par2);
			this.buttonList.get(2).enabled = ((this.profileNameTextBox.getText().length() > 0));
		}
		if (this.configKeyTextBox != null) {
			this.configKeyTextBox.textboxKeyTyped(par1, par2);
		}
		if (this.configValueTextBox != null) {
			this.configValueTextBox.textboxKeyTyped(par1, par2);
		}
		try {
			super.keyTyped(par1, par2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		drawCenteredString(this.fontRenderer, "X-Ray main menu", this.width / 2, this.height / 4 + (-25) + 12, 0xFFFFFF);
		drawCenteredString(this.fontRenderer, "Commit data not implemented" , this.width / 2, this.height / 4 + (-25) + 24, 0xFFFFFF);
		if (this.profileNameTextBox != null) {
			this.profileNameTextBox.drawTextBox();
		}
		if (this.configKeyTextBox != null) {
			this.configKeyTextBox.drawTextBox();
		}
		if (this.configValueTextBox != null) {
			this.configValueTextBox.drawTextBox();
		}
		super.drawScreen(par1, par2, par3);
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
    @Override
	public void confirmClicked(boolean doOpen, int status)
    {
        if (status == 0)
        {
            if (doOpen)
            {
                this.OpenURI(this.clickedURI);
            }

            this.clickedURI = null;
            this.mc.displayGuiScreen(this);
        }
    }
	

    private void OpenURI(URI uri)
    {
        try
        {
            Class<?> var2 = Class.forName("java.awt.Desktop");
            Object var3 = var2.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            var2.getMethod("browse", new Class[] {URI.class}).invoke(var3, new Object[] {uri});
        }
        catch (Throwable var4)
        {}
    }


}
