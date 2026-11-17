	package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import gui.MainFrame;
import gui.theme.Theme;

/** Chay class nay de mo trang chu. Khong can SQL Server de xem giao dien. */
public final class Main {
    private Main() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Java Swing mac dinh van hoat dong.
            }
            Theme.installDefaults();
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
