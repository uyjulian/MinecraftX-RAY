package julialy.xray.litemod;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import julialy.xray.main.XrayMain;
import net.minecraft.client.Minecraft;
import net.minecraft.src.KeyBinding;

import com.mumfrey.liteloader.InitCompleteListener;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.core.LiteLoader;

public class LiteModXray implements LiteMod, InitCompleteListener {
	
    private boolean[] pressed = new boolean[400];

	@Override
	public String getName() {
		return "X-Ray";
	}

	@Override
	public String getVersion() {
		return "1.5.1";
	}

	@Override
	public void init() {
		XrayMain.getXrayInstance();
	}

	@Override
	public void onTick(Minecraft var1, float var2, boolean var3, boolean var4) {
		XrayMain.getXrayInstance().onTick(var1, var2, var3, var4);
	}

	@Override
	public void onInitCompleted(Minecraft minecraft, LiteLoader loader) {
		// TODO Nothing
		
	}

    private boolean getKeyPressed(int var1)
    {
        return var1 < 0 ? Mouse.isButtonDown(var1 + 100) : Keyboard.isKeyDown(var1);
    }


}
