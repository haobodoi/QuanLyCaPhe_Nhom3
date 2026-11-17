package gui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import gui.theme.Theme;

/**
 * Nut dieu huong: hover phat sang, vien neon va hai tia sang di quanh vien.
 * Timer chi chay khi nut dang duoc chon hoac con chuot nam tren nut.
 */
public class NeonButton extends JButton {
    private final Color accent;
    private boolean selectedState;
    private boolean hovered;
    private float progress;
    private float glowProgress;
    private final Timer timer;

    public NeonButton(String text, Color accent) {
        super(text);
        this.accent = accent;
        setFont(Theme.font(14, Font.BOLD));
        setForeground(Theme.TEXT);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 17, 0, 12));

        timer = new Timer(42, e -> {
            progress = (progress + 0.011f) % 1.0f;
            float target = (hovered || selectedState) ? 1f : 0f;
            glowProgress += (target - glowProgress) * .13f;
            if (Math.abs(target - glowProgress) < .015f) glowProgress = target;
            if (target == 0f && glowProgress == 0f) ((Timer)e.getSource()).stop();
            repaint();
        });
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                hovered = true;
                updateAnimation();
                repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                hovered = false;
                updateAnimation();
                repaint();
            }
        });
    }

    public void setSelectedState(boolean selected) {
        selectedState = selected;
        updateAnimation();
        repaint();
    }

    public boolean isSelectedState() {
        return selectedState;
    }

    private void updateAnimation() {
        if (selectedState || hovered || glowProgress > 0f) {
            if (!timer.isRunning()) timer.start();
        } else {
            timer.stop();
        }
    }

    @Override
    public void removeNotify() {
        timer.stop();
        super.removeNotify();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        Theme.quality(g);
        int w = getWidth() - 1;
        int h = getHeight() - 1;
        if (w <= 2 || h <= 2) {
            g.dispose();
            return;
        }

        RoundRectangle2D border = new RoundRectangle2D.Float(1, 1, w - 2, h - 2, 17, 17);
        g.setColor(Theme.alpha(Theme.PANEL_LIGHT, 70));
        g.fill(border);

        if (glowProgress > .01f) {
            java.awt.Shape oldClip = g.getClip();
            g.clip(border);
            float spread = w * glowProgress;
            float x = (w - spread) / 2f;
            g.setPaint(new GradientPaint(x, 0, Theme.alpha(accent, 66),
                    x + spread, h, Theme.alpha(Theme.PURPLE, 53)));
            g.fill(new RoundRectangle2D.Float(x, 1, spread, h - 2, 17, 17));
            g.setClip(oldClip);
        }

        if (selectedState || hovered || glowProgress > 0f) {
            g.setStroke(new BasicStroke(1.3f));
            g.setColor(Theme.alpha(accent, (int)(85 + glowProgress * 90)));
            g.draw(border);
            if (glowProgress > .20f) {
                drawMovingTrail(g, border, progress, accent);
                drawMovingTrail(g, border, (progress + .50f) % 1.0f, Theme.PURPLE);
            }
        } else {
            g.setStroke(new BasicStroke(1f));
            g.setColor(Theme.alpha(Theme.BORDER, 75));
            g.draw(border);
        }

        g.dispose();
        super.paintComponent(graphics);
    }

    private static void drawMovingTrail(Graphics2D g, RoundRectangle2D shape,
                                        float offset, Color color) {
        List<Point2D.Float> path = flatten(shape);
        if (path.size() < 3) return;
        float total = pathLength(path);
        float trail = Math.min(105f, total * .16f);
        for (int i = 0; i < 20; i++) {
            float begin = (offset * total + trail * i / 20f) % total;
            float end = (offset * total + trail * (i + 1) / 20f) % total;
            Point2D.Float p1 = sample(path, begin);
            Point2D.Float p2 = sample(path, end);
            float brightness = (float) Math.sin((i + 1) * Math.PI / 21.0);
            if (p1.distance(p2) > 22) continue; // doan qua diem dong path
            g.setStroke(new BasicStroke(5.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(Theme.alpha(color, (int)(53 * brightness)));
            g.drawLine(Math.round(p1.x), Math.round(p1.y), Math.round(p2.x), Math.round(p2.y));
            g.setStroke(new BasicStroke(2.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(Theme.alpha(color, (int)(222 * brightness)));
            g.drawLine(Math.round(p1.x), Math.round(p1.y), Math.round(p2.x), Math.round(p2.y));
        }
    }

    private static List<Point2D.Float> flatten(RoundRectangle2D shape) {
        List<Point2D.Float> points = new ArrayList<>();
        PathIterator it = shape.getPathIterator(null, 1.1);
        double[] coords = new double[6];
        while (!it.isDone()) {
            int type = it.currentSegment(coords);
            if (type == PathIterator.SEG_MOVETO || type == PathIterator.SEG_LINETO) {
                points.add(new Point2D.Float((float)coords[0], (float)coords[1]));
            }
            it.next();
        }
        if (!points.isEmpty()) points.add(new Point2D.Float(points.get(0).x, points.get(0).y));
        return points;
    }

    private static float pathLength(List<Point2D.Float> path) {
        float sum = 0;
        for (int i = 1; i < path.size(); i++) sum += path.get(i - 1).distance(path.get(i));
        return sum;
    }

    private static Point2D.Float sample(List<Point2D.Float> path, float target) {
        float traversed = 0;
        for (int i = 1; i < path.size(); i++) {
            Point2D.Float a = path.get(i - 1), b = path.get(i);
            float dist = (float)a.distance(b);
            if (traversed + dist >= target) {
                float t = dist <= 0 ? 0 : (target - traversed) / dist;
                return new Point2D.Float(a.x + (b.x - a.x) * t,
                                         a.y + (b.y - a.y) * t);
            }
            traversed += dist;
        }
        return path.get(path.size() - 1);
    }
}
