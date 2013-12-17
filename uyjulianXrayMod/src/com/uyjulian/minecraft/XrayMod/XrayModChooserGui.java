package com.uyjulian.minecraft.XrayMod;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.*;

public class XrayModChooserGui extends GuiScreen {
	UyjuliansXrayModMain modInstance = UyjuliansXrayModMain.getModInstance();
	List<Integer> idList = new ArrayList<Integer>();
	List<Integer> invisibleIdList = new ArrayList<Integer>();
	int listPos = 0;

	public XrayModChooserGui() {
		for (int i = 0; i < Block.blocksList.length; ++i) {
			if (Block.blocksList[i] != null) {
				this.idList.add(i);
			}
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		int[] blockListCache = modInstance.blockList;
		for (int i = 0; i < blockListCache.length; ++i) {
			this.invisibleIdList.add(blockListCache[i]);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		super.actionPerformed(par1GuiButton);
		if (par1GuiButton.id != 0) {
			this.invisibleIdList.add(par1GuiButton.id);
		}
	}
	
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int mouseScrollWheelStatus = Mouse.getEventDWheel();
		int mouseButtonStatus = Mouse.getEventButton();
		boolean mouseButtonState = Mouse.getEventButtonState();
		if ((mouseScrollWheelStatus > 0) && (this.listPos > 0)) {
			--this.listPos;
		}
		else if ((mouseScrollWheelStatus < 0) && (this.listPos < this.idList.size() - 1)) {
			++this.listPos;
		}
		if ((mouseButtonState) && (mouseButtonStatus != -1))  {
			if ((this.invisibleIdList.indexOf(this.idList.get(this.listPos).intValue()) >= 0)) {
				this.invisibleIdList.remove(this.idList.get(this.listPos));
			}
			else {
				this.invisibleIdList.add(this.idList.get(this.listPos));
			}
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
		if (par2 != 1 && par2 != 14 && par2 != 29 && par2 != 157) {
			if (((par2 == 200) || (par1 == 'w') || (par1 == 'W')) && (this.listPos > 0)) {
				--this.listPos;
			}
			else if (((par2 == 208) || (par1 == 's') || (par1 == 'S')) && (this.listPos < this.idList.size() - 1)) {
				++this.listPos;
			}
			else if (par2 == 28) {
				if ((this.invisibleIdList.indexOf(this.idList.get(this.listPos).intValue()) >= 0)) {
					this.invisibleIdList.remove(this.idList.get(this.listPos));
				}
				else {
					this.invisibleIdList.add(this.idList.get(this.listPos));
				}
			}
			else {
				java.awt.Toolkit.getDefaultToolkit().beep();
			}
		}
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(true);
		int[] blockListCache = new int[this.invisibleIdList.size()];
		for (int i = 0; i < this.invisibleIdList.size(); ++i) {
			blockListCache[i] = this.invisibleIdList.get(i).intValue();
		}
		modInstance.blockList = blockListCache;
		modInstance.saveBlockList(modInstance.currentBlocklistName);
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		int widthPlus100 = this.width / 2 + 100;
		int widthMinus100 = this.width / 2 - 100;
		int heightCalc = this.height / 3;
		int renderPosition = this.listPos - 2;
		drawRect(widthPlus100 + 2, heightCalc + 12, widthMinus100 - 2, heightCalc - 12, -4144960);
		for (int i = 0; i < 7; ++i) {
			if (renderPosition >= 0 && renderPosition < this.idList.size()) {
				if (this.invisibleIdList.indexOf((this.idList.get(renderPosition)).intValue()) >= 0) {
					drawRect(widthPlus100, heightCalc + 10 - 48 + i * 24, widthMinus100, heightCalc - 10 - 48 + i * 24, -65536);
				}
				drawString(this.fontRenderer, renderPosition + 1 + ": ", this.width / 2 - 66, heightCalc - 60 + 7 + i * 24, 16777215);
				String currentBlockName = Block.blocksList[this.idList.get(renderPosition).intValue()].getLocalizedName();
				if (currentBlockName == null) {
					currentBlockName = "No Name";
				}
				drawCenteredString(this.fontRenderer, currentBlockName, this.width / 2, heightCalc - 60 + 7 + i * 24, 16777215);
			}
			++renderPosition;
		}
		super.drawScreen(par1, par2, par3);
	}

}
