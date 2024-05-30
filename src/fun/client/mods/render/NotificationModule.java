package fun.client.mods.render;

import com.darkmagician6.eventapi.event.events.EventRender2D;
import fun.client.mods.Category;
import fun.client.mods.Module;
import fun.utils.render.Notification;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;

public class NotificationModule extends Module {
    public ArrayList<Notification> notifications=new ArrayList<>();
    public NotificationModule() {
        super("Notification",Category.RENDER);
    }
    public void post(Notification notification){
        notifications.add(notification);
    }

    @Override
    public void onRender2D(EventRender2D event) {
        super.onRender2D(event);
        ArrayList<Notification> removes=new ArrayList<>();
        for (int i = 0, notificationsSize = notifications.size(); i < notificationsSize; i++) {
            Notification n = notifications.get(i);
            if (n.tick <= 0) {
                removes.add(n);
            }
            n.setTargetPos(new Vector2f(n.scaledResolution.getScaledWidth(),
                    n.scaledResolution.getScaledHeight()-(i*(5+n.height))));
            n.render();
        }
        notifications.removeAll(removes);
    }
}
