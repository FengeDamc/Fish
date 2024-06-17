package fun.utils.font;



import java.awt.*;
import java.io.InputStream;

public class FontManager {
    public static UnicodeFontRenderer inkFree;
    public static UnicodeFontRenderer tenacity;
    public static UnicodeFontRenderer tenacity20;
    public static UnicodeFontRenderer simkai;
    public static void init(){
        Font inkfree=getFont(35,FontManager.class.getResourceAsStream("/assets/fonts/Inkfree.ttf"));
        Font tenacityF=getFont(25,FontManager.class.getResourceAsStream("/assets/fonts/tenacity.ttf"));
        inkFree=new UnicodeFontRenderer(inkfree);
        tenacity=new UnicodeFontRenderer(tenacityF);
        tenacity20=new UnicodeFontRenderer(getFont(20,FontManager.class.getResourceAsStream("/assets/fonts/tenacity.ttf")));
        simkai=new UnicodeFontRenderer(getFont(25,FontManager.class.getResourceAsStream("/assets/fonts/simkai.ttf")));

    }
    public static Font getFont(int size, InputStream is) {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT,is).deriveFont(Font.PLAIN, (float) size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return font;
    }

}
