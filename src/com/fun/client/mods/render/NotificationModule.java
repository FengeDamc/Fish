package com.fun.client.mods.render;

import com.darkmagician6.eventapi.event.events.EventRender2D;
import com.darkmagician6.eventapi.event.events.EventTick;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.utils.render.Notification;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;

public class NotificationModule extends Module {
    public ArrayList<Notification> notifications=new ArrayList<>();
    public NotificationModule() {
        super("Notification",Category.RENDER);
    }
    public void post(Notification notification){
        if(notification.type== Notification.Type.WHITE||isRunning())notifications.add(notification);
    }


    public void render(EventRender2D event) {
        super.onRender2D(event);
        for (int i = 0, notificationsSize = notifications.size(); i < notificationsSize; i++) {
            Notification n = notifications.get(i);
            n.render();
        }
    }
    public void tick(EventTick event) {
        ArrayList<Notification> removes=new ArrayList<>();
        for (int i = 0, notificationsSize = notifications.size(); i < notificationsSize; i++) {
            Notification n = notifications.get(i);
            if (n.tick <System.currentTimeMillis()) {
                removes.add(n);
            }
            n.setTargetPos(new Vector2f(n.scaledResolution.getScaledWidth(),
                    n.scaledResolution.getScaledHeight()-(i*(5+n.height))));
            n.tick();

        }
        notifications.removeAll(removes);
    }
}
