package fun.inject.inject.wrapper.impl;


import fun.inject.inject.Mappings;
import fun.inject.inject.ReflectionUtils;
import fun.inject.inject.wrapper.Wrapper;
import fun.inject.inject.wrapper.impl.entity.EntityPlayerSPWrapper;
import fun.inject.inject.wrapper.impl.network.NetHandlerPlayClientWrapper;
import fun.inject.inject.wrapper.impl.other.TimerWrapper;
import fun.inject.inject.wrapper.impl.render.EntityRendererWrapper;
import fun.inject.inject.wrapper.impl.render.FontRendererWrapper;
import fun.inject.inject.wrapper.impl.render.FramebufferWrapper;
import fun.inject.inject.wrapper.impl.render.RenderManagerWrapper;
import fun.inject.inject.wrapper.impl.setting.GameSettingsWrapper;
import fun.inject.inject.wrapper.impl.world.WorldClientWrapper;

public class MinecraftWrapper extends Wrapper {
    private static final String CLASS = "net/minecraft/client/Minecraft";

    private static MinecraftWrapper instance;

    private final Object minecraftObj;

    private Object currentScreenObj;

    private final EntityPlayerSPWrapper thePlayer = new EntityPlayerSPWrapper();
    private final WorldClientWrapper theWorld = new WorldClientWrapper();

    private EntityRendererWrapper entityRenderer;
    private RenderManagerWrapper renderManager;
    private GameSettingsWrapper gameSettings;
    private NetHandlerPlayClientWrapper netHandlerPlayClientWrapper;
    private FramebufferWrapper framebufferWrapper;
    private TimerWrapper timer;

    private FontRendererWrapper fontRendererWrapper;

    private MinecraftWrapper(Object obj) {
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

        try {
            Object value = ReflectionUtils.getFieldValue(minecraftObj, Mappings.getObfField("field_71439_g"));
            thePlayer.setPlayerObj(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return thePlayer;
    }

    public WorldClientWrapper getWorld() {
        try {
            Object value = ReflectionUtils.getFieldValue(minecraftObj, Mappings.getObfField("field_71441_e"));
            theWorld.setWorldObj(value);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return theWorld;
    }

    public Object getCurrentScreen() {
        return currentScreenObj;
    }

    public void setCurrentScreen(Object currentScreenObj) {
        this.currentScreenObj = currentScreenObj;
    }

    public void displayGuiScreen(Object guiScreen) {
        try {
            ReflectionUtils.invokeMethod(minecraftObj, Mappings.getObfMethod("func_147108_a"), new Class[]{Class.forName(Mappings.getObfClass("net.minecraft.client.gui.GuiScreen"))}, guiScreen);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public int getDisplayWidth() {
        return (Integer) ReflectionUtils.getFieldValue(minecraftObj, Mappings.getObfField("field_71443_c"));
    }

    public int getDisplayHeight() {
        return (Integer) ReflectionUtils.getFieldValue(minecraftObj, Mappings.getObfField("field_71440_d"));
    }

    public FontRendererWrapper getFontRenderer() {
        if (fontRendererWrapper == null) {
            try {
                fontRendererWrapper = new FontRendererWrapper(ReflectionUtils.getFieldValue(minecraftObj, Mappings.getObfField("field_71466_p")));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        return fontRendererWrapper;
    }

    public NetHandlerPlayClientWrapper getNetHandler() {
        if (netHandlerPlayClientWrapper == null) {
            netHandlerPlayClientWrapper = new NetHandlerPlayClientWrapper(getPlayer().getSendQueue());
        }
        return netHandlerPlayClientWrapper;
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

    public Object getMinecraftObj() {
        return minecraftObj;
    }

    public static MinecraftWrapper get() {
        if (instance == null) {
            try {
                instance = new MinecraftWrapper(ReflectionUtils.invokeMethod(Class.forName(Mappings.getObfClass(CLASS)), Mappings.getObfMethod("func_71410_x")));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        return instance;
    }
}
