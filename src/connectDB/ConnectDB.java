package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Ket noi SQL Server cho cac DAO sau nay.
 * Trang chu khong goi class nay; co the chay ma KHONG CAN SQL Server/JDBC jar.
 *
 * Dat cac bien moi truong khi bat dau ket noi that:
 *   CAFE_DB_URL  = jdbc:sqlserver://localhost:1433;databaseName=QuanLyQuanCafe_Final;encrypt=true;trustServerCertificate=true
 *   CAFE_DB_USER = sa
 *   CAFE_DB_PASS = mat_khau_tren_may_cua_ban
 */
public final class ConnectDB {
    private ConnectDB() { }

    public static Connection open() throws SQLException {
        String url = System.getenv("CAFE_DB_URL");
        String user = System.getenv("CAFE_DB_USER");
        String pass = System.getenv("CAFE_DB_PASS");
        if (url == null || url.trim().isEmpty()) {
            throw new SQLException("Chua thiet lap CAFE_DB_URL. Trang chu van chay offline binh thuong.");
        }
        if (user == null || user.trim().isEmpty()) {
            return DriverManager.getConnection(url); // Windows Authentication neu driver duoc cau hinh
        }
        return DriverManager.getConnection(url, user, pass == null ? "" : pass);
    }
}
