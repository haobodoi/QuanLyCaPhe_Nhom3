package dev;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import gui.MainView;
import gui.theme.Theme;

/** Xuat screenshot offline: java -Djava.awt.headless=true dev.PreviewRenderer path.png */
public final class PreviewRenderer {
    private PreviewRenderer() { }

    public static void main(String[] args) throws Exception {
        final String path = args.length == 0 ? "preview.png" : args[0];
        final String pageId = args.length <= 1 ? "home" : args[1];
        SwingUtilities.invokeAndWait(() -> {
            try {
                Theme.installDefaults();
                MainView view = new MainView();
                view.setSize(1366, 768);
                view.showPage(pageId);
                layoutDeep(view);
                RepaintManager.currentManager(view).setDoubleBufferingEnabled(false);
                BufferedImage img = new BufferedImage(1366, 768, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = img.createGraphics();
                view.printAll(g);
                g.dispose();
                ImageIO.write(img, "png", new File(path));
                System.out.println("Saved screenshot to " + path);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        System.exit(0);
    }

    private static void layoutDeep(Component c) {
        if (c instanceof Container) {
            Container container = (Container)c;
            container.doLayout();
            for (Component child : container.getComponents()) layoutDeep(child);
        }
    }
}
