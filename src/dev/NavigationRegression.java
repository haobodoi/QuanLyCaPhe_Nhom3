package dev;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import gui.MainView;
import gui.navigation.MenuConfig;
import gui.theme.Theme;

/** Kiem tra loi bong trang cu khi chuyen nhieu muc tren CardLayout. */
public final class NavigationRegression {
    private static final int WIDTH = 1366;
    private static final int HEIGHT = 768;

    private NavigationRegression() { }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Theme.installDefaults();
            MainView view = new MainView();
            view.setSize(WIDTH, HEIGHT);
            RepaintManager.currentManager(view).setDoubleBufferingEnabled(false);
            layoutDeep(view);

            BufferedImage reused = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            render(view, reused);
            List<String> routes = new ArrayList<>(MenuConfig.pages().keySet());
            routes.add("home");
            routes.add("nhan-vien");
            routes.add("danh-muc");
            routes.add("home");

            for (String route : routes) {
                view.showPage(route);
                layoutDeep(view);
                render(view, reused);

                // Ban ve sach phai giong ban ve tren bo dem cua trang truoc.
                BufferedImage clean = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                render(view, clean);
                int[] a = ((DataBufferInt) reused.getRaster().getDataBuffer()).getData();
                int[] b = ((DataBufferInt) clean.getRaster().getDataBuffer()).getData();
                if (!Arrays.equals(a, b)) {
                    throw new AssertionError("Loi bong giao dien khi chuyen den: " + route);
                }
            }
            System.out.println("PASS: " + routes.size() + " lan chuyen trang khong luu hinh cu.");
        });
        System.exit(0);
    }

    private static void render(MainView view, BufferedImage image) {
        Graphics2D g = image.createGraphics();
        view.printAll(g);
        g.dispose();
    }

    private static void layoutDeep(Component component) {
        if (component instanceof Container) {
            Container container = (Container) component;
            container.doLayout();
            for (Component child : container.getComponents()) layoutDeep(child);
        }
    }
}
