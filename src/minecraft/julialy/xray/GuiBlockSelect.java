package julialy.xray;

import java.util.ArrayList;
import java.util.List;

import julialy.xray.main.XrayMain;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;


import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiBlockSelect extends GuiScreen
{
    List fullList = new ArrayList();
    List invisible = new ArrayList();
    int listpos = 0;
    //String nazwa = "xrayProfile1.txt";

    public GuiBlockSelect()
    {
        for (int var1 = 0; var1 < Block.blocksList.length; ++var1)
        {
            if (Block.blocksList[var1] != null)
            {
                this.fullList.add(new Integer(var1));
            }
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        for (int var1 = 0; var1 < XrayMain.getXrayInstance().blackList.length; ++var1)
        {
            this.invisible.add(Integer.valueOf(XrayMain.getXrayInstance().blackList[var1]));
        }

        Keyboard.enableRepeatEvents(true);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        if (var1.id != 0)
        {
            this.invisible.add(new Integer(var1.id));
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();
        int var2 = Mouse.getEventButton();

        if (var1 > 0)
        {
            var1 = 1;
        }

        if (var1 < 0)
        {
            var1 = -1;
        }

        boolean var3;

        if (var1 > 0 && this.listpos > 0)
        {
            --this.listpos;
            var3 = false;
        }
        else if (var1 < 0 && this.listpos < this.fullList.size() - 1)
        {
            ++this.listpos;
            var3 = false;
        }

        if (Mouse.getEventButtonState() && var2 != -1 && !this.invisible.remove(new Integer(((Integer)this.fullList.get(this.listpos)).intValue())))
        {
            this.invisible.add(new Integer(((Integer)this.fullList.get(this.listpos)).intValue()));
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {
        if (var2 != 1 && var2 != 14 && var2 != 29 && var2 != 157)
        {
            if (var2 == 200 && this.listpos > 0)
            {
                --this.listpos;
            }
            else if (var2 == 208 && this.listpos < this.fullList.size() - 1)
            {
                ++this.listpos;
            }
            else if (var2 == 28 && !this.invisible.remove(new Integer(((Integer)this.fullList.get(this.listpos)).intValue())))
            {
                this.invisible.add(new Integer(((Integer)this.fullList.get(this.listpos)).intValue()));
            }
            else 
            {
            	java.awt.Toolkit.getDefaultToolkit().beep(); //Beep!
            }
        }
        else
        {
            Object var3 = null;
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)var3);

            if (XrayMain.getXrayInstance().on)
            {
            	XrayMain.getXrayInstance().rerendereverything(true);
            }
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
    	XrayMain.getXrayInstance().blackList = new int[this.invisible.size()];

        for (int var1 = 0; var1 < this.invisible.size(); ++var1)
        {
        	XrayMain.getXrayInstance().blackList[var1] = Integer.valueOf(((Integer)this.invisible.get(var1)).intValue()).intValue();
        }

        XrayMain.getXrayInstance().saveBlackList(XrayMain.getXrayInstance().nazwa);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        int var4 = this.width / 2 + 100;
        int var5 = this.width / 2 - 100;
        int var6 = this.height / 3;
        drawRect(var4 + 2, var6 + 12, var5 - 2, var6 - 12, -4144960);
        int var7 = this.listpos - 2;

        for (int var8 = 0; var8 < 7; ++var8)
        {
            if (var7 >= 0 && var7 < this.fullList.size())
            {
                if (this.invisible.indexOf(new Integer(((Integer)this.fullList.get(var7)).intValue())) >= 0)
                {
                    drawRect(var4, var6 + 10 - 48 + var8 * 24, var5, var6 - 10 - 48 + var8 * 24, -65536);
                }

                this.drawString(this.fontRenderer, var7 + 1 + ": ", this.width / 2 - 66, var6 - 60 + 7 + var8 * 24, 16777215);
                String var9 = Block.blocksList[((Integer)this.fullList.get(var7)).intValue()].getLocalizedName();

                if (var9 == null)
                {
                    var9 = "Unknown";
                }
                else
                {
                    //var9 = var9.substring(5);
                }

                this.drawCenteredString(this.fontRenderer, var9, this.width / 2, var6 - 60 + 7 + var8 * 24, 16777215);
                
                //TODO: fix icons
                
                /*
                int var10 = Block.blocksList[((Integer)this.fullList.get(var7)).intValue()].blockIndexInTexture;

                if (var10 >= 0)
                {
                    GL11.glPushMatrix();
                    this.mc.renderEngine.bindTexture("/terrain.png");
                    this.drawTexturedModalRect(this.width / 2 - 97, var6 - 60 + 4 + var8 * 24, var10 % 16 * 16, var10 / 16 * 16, 16, 16);
                    GL11.glPopMatrix();
                }
                */
            }

            ++var7;
        }

        super.drawScreen(var1, var2, var3);
    }
}
