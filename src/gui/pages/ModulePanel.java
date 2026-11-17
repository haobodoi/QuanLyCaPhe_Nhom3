package gui.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import gui.components.GlassPanel;
import gui.components.NeonButton;
import gui.navigation.MenuConfig;
import gui.theme.Theme;

/**
 * Khung giao dien theo doi tuong: thong tin, thanh cong cu va JTable rong.
 * CRUD se duoc gan vao JPanel cua thanh vien phu trach sau nay.
 */
public class ModulePanel extends JScrollPane {
    public ModulePanel(MenuConfig.Page page, Consumer<String> navigate) {
        JPanel root = new JPanel();
        root.setOpaque(false);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(26, 27, 27, 27));

        GlassPanel intro = new GlassPanel(24, accent(page));
        intro.setLayout(new BorderLayout());
        intro.setBorder(BorderFactory.createEmptyBorder(21, 25, 21, 25));
        JPanel headings = new JPanel(new GridLayout(3, 1, 0, 7));
        headings.setOpaque(false);
        headings.add(text("QUẢN LÝ QUÁN CÀ PHÊ", 12, Font.BOLD, accent(page)));
        headings.add(text(page.title, 29, Font.BOLD, Theme.TEXT));
        headings.add(text(page.subtitle, 15, Font.PLAIN, Theme.MUTED));
        intro.add(headings, BorderLayout.CENTER);
        fixed(root, intro, 165);
        root.add(Box.createVerticalStrut(15));

        GlassPanel toolbar = new GlassPanel(22, Theme.BORDER);
        toolbar.setLayout(new BorderLayout(12, 0));
        toolbar.setBorder(BorderFactory.createEmptyBorder(15, 19, 15, 19));
        JTextField search = new JTextField("Tìm kiếm " + page.title.toLowerCase() + "...");
        search.setFont(Theme.font(13, Font.PLAIN));
        search.setForeground(Theme.SOFT);
        search.setCaretColor(Theme.CYAN);
        search.setBackground(Theme.PANEL_LIGHT);
        search.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.alpha(Theme.BORDER, 160)),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)));
        search.setEditable(false);
        toolbar.add(search, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        action(actions, "Thêm", Theme.GREEN);
        action(actions, "Sửa", Theme.CYAN);
        action(actions, "Xóa", Theme.RED);
        toolbar.add(actions, BorderLayout.EAST);
        fixed(root, toolbar, 76);
        root.add(Box.createVerticalStrut(15));

        GlassPanel listCard = new GlassPanel(23, Theme.BORDER);
        listCard.setLayout(new BorderLayout(0, 13));
        listCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 18, 20));
        listCard.add(text("DANH SÁCH " + page.title.toUpperCase(), 15, Font.BOLD, Theme.TEXT),
                BorderLayout.NORTH);

        String[] columns = columnsFor(page.id);
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(Theme.font(13, Font.PLAIN));
        table.setForeground(Theme.TEXT);
        table.setBackground(Theme.PANEL_LIGHT);
        table.setSelectionBackground(Theme.alpha(Theme.CYAN, 45));
        table.setSelectionForeground(Theme.TEXT);
        table.setRowHeight(39);
        table.setShowVerticalLines(false);
        table.setGridColor(Theme.alpha(Theme.BORDER, 76));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(Theme.font(13, Font.BOLD));
        table.getTableHeader().setForeground(Theme.TEXT);
        table.getTableHeader().setBackground(Theme.BG_DEEP);
        table.getTableHeader().setPreferredSize(new Dimension(0, 42));

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setOpaque(false);
        tableScroll.getViewport().setBackground(Theme.alpha(Theme.PANEL_LIGHT, 235));
        tableScroll.setBorder(BorderFactory.createLineBorder(Theme.alpha(Theme.BORDER, 140)));
        listCard.add(tableScroll, BorderLayout.CENTER);
        JLabel empty = text("Chưa có kết quả hiển thị", 13, Font.PLAIN, Theme.MUTED);
        empty.setHorizontalAlignment(SwingConstants.CENTER);
        listCard.add(empty, BorderLayout.SOUTH);
        fixed(root, listCard, 310);
        root.add(Box.createVerticalStrut(15));

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        footer.setOpaque(false);
        NeonButton home = new NeonButton("  ←  Về trang chủ", Theme.CYAN);
        home.setPreferredSize(new Dimension(182, 43));
        home.addActionListener(e -> navigate.accept("home"));
        footer.add(home);
        fixed(root, footer, 47);

        setViewportView(root);
        setOpaque(false);
        getViewport().setOpaque(false);
        setBorder(null);
        getVerticalScrollBar().setUnitIncrement(19);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }

    private static String[] columnsFor(String pageId) {
        switch (pageId) {
            case "tai-khoan": return new String[]{"Mã tài khoản", "Tên đăng nhập", "Trạng thái"};
            case "nhan-vien": return new String[]{"Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Chức vụ"};
            case "khach-hang": return new String[]{"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Điểm tích lũy"};
            case "danh-muc": return new String[]{"Mã danh mục", "Tên danh mục"};
            case "mon": return new String[]{"Mã món", "Tên món", "Đơn giá", "Đang bán", "Danh mục"};
            case "khuyen-mai": return new String[]{"Mã khuyến mãi", "Tên khuyến mãi", "Thời gian", "Giá trị giảm"};
            case "ban": return new String[]{"Mã bàn", "Tên bàn", "Số chỗ", "Trạng thái"};
            case "phieu-dat-ban": return new String[]{"Mã phiếu", "Khách hàng", "Giờ đến", "Số khách", "Trạng thái"};
            case "hoa-don": return new String[]{"Mã hóa đơn", "Nhân viên", "Khách hàng", "Thời gian", "Trạng thái"};
            case "phuong-thuc-thanh-toan": return new String[]{"Mã thanh toán", "Mã hóa đơn", "Phương thức", "Số tiền"};
            default: return new String[]{"Chỉ tiêu", "Khoảng thời gian", "Giá trị"};
        }
    }

    private static void action(JPanel panel, String name, Color accent) {
        NeonButton button = new NeonButton(name, accent);
        button.setFont(Theme.font(13, Font.BOLD));
        button.setPreferredSize(new Dimension(76, 42));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setEnabled(false); // chưa gắn DAO: không tạo hành vi CRUD giả
        panel.add(button);
    }

    private static void fixed(JPanel root, JPanel panel, int height) {
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setPreferredSize(new Dimension(900, height));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        root.add(panel);
    }

    private static JLabel text(String value, int size, int style, Color color) {
        JLabel l = new JLabel(value);
        l.setFont(Theme.font(size, style));
        l.setForeground(color);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private static Color accent(MenuConfig.Page page) {
        if (page.owner.equals("Người 1")) return Theme.CYAN;
        if (page.owner.equals("Người 2")) return Theme.PURPLE;
        return Theme.GREEN;
    }
}
