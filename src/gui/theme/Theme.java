package gui.theme;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.UIManager;

/** Bang mau va typography chung. Cac thanh vien nen dung chung file nay. */
public final class Theme {
    // Bang mau sang: kem, ca phe sua, chu nau dam va diem nhan xanh ngoc.
    public static final Color BG = new Color(248, 244, 237);
    public static final Color BG_DEEP = new Color(239, 229, 217);
    public static final Color SIDEBAR = new Color(255, 250, 243);
    public static final Color PANEL = new Color(255, 251, 245);
    public static final Color PANEL_LIGHT = new Color(255, 255, 253);
    public static final Color BORDER = new Color(190, 173, 153);
    public static final Color TEXT = new Color(47, 37, 30);
    public static final Color MUTED = new Color(97, 81, 70);
    public static final Color SOFT = new Color(116, 101, 88);
    public static final Color CYAN = new Color(20, 122, 133);
    public static final Color PURPLE = new Color(119, 84, 149);
    public static final Color GREEN = new Color(33, 121, 84);
    public static final Color GOLD = new Color(169, 104, 41);
    public static final Color RED = new Color(188, 68, 70);

    private static final String FONT_FAMILY = "SansSerif";

    private Theme() { }

    public static Font font(int size, int style) {
        return new Font(FONT_FAMILY, style, size);
    }

    public static Color alpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void quality(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                           RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                           RenderingHints.VALUE_RENDER_QUALITY);
    }

    public static void installDefaults() {
        UIManager.put("Label.font", font(14, Font.PLAIN));
        UIManager.put("Button.font", font(14, Font.BOLD));
        UIManager.put("OptionPane.messageFont", font(14, Font.PLAIN));
        UIManager.put("OptionPane.buttonFont", font(14, Font.BOLD));
        UIManager.put("Table.font", font(14, Font.PLAIN));
        UIManager.put("TableHeader.font", font(14, Font.BOLD));
        UIManager.put("Table.rowHeight", 35);
    }
}
