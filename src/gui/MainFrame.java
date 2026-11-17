package gui;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JFrame;

/** Mot JFrame duy nhat cua ung dung; noi dung thay doi bang CardLayout. */
public class MainFrame extends JFrame {
    private final MainView view;

    public MainFrame() {
        super("Cafe Manager | Quản lý quán cà phê");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view = new MainView();
        setContentPane(view);
        setMinimumSize(new Dimension(1120, 675));
        setSize(new Dimension(1320, 800));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void registerModule(String pageId, JComponent page) {
        view.registerModule(pageId, page);
    }

    public void showPage(String pageId) {
        view.showPage(pageId);
    }
}
