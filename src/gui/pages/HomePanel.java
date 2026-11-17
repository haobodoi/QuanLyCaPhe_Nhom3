package gui.pages;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import gui.components.GlassPanel;
import gui.components.NeonButton;
import gui.theme.CoffeeBackground;
import gui.theme.Theme;

/** Trang chu sang, tap trung dieu huong; khong hien thi so lieu gia lap. */
public class HomePanel extends JScrollPane {
    public HomePanel(Consumer<String> navigate) {
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(22, 25, 25, 25));

        block(body, makeHero(navigate), 195);
        body.add(Box.createVerticalStrut(17));
        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);
        section.add(label("KHÁM PHÁ KHÔNG GIAN QUẢN LÝ", 14, Font.BOLD, Theme.TEXT), BorderLayout.WEST);
        section.add(label("CHỌN MỘT KHU VỰC ĐỂ BẮT ĐẦU", 11, Font.BOLD, Theme.CYAN), BorderLayout.EAST);
        block(body, section, 26);
        body.add(Box.createVerticalStrut(10));

        JPanel features = new JPanel(new GridLayout(1, 4, 13, 0));
        features.setOpaque(false);
        features.add(feature("NHÂN SỰ", "Nhân viên & khách hàng", "nhan-vien", Theme.CYAN, navigate));
        features.add(feature("THỰC ĐƠN", "Món, danh mục và ưu đãi", "mon", Theme.PURPLE, navigate));
        features.add(feature("BÀN & ĐẶT BÀN", "Quản lý bàn và lịch đặt", "phieu-dat-ban", Theme.GREEN, navigate));
        features.add(feature("HÓA ĐƠN", "Theo dõi hóa đơn, thanh toán", "hoa-don", Theme.GOLD, navigate));
        block(body, features, 139);
        body.add(Box.createVerticalStrut(16));

        JPanel lower = new JPanel(new GridLayout(1, 2, 15, 0));
        lower.setOpaque(false);
        lower.add(makeShortcuts(navigate));
        lower.add(makeWorkflow());
        block(body, lower, 249);

        setViewportView(body);
        setOpaque(false);
        getViewport().setOpaque(false);
        setBorder(null);
        getVerticalScrollBar().setUnitIncrement(18);
        getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }

    private static void block(JPanel root, JPanel panel, int height) {
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setPreferredSize(new Dimension(1000, height));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        panel.setMinimumSize(new Dimension(0, height));
        root.add(panel);
    }

    private static JLabel label(String text, int size, int style, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.font(size, style));
        l.setForeground(color);
        return l;
    }

    private static JPanel makeHero(Consumer<String> navigate) {
        GlassPanel hero = new GlassPanel(28, Theme.CYAN);
        hero.setLayout(new BorderLayout(15, 0));
        hero.setBorder(BorderFactory.createEmptyBorder(14, 24, 12, 15));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel kicker = label("C A F E   M A N A G E R", 12, Font.BOLD, Theme.CYAN);
        kicker.setAlignmentX(LEFT_ALIGNMENT);
        left.add(kicker);
        left.add(Box.createVerticalStrut(8));
        JLabel title = label("Chào mừng đến quán cà phê!", 27, Font.BOLD, Theme.TEXT);
        title.setAlignmentX(LEFT_ALIGNMENT);
        left.add(title);
        left.add(Box.createVerticalStrut(6));
        JLabel caption1 = label("Quản lý bàn, thực đơn, khách hàng và hóa đơn trong một nơi.",
                                14, Font.PLAIN, Theme.MUTED);
        caption1.setAlignmentX(LEFT_ALIGNMENT);
        left.add(caption1);
        left.add(Box.createVerticalStrut(3));
        JLabel caption2 = label("Một không gian làm việc gọn gàng, trực quan và dễ sử dụng.",
                                13, Font.PLAIN, Theme.SOFT);
        caption2.setAlignmentX(LEFT_ALIGNMENT);
        left.add(caption2);
        left.add(Box.createVerticalStrut(12));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(LEFT_ALIGNMENT);
        NeonButton tables = new NeonButton("  Quản lý bàn   →", Theme.CYAN);
        tables.setPreferredSize(new Dimension(184, 37));
        tables.setFont(Theme.font(13, Font.BOLD));
        tables.addActionListener(e -> navigate.accept("ban"));
        NeonButton invoices = new NeonButton("  Xem hóa đơn   →", Theme.PURPLE);
        invoices.setPreferredSize(new Dimension(167, 37));
        invoices.setFont(Theme.font(13, Font.BOLD));
        invoices.addActionListener(e -> navigate.accept("hoa-don"));
        actions.add(tables);
        actions.add(invoices);
        left.add(actions);
        hero.add(left, BorderLayout.CENTER);

        JPanel photo = new CoffeePhotoWindow();
        photo.setPreferredSize(new Dimension(219, 161));
        hero.add(photo, BorderLayout.EAST);
        return hero;
    }

    private static GlassPanel feature(String title, String subtitle, String key,
                                      Color accent, Consumer<String> navigate) {
        GlassPanel card = new GlassPanel(23, accent);
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(BorderFactory.createEmptyBorder(15, 17, 14, 15));
        JPanel copy = new JPanel(new GridLayout(2, 1, 0, 5));
        copy.setOpaque(false);
        copy.add(label(title, 14, Font.BOLD, Theme.TEXT));
        copy.add(label(subtitle, 12, Font.PLAIN, Theme.MUTED));
        card.add(copy, BorderLayout.CENTER);
        NeonButton open = new NeonButton("   Mở quản lý   →", accent);
        open.setFont(Theme.font(12, Font.BOLD));
        open.setPreferredSize(new Dimension(100, 35));
        open.addActionListener(e -> navigate.accept(key));
        card.add(open, BorderLayout.SOUTH);
        return card;
    }

    private static JPanel makeShortcuts(Consumer<String> navigate) {
        GlassPanel card = new GlassPanel(23, Theme.BORDER);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 15, 18));
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setOpaque(false);
        header.add(label("TRUY CẬP NHANH", 15, Font.BOLD, Theme.TEXT));
        header.add(label("Những khu vực thường sử dụng", 12, Font.PLAIN, Theme.MUTED));
        card.add(header, BorderLayout.NORTH);
        JPanel buttons = new JPanel(new GridLayout(2, 2, 10, 10));
        buttons.setOpaque(false);
        shortcut(buttons, "  Bàn & đặt bàn  ↗", "ban", Theme.CYAN, navigate);
        shortcut(buttons, "  Hóa đơn  ↗", "hoa-don", Theme.PURPLE, navigate);
        shortcut(buttons, "  Khách hàng  ↗", "khach-hang", Theme.GREEN, navigate);
        shortcut(buttons, "  Thống kê  ↗", "thong-ke-tong-quan", Theme.GOLD, navigate);
        card.add(buttons, BorderLayout.CENTER);
        return card;
    }

    private static void shortcut(JPanel container, String title, String key,
                                 Color color, Consumer<String> navigate) {
        NeonButton button = new NeonButton(title, color);
        button.setFont(Theme.font(13, Font.BOLD));
        button.addActionListener(e -> navigate.accept(key));
        container.add(button);
    }

    private static JPanel makeWorkflow() {
        GlassPanel card = new GlassPanel(23, Theme.GOLD);
        card.setLayout(new BorderLayout(0, 9));
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 17, 18));
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setOpaque(false);
        header.add(label("HOẠT ĐỘNG TẠI QUÁN", 15, Font.BOLD, Theme.TEXT));
        header.add(label("Quy trình phục vụ đơn giản", 12, Font.PLAIN, Theme.MUTED));
        card.add(header, BorderLayout.NORTH);
        JPanel list = new JPanel(new GridLayout(3, 1, 0, 5));
        list.setOpaque(false);
        list.add(workflowRow("01", "Tiếp đón & đặt bàn", "Sắp xếp bàn phù hợp cho khách", Theme.CYAN));
        list.add(workflowRow("02", "Gọi món & lập hóa đơn", "Ghi nhận món và thời gian sử dụng bàn", Theme.PURPLE));
        list.add(workflowRow("03", "Thanh toán", "Hoàn tất hóa đơn và cập nhật bàn", Theme.GREEN));
        card.add(list, BorderLayout.CENTER);
        return card;
    }

    private static JPanel workflowRow(String number, String title, String desc, Color accent) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        JLabel badge = label(number, 20, Font.BOLD, accent);
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setPreferredSize(new Dimension(37, 44));
        row.add(badge, BorderLayout.WEST);
        JPanel text = new JPanel(new GridLayout(2, 1, 0, 1));
        text.setOpaque(false);
        text.add(label(title, 13, Font.BOLD, Theme.TEXT));
        text.add(label(desc, 12, Font.PLAIN, Theme.MUTED));
        row.add(text, BorderLayout.CENTER);
        return row;
    }

    /** Anh that do nguoi dung cung cap, hien thi voi goc bo tron. */
    private static final class CoffeePhotoWindow extends JPanel {
        private CoffeePhotoWindow() { setOpaque(false); }

        @Override protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D)graphics.create();
            Theme.quality(g);
            int w = getWidth() - 1, h = getHeight() - 1;
            RoundRectangle2D shape = new RoundRectangle2D.Float(0, 0, w, h, 25, 25);
            g.setClip(shape);
            CoffeeBackground.drawCupWindow(g, w, h);
            g.setColor(Theme.alpha(Theme.PANEL_LIGHT, 31));
            g.fill(shape);
            g.setClip(null);
            g.setStroke(new BasicStroke(1.3f));
            g.setColor(Theme.alpha(Theme.GOLD, 155));
            g.draw(shape);
            g.dispose();
        }
    }
}
