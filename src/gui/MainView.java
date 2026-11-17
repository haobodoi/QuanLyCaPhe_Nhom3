package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import gui.components.NeonButton;
import gui.navigation.MenuConfig;
import gui.pages.HomePanel;
import gui.pages.ModulePanel;
import gui.theme.CoffeeBackground;
import gui.theme.Theme;

/**
 * Main content cua ung dung, dung JPanel de co the test/render khi khong co man hinh.
 * Menu theo doi tuong; cac muc con dang duoc chuyen huong bang CardLayout.
 */
public class MainView extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    // CardLayout bat buoc phai co lop nen ve kin khung. Neu cards trong suot,
    // JScrollPane cua tung module se de lai noi dung trang cu sau khi chuyen menu.
    private final JPanel cards = new JPanel(cardLayout) {
        @Override protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            int fullHeight = getParent() == null ? getHeight() : getParent().getHeight();
            int offsetY = getParent() == null ? 0 : getY();
            g.translate(0, -offsetY);
            paintRightBackground(g, getWidth(), fullHeight);
            g.dispose();
        }
    };
    private final Map<String, JComponent> registered = new LinkedHashMap<>();
    private final Map<String, NeonButton> menuButtons = new LinkedHashMap<>();
    private final Map<String, JPanel> groupChildren = new LinkedHashMap<>();
    private final Map<String, JButton> groupHeaders = new LinkedHashMap<>();
    private String currentPage = "home";
    private NeonButton homeButton;
    private JLabel breadcrumb;
    private JLabel heading;
    private JLabel clock;
    private JTextField search;

    public MainView() {
        setBackground(Theme.BG);
        setOpaque(true);
        cards.setBackground(Theme.BG);
        cards.setOpaque(true);
        setLayout(new BorderLayout());
        add(createSidebar(), BorderLayout.WEST);
        add(createRightArea(), BorderLayout.CENTER);

        registerModule("home", new HomePanel(this::showPage));
        for (MenuConfig.Page page : MenuConfig.pages().values()) {
            registerModule(page.id, new ModulePanel(page, this::showPage));
        }
        showPage("home");
    }

    /**
     * DIEM TICH HOP: module cua thanh vien phai la JPanel/JComponent.
     * Vi du: frame.registerModule("nhan-vien", new NhanVienGUI());
     */
    public void registerModule(String pageId, JComponent component) {
        if (!"home".equals(pageId) && MenuConfig.byId(pageId) == null) {
            throw new IllegalArgumentException("Menu ID chua duoc khai bao: " + pageId);
        }
        JComponent previous = registered.put(pageId, component);
        if (previous != null) cards.remove(previous);
        cards.add(component, pageId);
        cards.revalidate();
        if (pageId.equals(currentPage)) cardLayout.show(cards, pageId);
    }

    public void showPage(String id) {
        if (!"home".equals(id) && MenuConfig.byId(id) == null) return;
        currentPage = id;
        cardLayout.show(cards, id);
        homeButton.setSelectedState("home".equals(id));
        for (Map.Entry<String, NeonButton> e : menuButtons.entrySet()) {
            e.getValue().setSelectedState(e.getKey().equals(id));
        }
        if ("home".equals(id)) {
            heading.setText("Trang chủ");
            breadcrumb.setText("KHÔNG GIAN QUẢN TRỊ   /   TỔNG QUAN");
        } else {
            MenuConfig.Page page = MenuConfig.byId(id);
            heading.setText(page.title);
            breadcrumb.setText("QUẢN LÝ ĐỐI TƯỢNG   /   " + page.title.toUpperCase());
            expandGroupContaining(id);
        }
        cards.revalidate();
        cards.repaint();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(Theme.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(268, 760));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1,
                Theme.alpha(Theme.BORDER, 100)));

        JPanel branding = new JPanel(new BorderLayout(12, 0));
        branding.setOpaque(false);
        branding.setBorder(BorderFactory.createEmptyBorder(24, 20, 18, 12));
        JLabel logo = new JLabel("C", SwingConstants.CENTER);
        logo.setFont(Theme.font(24, Font.BOLD));
        logo.setForeground(Theme.BG_DEEP);
        logo.setOpaque(true);
        logo.setBackground(Theme.CYAN);
        logo.setPreferredSize(new Dimension(44, 44));
        branding.add(logo, BorderLayout.WEST);
        JPanel logoText = new JPanel(new GridLayout(2, 1, 0, 2));
        logoText.setOpaque(false);
        logoText.add(label("CAFE MANAGER", 16, Font.BOLD, Theme.TEXT));
        logoText.add(label("QUẢN LÝ QUÁN CÀ PHÊ", 10, Font.BOLD, Theme.CYAN));
        branding.add(logoText, BorderLayout.CENTER);
        sidebar.add(branding, BorderLayout.NORTH);

        JPanel links = new JPanel();
        links.setOpaque(false);
        links.setLayout(new BoxLayout(links, BoxLayout.Y_AXIS));
        links.setBorder(BorderFactory.createEmptyBorder(9, 14, 18, 13));

        JLabel overview = label("KHÔNG GIAN LÀM VIỆC", 11, Font.BOLD, Theme.SOFT);
        overview.setBorder(BorderFactory.createEmptyBorder(0, 11, 9, 0));
        overview.setAlignmentX(LEFT_ALIGNMENT);
        links.add(overview);

        homeButton = new NeonButton("  TRANG CHỦ", Theme.CYAN);
        homeButton.setFont(Theme.font(14, Font.BOLD));
        homeButton.setAlignmentX(LEFT_ALIGNMENT);
        homeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        homeButton.setPreferredSize(new Dimension(240, 45));
        homeButton.addActionListener(e -> showPage("home"));
        links.add(homeButton);
        links.add(Box.createVerticalStrut(16));

        JLabel modules = label("CÁC NHÓM ĐỐI TƯỢNG", 11, Font.BOLD, Theme.SOFT);
        modules.setBorder(BorderFactory.createEmptyBorder(0, 11, 6, 0));
        modules.setAlignmentX(LEFT_ALIGNMENT);
        links.add(modules);

        for (MenuConfig.Group group : MenuConfig.groups()) {
            JPanel wrapper = new JPanel();
            wrapper.setOpaque(false);
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.setAlignmentX(LEFT_ALIGNMENT);

            JButton header = new JButton(group.shortCode + "   " + group.title + "   ▾");
            header.setHorizontalAlignment(SwingConstants.LEFT);
            header.setBorder(BorderFactory.createEmptyBorder(5, 12, 4, 4));
            header.setContentAreaFilled(false);
            header.setFocusPainted(false);
            header.setOpaque(false);
            header.setForeground(Theme.MUTED);
            header.setFont(Theme.font(11, Font.BOLD));
            header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
            header.setPreferredSize(new Dimension(239, 43));
            header.setAlignmentX(LEFT_ALIGNMENT);
            header.addActionListener(e -> toggleGroup(group.title));
            header.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { header.setForeground(Theme.CYAN); }
                @Override public void mouseExited(MouseEvent e) {
                    header.setForeground(Theme.MUTED);
                }
            });
            wrapper.add(header);
            groupHeaders.put(group.title, header);

            JPanel children = new JPanel();
            children.setOpaque(false);
            children.setLayout(new BoxLayout(children, BoxLayout.Y_AXIS));
            children.setBorder(BorderFactory.createEmptyBorder(0, 8, 7, 0));
            children.setAlignmentX(LEFT_ALIGNMENT);
            for (MenuConfig.Page page : group.pages) {
                NeonButton item = new NeonButton("   " + page.title, colorFor(page.owner));
                item.setFont(Theme.font(13, Font.PLAIN));
                item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 41));
                item.setPreferredSize(new Dimension(225, 41));
                item.setAlignmentX(LEFT_ALIGNMENT);
                item.addActionListener(e -> showPage(page.id));
                item.setToolTipText(page.subtitle + " · " + page.owner);
                children.add(item);
                children.add(Box.createVerticalStrut(3));
                menuButtons.put(page.id, item);
            }
            children.setVisible(group == MenuConfig.groups().get(0));
            wrapper.add(children);
            groupChildren.put(group.title, children);
            links.add(wrapper);
        }
        links.add(Box.createVerticalGlue());

        JScrollPane sideScroll = new JScrollPane(links);
        sideScroll.setBorder(null);
        sideScroll.setOpaque(false);
        sideScroll.getViewport().setOpaque(false);
        sideScroll.getVerticalScrollBar().setUnitIncrement(14);
        sideScroll.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        sideScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebar.add(sideScroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(2, 1, 0, 3));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 24, 17, 11));
        bottom.add(label("CAFE MANAGER", 11, Font.BOLD, Theme.CYAN));
        bottom.add(label("Quản lý quán cà phê", 11, Font.PLAIN, Theme.SOFT));
        sidebar.add(bottom, BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel createRightArea() {
        JPanel area = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics graphics) {
                // Vung noi dung phai ve lai NEN DAY DU moi lan chuyen CardLayout.
                // Neu chi phu lop mau trong suot len khung hinh cu, Swing se
                // de lai chu va the cua trang truoc tren man hinh (ghosting).
                super.paintComponent(graphics);
                Graphics2D g = (Graphics2D) graphics.create();
                paintRightBackground(g, getWidth(), getHeight());
                g.dispose();
            }
        };
        area.setOpaque(true);
        area.setBackground(Theme.BG);
        area.add(createTopbar(), BorderLayout.NORTH);
        area.add(cards, BorderLayout.CENTER);
        return area;
    }

    /** Nen anh duoc ve tu dau (khong chong alpha tren khung hinh truoc). */
    private static void paintRightBackground(Graphics2D g, int width, int height) {
        if (width <= 0 || height <= 0) return;
        Theme.quality(g);
        CoffeeBackground.drawCover(g, width, height);
        g.setPaint(new GradientPaint(0, 0, Theme.alpha(Theme.PANEL, 178),
                width, height, Theme.alpha(Theme.BG, 183)));
        g.fillRect(0, 0, width, height);
    }

    private JPanel createTopbar() {
        JPanel top = new JPanel(new BorderLayout(20, 0)) {
            @Override protected void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D)graphics.create();
                g.setColor(Theme.alpha(Theme.PANEL, 228));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.dispose();
                super.paintComponent(graphics);
            }
        };
        top.setOpaque(false);
        top.setPreferredSize(new Dimension(900, 83));
        top.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.alpha(Theme.BORDER, 70)),
                BorderFactory.createEmptyBorder(12, 25, 12, 23)));

        JPanel names = new JPanel(new GridLayout(2, 1, 0, 4));
        names.setOpaque(false);
        breadcrumb = label("KHÔNG GIAN QUẢN TRỊ / TỔNG QUAN", 11, Font.BOLD, Theme.SOFT);
        heading = label("Trang chủ", 24, Font.BOLD, Theme.TEXT);
        names.add(breadcrumb);
        names.add(heading);
        top.add(names, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        right.setOpaque(false);
        search = new JTextField("Tìm module nhanh...");
        search.setPreferredSize(new Dimension(200, 36));
        search.setFont(Theme.font(13, Font.PLAIN));
        search.setForeground(Theme.SOFT);
        search.setCaretColor(Theme.CYAN);
        search.setBackground(Theme.PANEL);
        search.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.alpha(Theme.BORDER, 125)),
                BorderFactory.createEmptyBorder(0, 11, 0, 8)));
        search.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (search.getText().equals("Tìm module nhanh...")) {
                    search.setText("");
                    search.setForeground(Theme.TEXT);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (search.getText().trim().isEmpty()) {
                    search.setText("Tìm module nhanh...");
                    search.setForeground(Theme.SOFT);
                }
            }
        });
        search.addActionListener(e -> searchMenu());
        right.add(search);

        JPanel time = new JPanel(new GridLayout(2, 1, 0, 3));
        time.setOpaque(false);
        time.setPreferredSize(new Dimension(100, 36));
        clock = label("", 12, Font.BOLD, Theme.TEXT);
        time.add(clock);
        time.add(label("GIỜ VIỆT NAM", 10, Font.BOLD, Theme.SOFT));
        updateClock();
        new Timer(60_000, e -> updateClock()).start();
        right.add(time);

        NeonButton profile = new NeonButton("Tài khoản  ▾", Theme.CYAN);
        profile.setFont(Theme.font(13, Font.BOLD));
        profile.setHorizontalAlignment(SwingConstants.CENTER);
        profile.setPreferredSize(new Dimension(134, 36));
        profile.addActionListener(e -> showPage("tai-khoan"));
        right.add(profile);
        top.add(right, BorderLayout.EAST);
        return top;
    }

    private void searchMenu() {
        String value = search.getText().trim().toLowerCase(Locale.forLanguageTag("vi-VN"));
        if (value.isEmpty() || value.equals("tìm module nhanh...")) return;
        for (MenuConfig.Page page : MenuConfig.pages().values()) {
            if (page.title.toLowerCase(Locale.forLanguageTag("vi-VN")).contains(value)
                    || page.id.contains(value)) {
                showPage(page.id);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Không tìm thấy module: " + search.getText(),
                "Tìm kiếm", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateClock() {
        clock.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm · dd/MM")));
    }

    private void toggleGroup(String title) {
        JPanel selected = groupChildren.get(title);
        boolean open = selected != null && !selected.isVisible();
        for (JPanel panel : groupChildren.values()) panel.setVisible(false);
        if (selected != null) selected.setVisible(open);
        revalidate();
        repaint();
    }

    private void expandGroupContaining(String pageId) {
        for (MenuConfig.Group group : MenuConfig.groups()) {
            for (MenuConfig.Page p : group.pages) {
                if (p.id.equals(pageId)) {
                    for (Map.Entry<String, JPanel> e : groupChildren.entrySet()) {
                        e.getValue().setVisible(e.getKey().equals(group.title));
                    }
                    revalidate();
                    return;
                }
            }
        }
    }

    private static Color colorFor(String owner) {
        if (owner.equals("Người 1")) return Theme.CYAN;
        if (owner.equals("Người 2")) return Theme.PURPLE;
        return Theme.GREEN;
    }

    private static JLabel label(String text, int size, int style, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.font(size, style));
        lbl.setForeground(color);
        return lbl;
    }
}
