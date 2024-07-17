package com.fun.client.mods.render;

import com.fun.eventapi.event.events.EventRender2D;
import com.fun.eventapi.event.events.EventTick;
import com.fun.client.mods.render.notify.Notification;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
public class NotificationModule extends Module {
    public ArrayList<Notification> notifications=new ArrayList<>();
    public NotificationModule() {
        super("Notification",Category.Render);
    }
    public void post(Notification notification){
        if(notification.type== Notification.Type.WHITE||isRunning())notifications.add(notification);
    }


    public void render(EventRender2D event) {
        super.onRender2D(event);
        for (int i = 0, notificationsSize = notifications.size(); i < notificationsSize; i++) {
            Notification n = notifications.get(i);
            n.render();
            System.out.println("render notify");
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
