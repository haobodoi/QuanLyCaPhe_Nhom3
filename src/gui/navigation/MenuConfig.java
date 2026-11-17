package gui.navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Mo ta menu theo doi tuong, gom 5 nhom, cac man hinh chi tiet nam trong doi tuong cha.
 * Moi thanh vien viet GUI rieng va dang ky bang page key tu day.
 */
public final class MenuConfig {
    public static final class Page {
        public final String id;
        public final String title;
        public final String subtitle;
        public final String owner;
        public final String relatedClasses;

        public Page(String id, String title, String subtitle, String owner, String relatedClasses) {
            this.id = id;
            this.title = title;
            this.subtitle = subtitle;
            this.owner = owner;
            this.relatedClasses = relatedClasses;
        }
    }

    public static final class Group {
        public final String title;
        public final String shortCode;
        public final List<Page> pages;

        public Group(String title, String shortCode, Page... pages) {
            this.title = title;
            this.shortCode = shortCode;
            this.pages = Collections.unmodifiableList(Arrays.asList(pages));
        }
    }

    private static final List<Group> GROUPS;
    private static final Map<String, Page> PAGES;

    static {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group("NHÂN SỰ & TÀI KHOẢN", "NS",
                new Page("tai-khoan", "Tài khoản", "Quản lý đăng nhập và phân quyền",
                         "Người 1", "TaiKhoan"),
                new Page("nhan-vien", "Nhân viên", "Hồ sơ và thông tin nhân viên",
                         "Người 1", "NhanVien"),
                new Page("khach-hang", "Khách hàng", "Khách hàng và điểm tích lũy",
                         "Người 1", "KhachHang")));
        groups.add(new Group("THỰC ĐƠN & ƯU ĐÃI", "TD",
                new Page("danh-muc", "Danh mục", "Danh mục đồ uống và món ăn",
                         "Người 2", "DanhMuc"),
                new Page("mon", "Món", "Thực đơn, giá và trạng thái bán",
                         "Người 2", "Mon, DanhMuc"),
                new Page("khuyen-mai", "Khuyến mãi", "Chương trình ưu đãi của quán",
                         "Người 1", "KhuyenMai")));
        groups.add(new Group("BÀN & ĐẶT BÀN", "BA",
                new Page("ban", "Bàn", "Sơ đồ bàn và sức chứa",
                         "Người 2", "Ban"),
                new Page("phieu-dat-ban", "Phiếu đặt bàn", "Phiếu và chi tiết bàn đã đặt",
                         "Người 2", "PhieuDatBan, ChiTietPhieuDatBan")));
        groups.add(new Group("HÓA ĐƠN & THANH TOÁN", "HD",
                new Page("hoa-don", "Hóa đơn", "Hóa đơn, món và bàn thực tế",
                         "Người 3", "HoaDon, ChiTietHoaDon, ChiTietDatBan"),
                new Page("phuong-thuc-thanh-toan", "Phương thức thanh toán",
                         "Thanh toán và cập nhật điểm tích lũy",
                         "Người 3", "PhuongThucThanhToan")));
        groups.add(new Group("THỐNG KÊ & BÁO CÁO", "TK",
                new Page("thong-ke-tong-quan", "Tổng quan thống kê", "Tổng hợp hoạt động kinh doanh",
                         "Người 3", "HoaDon, PhuongThucThanhToan"),
                new Page("thong-ke-doanh-thu", "Doanh thu", "Doanh thu theo ngày, tháng, năm",
                         "Người 3", "HoaDon, PhuongThucThanhToan"),
                new Page("thong-ke-mon", "Món bán chạy", "Thống kê số lượng món tiêu thụ",
                         "Người 3", "ChiTietHoaDon, Mon"),
                new Page("thong-ke-dat-ban", "Đặt bàn", "Thống kê số phiếu và tần suất bàn",
                         "Người 3", "PhieuDatBan, ChiTietPhieuDatBan")));

        GROUPS = Collections.unmodifiableList(groups);
        Map<String, Page> map = new LinkedHashMap<>();
        for (Group group : groups) {
            for (Page page : group.pages) map.put(page.id, page);
        }
        PAGES = Collections.unmodifiableMap(map);
    }

    private MenuConfig() { }

    public static List<Group> groups() { return GROUPS; }

    public static Map<String, Page> pages() { return PAGES; }

    public static Page byId(String id) { return PAGES.get(id); }
}
