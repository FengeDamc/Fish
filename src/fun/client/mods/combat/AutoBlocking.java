package fun.client.mods.combat;

import com.darkmagician6.eventapi.event.events.EventRender3D;
import fun.client.mods.Category;
import fun.client.mods.Module;
import fun.inject.inject.wrapper.impl.setting.GameSettingsWrapper;
import fun.inject.inject.wrapper.impl.setting.KeyBindingWrapper;
import net.java.games.input.Controller;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class AutoBlocking extends Module {
    public AutoBlocking() {
        super("AutoBlocking",Category.Combat);

    }

    @Override
    public void onRender3D(EventRender3D event) {
        super.onRender3D(event);
        mc.getGameSettings().getKey(GameSettingsWrapper.USE).setPressed(Mouse.isButtonDown(1));
        mc.getGameSettings().getKey(GameSettingsWrapper.ATTACK).setPressed(Mouse.isButtonDown(0));

    }
}
