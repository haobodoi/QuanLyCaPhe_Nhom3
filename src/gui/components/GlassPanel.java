package gui.components;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

import gui.theme.Theme;

/** Hieu ung mat kinh toi, khong can thu vien ngoai. */
public class GlassPanel extends JPanel {
    private final int arc;
    private Color accent;

    public GlassPanel() {
        this(24, Theme.BORDER);
    }

    public GlassPanel(int arc, Color accent) {
        this.arc = arc;
        this.accent = accent;
        setOpaque(false);
    }

    public void setAccent(Color accent) {
        this.accent = accent;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        Theme.quality(g);
        int w = getWidth() - 1;
        int h = getHeight() - 1;
        if (w <= 1 || h <= 1) {
            g.dispose();
            return;
        }
        RoundRectangle2D shape = new RoundRectangle2D.Float(.5f, .5f, w, h, arc, arc);
        g.setPaint(new GradientPaint(0, 0, Theme.alpha(Theme.PANEL_LIGHT, 226),
                w, h, Theme.alpha(Theme.PANEL, 231)));
        g.fill(shape);
        g.setColor(Theme.alpha(accent, 102));
        g.draw(shape);
        g.setColor(Theme.alpha(Color.WHITE, 155));
        g.draw(new RoundRectangle2D.Float(2.5f, 2.5f, w - 4, h - 4, arc - 3, arc - 3));
        g.dispose();
        super.paintComponent(graphics);
    }
}
