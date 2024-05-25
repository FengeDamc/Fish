package fun.client.utils;

import javax.vecmath.Vector2d;
import java.awt.*;

public class ColorUtils {
    public static int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return Color.getHSBColor((hue /= (float)speed), 0.8f, 1.0f).getRGB();
    }
    public static Color mixColor(int speed, int offset,final Color color1, final Color color2){
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return mixColors(color1,color2,hue/=speed);

    }
    public static Color mixColor(int speed, int offset,final int color1, final int color2){
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return mixColors(new Color(color1),new Color(color2),hue/=speed);

    }
    public static double getBlendFactor(Vector2d screenCoordinates) {
        return Math.sin(System.currentTimeMillis() / 600.0D
                + screenCoordinates.getX() * 0.005D
                + screenCoordinates.getY() * 0.06D
        ) * 0.5D + 0.5D;
    }

    public static Color mixColors(final Color color1, final Color color2, final double percent) {
        final double inverse_percent = 1.0 - percent;
        final int redPart = (int) (color1.getRed() * percent + color2.getRed() * inverse_percent);
        final int greenPart = (int) (color1.getGreen() * percent + color2.getGreen() * inverse_percent);
        final int bluePart = (int) (color1.getBlue() * percent + color2.getBlue() * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }

}
