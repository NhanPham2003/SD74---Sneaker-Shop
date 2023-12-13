package com.example.demo.shipperController;

import com.example.demo.model.GiaoHang;
import com.example.demo.model.HoaDon;
import com.example.demo.model.NhanVien;
import com.example.demo.service.GiaoHangService;
import com.example.demo.service.HoaDonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class HomeOrder {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private GiaoHangService giaoHangService;

    @RequestMapping(value = {"", "/", "/home"})
    private String getHomeShipping(Model model){

        showData(model);

        showDataTab1(model);
        return "transportation/index";
    }

    @GetMapping("/delivery/update/{idHD}")
    private String viewUpdateGiaoHang(Model model, @PathVariable UUID idHD){

        showData(model);

        HoaDon hoaDon = hoaDonService.getOne(idHD);
        showDataGH(model, hoaDon);
        showDataTab4(model);

        return "transportation/index";

    }

    @PostMapping("/delivery/update/{idHD}")
    private String updateGiaoHang(Model model, @PathVariable UUID idHD){

        HoaDon hoaDon = hoaDonService.getOne(idHD);
        showData(model);

        String trangThaiGiaoHang = request.getParameter("trangThaiGiaoHang");
        String thanhPho = request.getParameter("city");
        String district = request.getParameter("district");
        String ward = request.getParameter("ward");
        String moTa = request.getParameter("moTa");

        if (trangThaiGiaoHang.equals("thanhCong")){
            String viTri = "Đơn hàng đã giao hàng thành công";

            GiaoHang giaoHang = new GiaoHang();
            giaoHang.setHoaDon(hoaDon);
            giaoHang.setTrangThai(1);
            giaoHang.setViTri(viTri);
            giaoHang.setThoiGian(new Date());
            giaoHangService.saveGiaoHang(giaoHang);

            hoaDon.setTrangThai(3);
            hoaDon.setTgThanhToan(new Date());
            hoaDon.setTgNhan(new Date());
            hoaDonService.add(hoaDon);

//            showDataGH(model, hoaDon);
            showData(model);
            showDataTab2(model);
            return "transportation/index";
        } else if (trangThaiGiaoHang.equals("thatBai")){
            String viTri = "Đơn hàng giao thất bại ( " +moTa + " )";

            GiaoHang giaoHang = new GiaoHang();
            giaoHang.setHoaDon(hoaDon);
            giaoHang.setTrangThai(2);
            giaoHang.setViTri(viTri);
            giaoHang.setThoiGian(new Date());
            giaoHangService.saveGiaoHang(giaoHang);

            List<GiaoHang> giaoHangList = giaoHangService.findByHoaDonAndTrangThai(hoaDon, 2);

            if (giaoHangList.size() == 3){
                hoaDon.setTrangThai(4);
                hoaDon.setTgHuy(new Date());
                hoaDon.setLyDoHuy(moTa);
                hoaDonService.add(hoaDon);

                showDataTab2(model);

                showData(model);

                return "transportation/index";
            }

            showDataGH(model, hoaDon);
            showDataTab2(model);
            return "transportation/index";
        } else {
            String viTri = "Đơn hàng đã đến"  + ", " + ward + ", " + district + " , " + thanhPho;

            List<GiaoHang> giaoHangList = giaoHangService.listGiaoHangByHoaDon(hoaDon);

            for (GiaoHang x: giaoHangList) {
                if(x.getTrangThai() == 1){
                    x.setTrangThai(0);
                    giaoHangService.saveGiaoHang(x);
                }
            }

            GiaoHang giaoHang = new GiaoHang();
            giaoHang.setHoaDon(hoaDon);
            giaoHang.setTrangThai(1);
            giaoHang.setViTri(viTri);
            giaoHang.setThoiGian(new Date());
            giaoHangService.saveGiaoHang(giaoHang);

            showDataGH(model, hoaDon);
            showDataTab2(model);
            return "transportation/index";

        }
    }

    @GetMapping("/bill/print/{idHD}")
    private String printBill(Model model, @PathVariable UUID idHD){

        HoaDon hoaDon = hoaDonService.getOne(idHD);

        model.addAttribute("billPrint", hoaDon);

        return "transportation/printBill";
    }

    @GetMapping("/delivery/refund/update/{idHD}")
    private String viewUpdateRefundGiaoHang(Model model, @PathVariable UUID idHD){

        showData(model);
        HoaDon hoaDon = hoaDonService.getOne(idHD);
        showDataGHHH(model, hoaDon);
        showDataTab5(model);

        return "transportation/index";

    }
    @PostMapping("/delivery/refund/update/{idHD}")
    private String updateRefundGiaoHang(Model model, @PathVariable UUID idHD){

        showData(model);

        HoaDon hoaDon = hoaDonService.getOne(idHD);
        showDataGHHH(model, hoaDon);
        System.out.println("CCCCCCC");

        showDataTab2(model);
        return "transportation/index";

    }

    @GetMapping("/bill/refund/print/{idHD}")
    private String printBillRefund(Model model, @PathVariable UUID idHD){

        HoaDon hoaDon = hoaDonService.getOne(idHD);

        model.addAttribute("billPrint", hoaDon);

        return "transportation/printBillRefund";
    }

    private void showData(Model model){
        NhanVien nhanVien = (NhanVien) session.getAttribute("shipperLogged");

        List<HoaDon> allHoaDonList = hoaDonService.listAllHoaDonByNhanVien(nhanVien);
        List<HoaDon> hoaDonDGList = hoaDonService.listHoaDonByNhanVienAndTrangThai(nhanVien, 2);
        List<HoaDon> hoaDonDoneList = hoaDonService.listHoaDonHuyAndThanhCongByNhanVien(nhanVien);

        model.addAttribute("allHoaDonList",allHoaDonList);
        model.addAttribute("hoaDonDGList",hoaDonDGList);
        model.addAttribute("hoaDonDoneList",hoaDonDoneList);

        model.addAttribute("nameNhanVien",nhanVien.getHoTenNV());
    }

    private void showDataGH(Model model, HoaDon hoaDon){

        List<GiaoHang> giaoHangListActive = giaoHangService.listGiaoHangByHoaDon(hoaDon);
        model.addAttribute("showHTGH", "true");
        model.addAttribute("HoaDonVanChuyen", hoaDon);
        model.addAttribute("giaoHangListActive", giaoHangListActive);
    }

    private void showDataGHHH(Model model, HoaDon hoaDon){

        List<GiaoHang> giaoHangListActive = giaoHangService.listGiaoHangByHoaDon(hoaDon);
        model.addAttribute("showHTGHHH", "true");
        model.addAttribute("HoaDonVanChuyen", hoaDon);
        model.addAttribute("giaoHangListActive", giaoHangListActive);

    }

    private void showDataTab1(Model model){

        model.addAttribute("activeAll", "nav-link active");
        model.addAttribute("activeVanChuyen", "nav-link");
        model.addAttribute("activeDone", "nav-link");
        model.addAttribute("activeMuaHang", "nav-link active");
        model.addAttribute("activeHoanHang", "nav-link");

        model.addAttribute("tabpane1", "tab-pane show active");
        model.addAttribute("tabpane2", "tab-pane");
        model.addAttribute("tabpane3", "tab-pane");
        model.addAttribute("tabpane4", "tab-pane");
        model.addAttribute("tabpane5", "tab-pane");
    }

    private void showDataTab2(Model model){

        model.addAttribute("activeAll", "nav-link");
        model.addAttribute("activeVanChuyen", "nav-link active");
        model.addAttribute("activeDone", "nav-link");
        model.addAttribute("activeMuaHang", "nav-link active");
        model.addAttribute("activeHoanHang", "nav-link");

        model.addAttribute("tabpane1", "tab-pane");
        model.addAttribute("tabpane2", "tab-pane show active");
        model.addAttribute("tabpane3", "tab-pane");
        model.addAttribute("tabpane4", "tab-pane show active");
        model.addAttribute("tabpane5", "tab-pane");
    }

    private void showDataTab3(Model model){

        model.addAttribute("activeAll", "nav-link");
        model.addAttribute("activeVanChuyen", "nav-link");
        model.addAttribute("activeDone", "nav-link active");
        model.addAttribute("activeMuaHang", "nav-link active");
        model.addAttribute("activeHoanHang", "nav-link");

        model.addAttribute("tabpane1", "tab-pane");
        model.addAttribute("tabpane2", "tab-pane");
        model.addAttribute("tabpane3", "tab-pane show active");
        model.addAttribute("tabpane4", "tab-pane");
        model.addAttribute("tabpane5", "tab-pane");
    }

    private void showDataTab4(Model model){

        model.addAttribute("activeAll", "nav-link");
        model.addAttribute("activeVanChuyen", "nav-link active");
        model.addAttribute("activeDone", "nav-link");
        model.addAttribute("activeMuaHang", "nav-link active");
        model.addAttribute("activeHoanHang", "nav-link");

        model.addAttribute("tabpane1", "tab-pane");
        model.addAttribute("tabpane2", "tab-pane show active");
        model.addAttribute("tabpane3", "tab-pane");
        model.addAttribute("tabpane4", "tab-pane show active");
        model.addAttribute("tabpane5", "tab-pane");
    }

    private void showDataTab5(Model model){

        model.addAttribute("activeAll", "nav-link");
        model.addAttribute("activeVanChuyen", "nav-link active");
        model.addAttribute("activeDone", "nav-link");
        model.addAttribute("activeMuaHang", "nav-link");
        model.addAttribute("activeHoanHang", "nav-link active");

        model.addAttribute("tabpane1", "tab-pane");
        model.addAttribute("tabpane2", "tab-pane show active");
        model.addAttribute("tabpane3", "tab-pane");
        model.addAttribute("tabpane4", "tab-pane");
        model.addAttribute("tabpane5", "tab-pane show active");
    }
}
