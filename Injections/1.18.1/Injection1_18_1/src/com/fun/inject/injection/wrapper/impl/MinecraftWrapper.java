package com.fun.inject.injection.wrapper.impl;


import com.fun.inject.injection.wrapper.impl.entity.EntityPlayerSPWrapper;
import com.fun.inject.injection.wrapper.impl.network.NetHandlerPlayClientWrapper;
import com.fun.inject.injection.wrapper.impl.other.TimerWrapper;
import com.fun.inject.injection.wrapper.impl.render.EntityRendererWrapper;
import com.fun.inject.injection.wrapper.impl.render.FontRendererWrapper;
import com.fun.inject.injection.wrapper.impl.render.FramebufferWrapper;
import com.fun.inject.injection.wrapper.impl.render.RenderManagerWrapper;
import com.fun.inject.injection.wrapper.impl.setting.GameSettingsWrapper;
import com.fun.inject.injection.wrapper.impl.util.MouseHelperWrapper;
import com.fun.inject.injection.wrapper.impl.world.WorldClientWrapper;
import com.fun.utils.version.fields.Fields;
import com.fun.utils.version.methods.Methods;
import com.fun.inject.Agent;
import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.inject.injection.wrapper.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;


public class MinecraftWrapper extends Wrapper {
    private static final String CLASS = "net/minecraft/client/Minecraft";

    private static MinecraftWrapper instance;

    private final Minecraft minecraftObj;

    private Object currentScreenObj;

    private EntityPlayerSPWrapper thePlayer=null;
    private WorldClientWrapper theWorld=null;

    private EntityRendererWrapper entityRenderer;
    private RenderManagerWrapper renderManager;
    private GameSettingsWrapper gameSettings;
    private NetHandlerPlayClientWrapper netHandlerPlayClientWrapper;
    private FramebufferWrapper framebufferWrapper;
    private TimerWrapper timer;

    private FontRendererWrapper fontRendererWrapper;

    private MinecraftWrapper(Minecraft obj) {
        super(CLASS);
        this.minecraftObj = obj;
    }

    public GameSettingsWrapper getGameSettings() {
        if (gameSettings == null) {
            try {
                gameSettings = new GameSettingsWrapper(ReflectionUtils.getFieldValue(minecraftObj, Mappings.getObfField("field_71474_y")));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        return gameSettings;
    }

    public TimerWrapper getTimer() {
        if (timer == null) {
            try {
                timer = new TimerWrapper(ReflectionUtils.getFieldValue(minecraftObj, Mappings.getObfField("field_71428_T")));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return timer;
    }

    public RenderManagerWrapper getRenderManager() {
        if (renderManager == null) {
            try {
                renderManager = new RenderManagerWrapper(ReflectionUtils.getFieldValue(minecraftObj, Mappings.getObfField("field_175616_W")));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        return renderManager;
    }

    public EntityRendererWrapper getEntityRenderer() {
        if (entityRenderer == null) {
            try {
                entityRenderer = new EntityRendererWrapper(ReflectionUtils.getFieldValue(minecraftObj, Mappings.getObfField("field_71460_t")));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        return entityRenderer;
    }

    public HitResult getHitResult() {

        try {
            Object value = ReflectionUtils.getFieldValue(minecraftObj, Mappings.getObfField("field_71476_x"));

            if (value == null) return null;

            return new HitResult(value);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }

    public EntityPlayerSPWrapper getPlayer() {
        return new EntityPlayerSPWrapper().setEntityObj(minecraftObj.player);
    }
    public MouseHelperWrapper getMouseHelper() {
        return new MouseHelperWrapper(Fields.mouseHelper.get(minecraftObj));
    }



    public WorldClientWrapper getWorld() {
        WorldClientWrapper w=new WorldClientWrapper();
        w.setWorldObj(minecraftObj.level);
        return w;
    }

    public Object getCurrentScreen() {
        return currentScreenObj;
    }

    public void setCurrentScreen(Object currentScreenObj) {
        this.currentScreenObj = currentScreenObj;
    }

    public void displayGuiScreen(Screen guiScreen) {
        minecraftObj.setScreen(guiScreen);
    }

    public int getDisplayWidth() {
        return minecraftObj.getWindow().getWidth();//(Integer) ReflectionUtils.getFieldValue(minecraftObj, Mappings.getObfField("field_71443_c"));
    }

    public int getDisplayHeight() {
        return minecraftObj.getWindow().getHeight();
    }

    public FontRendererWrapper getFontRenderer() {
        return new FontRendererWrapper(minecraftObj.font);
    }

    public NetHandlerPlayClientWrapper getNetHandler() {

        return new NetHandlerPlayClientWrapper(minecraftObj.getConnection());
    }

    public FramebufferWrapper getFramebuffer() {
        if (framebufferWrapper == null) {
            framebufferWrapper = new FramebufferWrapper(ReflectionUtils.invokeMethod(minecraftObj, Mappings.getObfMethod("func_147110_a")));
        }
        return framebufferWrapper;
    }

    public void setLeftClickCounter(int delay) {
        try {
            ReflectionUtils.setFieldValue(minecraftObj, Mappings.getObfField("field_71429_W"), delay);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void clickMouse() {
        try {
            ReflectionUtils.invokeMethod(minecraftObj, Mappings.getObfMethod("func_147116_af"));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void rightClickMouse() {
        try {
            ReflectionUtils.invokeMethod(minecraftObj, Mappings.getObfMethod("func_147121_ag"));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    //MD: le/a (Ljava/lang/Runnable;)Lcom/google/common/util/concurrent/ListenableFuture; net/minecraft/world/WorldServer/func_152344_a (Ljava/lang/Runnable;)Lcom/google/common/util/concurrent/ListenableFuture;
    public void addScheduledTask(Runnable runnable) {
        minecraftObj.execute(runnable);
    }

    public Object getMinecraftObj() {
        return minecraftObj;
    }

    public static MinecraftWrapper get() {
        if(Agent.isAgent) {
            try {
                return new MinecraftWrapper(Minecraft.getInstance());//new MinecraftWrapper(ReflectionUtils.invokeMethod(Agent.findClass(Mappings.getObfClass(CLASS)), Mappings.getObfMethod("func_71410_x")));
            } catch (Exception e) {
            }
        }
        return null;
    }
}
