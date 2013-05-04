package julialy.xray;

import java.util.List;

import julialy.xray.main.XrayMain;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;


import org.lwjgl.input.Keyboard;
 
public class GuiXRAYMain extends GuiScreen
{
private GuiScreen parentScreen;
   protected String screenTitle = "XRAY Settings";
   private final GameSettings options;
   private int buttonId;
   private GuiTextField chatBufferTextField;
   //private GuiTextField chatScrollTextField;
 
   public GuiXRAYMain(GuiScreen var1, GameSettings var2)
  {
     this.parentScreen = var1;
    this.options = var2;
  }

  public void initGui()
 {
    int var1 = this.height / 4 + 48;
    StringTranslate var2 = StringTranslate.getInstance();
    Keyboard.enableRepeatEvents(true);
     this.buttonList.clear();
     this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 125 + 12, var2.translateKey("gui.done")));
     this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 150 + 12, "Select blocks"));
this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 175 + 12, "Save current profile then load profile"));
     this.screenTitle = "Chat Settings";
     this.chatBufferTextField = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 145, 200, 20);
    this.chatBufferTextField.setMaxStringLength(32);
    this.chatBufferTextField.setFocused(true);
    this.chatBufferTextField.setText(XrayMain.getXrayInstance().currentSelectedProfile);
   //this.chatScrollTextField = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 185, 200, 20);
   //this.chatScrollTextField.setMaxStringLength(2);
   //this.chatScrollTextField.setFocused(false);
   //this.chatScrollTextField.setText(mod_xray.currentSelectedProfile);
    ((GuiButton)this.buttonList.get(0)).enabled = ((this.chatBufferTextField.getText().length() > 0));
   }

 protected void actionPerformed(GuiButton var1)
  {
    if (var1.enabled)
    {
      if (var1.id == 1)
     {
        this.mc.displayGuiScreen(new GuiBlockSelect());
      }
     else if (var1.id == 0)
      {
       //GameSettingsSP.chatBuffer = Integer.parseInt(this.chatBufferTextField.getText());
        //GameSettingsSP.chatScroll = Integer.parseInt(this.chatScrollTextField.getText());
        //mod_SuperControlPack.agamevr.saveOptions();
       this.mc.displayGuiScreen(this.parentScreen);
      }
else if (var1.id == 2){
	XrayMain.getXrayInstance().loadBlackListName(this.chatBufferTextField.getText());
	XrayMain.getXrayInstance().rerendereverything(true);
	this.mc.displayGuiScreen(null);
}
   }
  }

 protected void mouseClicked(int var1, int var2, int var3)
  {
    super.mouseClicked(var1, var2, var3);
     this.chatBufferTextField.mouseClicked(var1, var2, var3);
     //this.chatScrollTextField.mouseClicked(var1, var2, var3);
   }

   protected void keyTyped(char var1, int var2)
  {
    this.chatBufferTextField.textboxKeyTyped(var1, var2);
    //this.chatScrollTextField.textboxKeyTyped(var1, var2);

  if (var1 == '\t')
  {
     if (this.chatBufferTextField.isFocused())
    {
       this.chatBufferTextField.setFocused(false);
      //this.chatScrollTextField.setFocused(true);
      }
      else
      {
       this.chatBufferTextField.setFocused(true);
      //this.chatScrollTextField.setFocused(false);
      }
    }

    if (var1 == '\034')
    {
      actionPerformed((GuiButton)this.buttonList.get(0));
     }

    ((GuiButton)this.buttonList.get(0)).enabled = ((this.chatBufferTextField.getText().length() > 0));
  }
   
   public void drawScreen(int var1, int var2, float var3)
   {
	   StringTranslate var4 = StringTranslate.getInstance();
    drawDefaultBackground();
    drawCenteredString(this.fontRenderer, "67.212.180.41", this.width / 2, 20, 16776960);
    drawCenteredString(this.fontRenderer, "JOIN NAO", this.width / 2, 40, 10526880);
    drawCenteredString(this.fontRenderer, "Best Server EVER", this.width / 2, 50, 10526880);
  drawCenteredString(this.fontRenderer, "67.212.180.41", this.width / 2, 60, 10526880);
  drawCenteredString(this.fontRenderer, "X-Ray options", this.width / 2, 110, 16777215);
  drawString(this.fontRenderer, "Profile name:", this.width / 2 - 100, 130, 10526880);
    //drawString(this.fontRenderer, "Chat scroll speed (normal 7):", this.width / 2 - 100, 170, 10526880);
    this.chatBufferTextField.drawTextBox();
    //this.chatScrollTextField.drawTextBox();
    	super.drawScreen(var1, var2, var3);
   }
}
