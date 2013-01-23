package julialy.xray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.src.BaseMod;
import net.minecraft.src.ModLoader;

import org.lwjgl.input.Keyboard;

public class mod_xray extends BaseMod
{
    public static Minecraft mc;
    public static boolean on = false;
    public static boolean cavefinder = false;
    public static int lightmode = 0;
    public static int[] blackList = new int[] {1, 2, 3, 4, 7, 12, 13, 24, 87, 8, 9, 78, 79, 80};
    private KeyBinding[] keys;
    public static String nazwa = "xrayProfile1.txt";

    public mod_xray()
    {
        mc = ModLoader.getMinecraftInstance();
        loadBlackList("xrayProfile1.txt");
        System.out.println("X-ray is currently installed! Version: " + this.getVersion());
        this.keys = new KeyBinding[8];
        this.keys[0] = new KeyBinding("xray", 45);
        this.keys[1] = new KeyBinding("Brightness", 46);
        this.keys[2] = new KeyBinding("Cave Finder", 47);
        this.keys[3] = new KeyBinding("Profile 1", 79);
        this.keys[4] = new KeyBinding("Profile 2", 80);
        this.keys[5] = new KeyBinding("Profile 3", 81);
        this.keys[6] = new KeyBinding("Profile 4", 75);
        this.keys[7] = new KeyBinding("Profile 5", 76);
        System.out.println("Please ignore the following messages, that appear.");
        ModLoader.setInGameHook(this, true, false); //is this the "game activity during mod construction"?
        ModLoader.setInGUIHook(this, true, false);
        KeyBinding[] var1 = this.keys;
        int var2 = this.keys.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            KeyBinding var4 = var1[var3];
            ModLoader.registerKey(this, var4, false);
        }
    }

    public void keyboardEvent(KeyBinding var1)
    {
        if (mc.inGameHasFocus)
        {
            if (mc.currentScreen == null || mc.thePlayer != null)
            {
                if (var1 == this.keys[0])
                {
                    if (!Keyboard.isKeyDown(29) && !Keyboard.isKeyDown(157))
                    {
                        on = !on;
                        mc.renderGlobal.loadRenderers();
                        mc.entityRenderer.updateRenderer();
                    }
                    else
                    {
                    	System.out.println("Use the arrow keys and enter key to select blocks.");
                        mc.displayGuiScreen(new GuiBlockSelect());
                    }

                    cavefinder = false;
                }
                else if (var1 == this.keys[1])
                {
                    ++lightmode;

                    if (lightmode == 3)
                    {
                        lightmode = 0;
                        mc.renderGlobal.loadRenderers();
                        mc.entityRenderer.updateRenderer();
                    }

                    if (lightmode == 2)
                    {
                        mc.renderGlobal.loadRenderers();
                        mc.entityRenderer.updateRenderer();
                    }

                    mc.entityRenderer.updateRenderer();
                }
                else if (var1 == this.keys[2])
                {
                    cavefinder = !cavefinder;
                    mc.renderGlobal.loadRenderers();
                    mc.entityRenderer.updateRenderer();
                    on = false;
                }

                if (var1 == this.keys[3])
                {
                    loadBlackList("xrayProfile1.txt");
                    nazwa = "xrayProfile1.txt";
                    mc.renderGlobal.loadRenderers();
                    mc.entityRenderer.updateRenderer();
                }
                else if (var1 == this.keys[4])
                {
                    loadBlackList("xrayProfile2.txt");
                    nazwa = "xrayProfile2.txt";
                    mc.renderGlobal.loadRenderers();
                    mc.entityRenderer.updateRenderer();
                }
                else if (var1 == this.keys[5])
                {
                    loadBlackList("xrayProfile3.txt");
                    nazwa = "xrayProfile3.txt";
                    mc.renderGlobal.loadRenderers();
                    mc.entityRenderer.updateRenderer();
                }
                else if (var1 == this.keys[6])
                {
                    loadBlackList("xrayProfile4.txt");
                    nazwa = "xrayProfile4.txt";
                    mc.renderGlobal.loadRenderers();
                    mc.entityRenderer.updateRenderer();
                }
                else if (var1 == this.keys[7])
                {
                    loadBlackList("xrayProfile5.txt");
                    nazwa = "xrayProfile5.txt";
                    mc.renderGlobal.loadRenderers();
                    mc.entityRenderer.updateRenderer();
                }
            }
        }
    }

    public static void loadBlackList(String var0)
    {
        try
        {
            int[] var1 = new int[512];
            File var2 = new File(Minecraft.getMinecraftDir(), var0);

            if (var2.exists())
            {
                blackList = null;
                BufferedReader var3 = new BufferedReader(new FileReader(var2));
                int var4;
                String var5;

                for (var4 = 0; (var5 = var3.readLine()) != null; ++var4)
                {
                    var1[var4] = Integer.parseInt(var5);
                }

                blackList = new int[var4];
                System.arraycopy(var1, 0, blackList, 0, var4);
            }
        }
        catch (Exception var6)
        {
            System.err.print(var6.toString());
        }
    }

    public static void saveBlackList(String var0)
    {
        try
        {
            File var1 = new File(Minecraft.getMinecraftDir(), var0);
            BufferedWriter var2 = new BufferedWriter(new FileWriter(var1));
            int[] var3 = blackList;
            int var4 = blackList.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                int var6 = var3[var5];
                var2.write(var6 + "\r\n");
            }

            var2.close();
        }
        catch (Exception var7)
        {
            System.err.print(var7.toString());
        }
    }

    public String getVersion()
    {
        return "X-RAY 1.4.7; compiled by julialy";
    }

    public void load() {}
}
