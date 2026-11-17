# Cafe Manager — Trang chủ Java Swing (Eclipse)

Project khởi đầu cho bài tập lớn **quản lý quán cà phê theo hướng đối tượng**.
Ở giai đoạn này đã có **MainGUI chạy được, menu nhóm và menu con, chuyển trang,
tìm kiếm menu và giao diện sáng tông cà phê**. Ảnh ly cà phê nằm trong
`src/resources/coffee-background.png` được dùng làm hình nền và hình trang chủ.
Các màn hình quản lý đã có bố cục bảng dữ liệu nhưng **chưa phải CRUD hoàn chỉnh**.

## 1. Chạy trực tiếp trong Eclipse

1. Giải nén file ZIP.
2. Trong Eclipse chọn **File → Import → General → Existing Projects into Workspace**.
3. Chọn **Select root directory** và trỏ đến thư mục `QuanLyQuanCafe_HomeUI` vừa giải nén,
   hoặc dùng **Select archive file** để chọn ZIP trực tiếp.
4. Nhấn **Finish**.
5. Mở `src/app/Main.java` → chuột phải → **Run As → Java Application**.

Sử dụng Java **11 trở lên**. **Không cần kết nối SQL Server hay tải thư viện ngoài** để mở trang chủ.
Trên máy có màn hình nhỏ, giao diện mở tối đa màn hình và chỉ cuộn theo chiều dọc khi cần.

## 2. Các menu theo đối tượng

| Menu lớn | Menu con | Người phụ trách |
|---|---|---|
| Trang chủ | Tổng quan | Người 1 (khung giao diện) |
| Nhân sự & Tài khoản | Tài khoản, Nhân viên, Khách hàng | Người 1 |
| Thực đơn & Ưu đãi | Danh mục, Món, Khuyến mãi | Người 2 / Khuyến mãi: Người 1 |
| Bàn & Đặt bàn | Bàn, Phiếu đặt bàn | Người 2 |
| Hóa đơn & Thanh toán | Hóa đơn, Phương thức thanh toán | Người 3 |
| Thống kê & Báo cáo | Tổng quan, Doanh thu, Món bán chạy, Đặt bàn | Người 3 |

`ChiTietPhieuDatBan` được dự kiến đặt ngay trong giao diện `PhieuDatBan`.
`ChiTietHoaDon` và `ChiTietDatBan` được dự kiến đặt ngay trong giao diện `HoaDon`.
Như vậy có 13 entity nhưng không cần 13 mục menu riêng.

## 3. Phân công và cấu trúc code

- **Người 1:** `TaiKhoan`, `NhanVien`, `KhachHang`, `KhuyenMai`, đăng nhập và khung `MainFrame`.
- **Người 2:** `DanhMuc`, `Mon`, `Ban`, `PhieuDatBan`, `ChiTietPhieuDatBan`.
- **Người 3:** `HoaDon`, `ChiTietHoaDon`, `ChiTietDatBan`, `PhuongThucThanhToan`, thống kê.

Các package:

```text
src/
  app/                  Main.java (entry point)
  gui/                  MainFrame.java, MainView.java
    navigation/          MenuConfig.java (danh sach menu va menu con)
    theme/               Theme.java, CoffeeBackground.java
    components/          GlassPanel, NeonButton
    pages/               HomePanel, ModulePanel
  connectDB/             ConnectDB.java (chua can su dung)
  entity/                class model cua tung thanh vien
  dao/                   thao tac SQL cua tung thanh vien
  service/               nghiep vu lien quan nhieu bang
  dev/                   PreviewRenderer.java (screenshot offline)
  resources/             coffee-background.png (hinh nen ca phe)
lib/                     thu vien ngoai sau nay
sql/                     SQL Server script cua du an
```

## 4. Cách tích hợp màn hình khi mỗi người hoàn thành

Mỗi thành viên viết class giao diện **`extends JPanel`**, ví dụ:

```java
package gui;

import javax.swing.JPanel;

public class NhanVienGUI extends JPanel {
    public NhanVienGUI() {
        // Tạo JTable, nút Thêm / Sửa / Xóa / Tìm kiếm.
    }
}
```

Người tích hợp (người 1) đăng ký vào `MainFrame`:

```java
MainFrame frame = new MainFrame();
frame.registerModule("nhan-vien", new NhanVienGUI());
frame.setVisible(true);
```

**Không tạo JFrame riêng cho từng module.** Một `MainFrame` dùng `CardLayout`
giúp chuyển trang và giữ nguyên menu. Không để cả nhóm cùng sửa `MainView.java`.

Các `pageId` chính cần dùng trong `registerModule(...)`:

```text
tai-khoan    nhan-vien    khach-hang    khuyen-mai
danh-muc     mon          ban           phieu-dat-ban
hoa-don      phuong-thuc-thanh-toan
thong-ke-tong-quan    thong-ke-doanh-thu
thong-ke-mon          thong-ke-dat-ban
```

## 5. Dữ liệu và SQL Server

Trang chủ và các màn hình quản lý **không hiển thị số liệu giả lập**. Danh sách
hiện đang để trống; khi các thành viên xây dựng DAO, bảng sẽ lấy dữ liệu thật
từ SQL Server. Nút Thêm / Sửa / Xóa đang bị vô hiệu hóa cho đến khi có xử lý
nghiệp vụ tương ứng.

Khi tích hợp SQL Server JDBC, bỏ file driver JAR trong `lib/`, thêm vào
**Build Path → Add JARs**. Sau đó cấu hình `ConnectDB` với biến môi trường:

```text
CAFE_DB_URL=jdbc:sqlserver://localhost:1433;databaseName=QuanLyQuanCafe_Final;encrypt=true;trustServerCertificate=true
CAFE_DB_USER=sa
CAFE_DB_PASS=<mật khẩu máy của bạn>
```

Nếu dùng Windows Authentication, có thể bỏ `CAFE_DB_USER` và `CAFE_DB_PASS`,
nhưng JDBC driver cần được cấu hình phù hợp với máy.

## 6. Những gì chạy được ở giai đoạn này

- Mở `MainGUI` và trang chủ không cần SQL.
- Sidebar theo 5 nhóm đối tượng, mở/đóng menu con kiểu accordion.
- Chọn menu con sẽ vào màn hình mang đúng tên module, có khu vực tìm kiếm và bảng dữ liệu.
- Các thẻ truy cập nhanh trên trang chủ điều hướng đến module tương ứng.
- Tìm module theo tên trên thanh công cụ, nhấn Enter để chuyển trang.
- Tông sáng kem / nâu cà phê, ảnh cà phê làm nền, thẻ trong mờ và chữ màu tối dễ đọc.
- Nền `CardLayout` được vẽ kín trước mỗi lần chuyển menu, không lưu hình của trang trước.
- Menu tài khoản phía trên bên phải có màu chữ và nền tương phản, không còn nhãn mã nhân viên giả.
- Không còn các nhãn ‘demo’, ‘dữ liệu mẫu’, trạng thái chờ tích hợp hay hướng dẫn kỹ thuật trên giao diện.
- Cấu trúc package sẵn sàng để 3 thành viên cùng phát triển.

**Chưa có:** đăng nhập thật, CRUD thật, báo cáo từ SQL, kết nối JDBC đang hoạt động.
Những phần đó được giao cho các thành viên sau khi khung trang chủ được chốt.

### Kiểm tra lỗi chuyển menu

Mở `src/dev/NavigationRegression.java` → **Run As → Java Application**.
Bài kiểm tra tự vẽ lại nhiều màn hình liên tiếp để phát hiện hiện tượng chữ,
bảng hoặc thẻ của trang trước còn hằn lên trang mới.
