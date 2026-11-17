package gui.theme;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/** Hinh ly ca phe do nguoi dung cung cap, dong goi trong src/resources. */
public final class CoffeeBackground {
    private static final BufferedImage IMAGE = load();

    private CoffeeBackground() { }

    private static BufferedImage load() {
        try (InputStream in = CoffeeBackground.class.getResourceAsStream(
                "/resources/coffee-background.png")) {
            return in == null ? null : ImageIO.read(in);
        } catch (IOException e) {
            return null; // van co mau nen du phong neu image bi mat
        }
    }

    public static boolean isLoaded() {
        return IMAGE != null;
    }

    /** Ve anh tran khung, giu nguyen ti le, can giua. */
    public static void drawCover(Graphics2D g, int width, int height) {
        if (IMAGE == null || width <= 0 || height <= 0) return;
        double scale = Math.max((double)width / IMAGE.getWidth(),
                                (double)height / IMAGE.getHeight());
        int scaledW = (int)Math.ceil(IMAGE.getWidth() * scale);
        int scaledH = (int)Math.ceil(IMAGE.getHeight() * scale);
        int x = (width - scaledW) / 2;
        int y = (height - scaledH) / 2;
        g.drawImage(IMAGE, x, y, scaledW, scaledH, null);
    }

    /** Cua so anh hep de thay ro ly ca phe o goc trai file anh. */
    public static void drawCupWindow(Graphics2D g, int width, int height) {
        if (IMAGE == null || width <= 0 || height <= 0) return;
        int sx1 = 10, sy1 = 355;
        int sx2 = 1040, sy2 = 1260;
        g.drawImage(IMAGE, 0, 0, width, height, sx1, sy1, sx2, sy2, null);
    }
}
