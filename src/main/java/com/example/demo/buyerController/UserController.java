package com.example.demo.buyerController;


import com.beust.ah.A;
import com.example.demo.model.*;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/buyer")
public class UserController {

    @Autowired
    private HttpSession session;

    @Autowired
    private GHCTService ghctService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private DiaChiKHService diaChiKHService;

    @Autowired
    private ShippingFeeService shippingFeeService;

    @Autowired
    private LoaiKhuyenMaiService loaiKhuyenMaiService;

    @Autowired
    private KhuyenMaiService khuyenMaiService;

    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private GiaoHangService giaoHangService;

    @Autowired
    private PhieuTraHangServices phieuTraHangServices;

    @Autowired
    private GiayChiTietService giayChiTietService;

    @GetMapping("/setting")
    private String getSettingAccount(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        UserForm(model, khachHang);

        model.addAttribute("pagesettingAccount",true);
        return "online/user";
    }

    @GetMapping("/addresses")
    private String getAddressAccount(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        UserForm(model, khachHang);

        List<DiaChiKH> diaChiKHDefaultList = diaChiKHService.findbyKhachHangAndLoaiAndTrangThai(khachHang, true, 1);
        List<DiaChiKH> diaChiKHList = diaChiKHService.findbyKhachHangAndLoaiAndTrangThai(khachHang, false, 1);

        if (diaChiKHDefaultList.size() == 0){
            model.addAttribute("diaChiShowNull",true);
        }else{
            model.addAttribute("diaChiShow",true);
            model.addAttribute("addressKHDefault", diaChiKHDefaultList.get(0));
            model.addAttribute("listCartDetail", diaChiKHList);

        }
        model.addAttribute("pageAddressesUser",true);
        return "online/user";
    }

    @PostMapping("/addresses/add")
    private String addnewAddress(Model model,@RequestParam(name = "defaultSelected", defaultValue = "false") boolean defaultSelected){
        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        String nameAddress = request.getParameter("nameAddress");
        String fullName = request.getParameter("fullName");
        String phoneAddress = request.getParameter("phoneAddress");
        String thernAddress = request.getParameter("thernAddress");
        String city = request.getParameter("city");
        String district = request.getParameter("district");
        String ward = request.getParameter("ward");
        String description = request.getParameter("description");

        String diaChiChiTiet = description + ", " + ward + ", " + district + ", " + city;

        DiaChiKH diaChiKH = new DiaChiKH();
        diaChiKH.setDiaChiChiTiet(diaChiChiTiet);
        diaChiKH.setMoTa(description);
        diaChiKH.setKhachHang(khachHang);
        diaChiKH.setTrangThai(1);
        diaChiKH.setMaDC( "DC_" + khachHang.getMaKH() );
        diaChiKH.setMien(thernAddress);
        diaChiKH.setSdtNguoiNhan(phoneAddress);
        diaChiKH.setQuanHuyen(district);
        diaChiKH.setTenDC(nameAddress);
        diaChiKH.setTinhTP(city);
        diaChiKH.setTenNguoiNhan(fullName);
        diaChiKH.setXaPhuong(ward);
        diaChiKH.setTgThem(new Date());
        diaChiKH.setLoai(defaultSelected);

        diaChiKHService.save(diaChiKH);

        return "redirect:/buyer/addresses";
    }

    @GetMapping("/addresses/delete/{idDC}")
    private String deleteAddress(Model model, @PathVariable UUID idDC){

        DiaChiKH diaChiKH =diaChiKHService.getByIdDiaChikh(idDC);
        diaChiKH.setTrangThai(0);
        diaChiKHService.save(diaChiKH);

        return "redirect:/buyer/addresses";
    }

    @GetMapping("/addresses/setDefault/{idDC}")
    private String setDefaultAddress(Model model, @PathVariable UUID idDC){
        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        DiaChiKH diaChiKH =diaChiKHService.getByIdDiaChikh(idDC);
        List<DiaChiKH> diaChiKHDefaultList = diaChiKHService.findbyKhachHangAndLoaiAndTrangThai(khachHang, true ,1);
        for (DiaChiKH x : diaChiKHDefaultList) {
            x.setLoai(false);
            diaChiKHService.save(x);
        }
        diaChiKH.setLoai(true);
        diaChiKHService.save(diaChiKH);

        return "redirect:/buyer/addresses";
    }

    @GetMapping("/notification")
    private String getNotidicationAccount(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        UserForm(model, khachHang);

        model.addAttribute("pageNotificationUser",true);
        return "online/user";
    }

    @GetMapping("/voucher")
    private String getVoucherAccount(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        UserForm(model, khachHang);

        model.addAttribute("pageVoucherUser",true);

        return "online/user";
    }

    @GetMapping("/coins")
    private String getCoinsAccount(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        UserForm(model, khachHang);

        model.addAttribute("pageCoinsUser",true);

        return "online/user";
    }

    @GetMapping("/purchase")
    private String getPurchaseAccount(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        UserForm(model, khachHang);

        List<HoaDon> listHoaDonByKhachHang = hoaDonService.findHoaDonByKhachHang(khachHang);

        List<HoaDon> listHoaDonChoThanhToan = hoaDonService.listHoaDonKhachHangAndTrangThaiOnline(khachHang, 0);


        model.addAttribute("pagePurchaseUser",true);
        model.addAttribute("purchaseAll",true);
        model.addAttribute("listAllHDByKhachHang", listHoaDonByKhachHang);
        model.addAttribute("listHoaDonChoThanhToan", listHoaDonChoThanhToan);

        model.addAttribute("type1","active");

        return "online/user";
    }

    @GetMapping("/purchase/pay")
    private String getPurchasePay(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        UserForm(model, khachHang);

        List<HoaDon> listHoaDonByKhachHang = hoaDonService.listHoaDonKhachHangAndTrangThaiOnline(khachHang, 0);



        model.addAttribute("listAllHDByKhachHang", listHoaDonByKhachHang);

        model.addAttribute("pagePurchaseUser",true);
        model.addAttribute("purchasePay",true);
        model.addAttribute("type2","active");
        return "online/user";
    }

    @GetMapping("/purchase/ship")
    private String getPurchaseShip(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        UserForm(model, khachHang);

        List<HoaDon> listHoaDonByKhachHang = hoaDonService.listHoaDonKhachHangAndTrangThaiOnline(khachHang, 1);


        model.addAttribute("listAllHDByKhachHang", listHoaDonByKhachHang);
        model.addAttribute("pagePurchaseUser",true);
        model.addAttribute("purchaseShip",true);
        model.addAttribute("type3","active");

        return "online/user";
    }

    @GetMapping("/purchase/receive")
    private String getPurchaseReceive(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        UserForm(model, khachHang);

        List<HoaDon> listHoaDonByKhachHang = hoaDonService.listHoaDonKhachHangAndTrangThaiOnline(khachHang, 2);

        model.addAttribute("listAllHDByKhachHang", listHoaDonByKhachHang);

        model.addAttribute("pagePurchaseUser",true);
        model.addAttribute("purchaseReceive",true);
        model.addAttribute("type4","active");
        return "online/user";
    }

    @GetMapping("/purchase/completed")
    private String getPurchaseCompleted(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        UserForm(model, khachHang);

        List<HoaDon> listHoaDonByKhachHang = hoaDonService.listHoaDonKhachHangAndTrangThaiOnline(khachHang, 3);

        model.addAttribute("listAllHDByKhachHang", listHoaDonByKhachHang);

        model.addAttribute("pagePurchaseUser",true);
        model.addAttribute("purchaseCompleted",true);
        model.addAttribute("type5","active");
        return "online/user";
    }

    @GetMapping("/purchase/cancel")
    private String getPurchaseCancel(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        UserForm(model, khachHang);

        List<HoaDon> listHoaDonByKhachHang = hoaDonService.listHoaDonKhachHangAndTrangThaiOnline(khachHang, 4);

        model.addAttribute("listAllHDByKhachHang", listHoaDonByKhachHang);

        model.addAttribute("pagePurchaseUser",true);
        model.addAttribute("purchaseCancel",true);
        model.addAttribute("type6","active");
        return "online/user";
    }

    @GetMapping("/purchase/refund")
    private String getPurchaseRefund(Model model){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        UserForm(model, khachHang);

        List<HoaDon> listHoaDonByKhachHang = hoaDonService.listHoaDonKhachHangAndTrangThaiOnline(khachHang, 7);

        List<HoaDon> hoaDonList = new ArrayList<>();

        for (HoaDon x: listHoaDonByKhachHang) {
            UUID idHDOld = x.getIdHDOld();
            HoaDon hoaDon = hoaDonService.getOne(idHDOld);
            hoaDonList.add(hoaDon);
        }
        model.addAttribute("listAllHDByKhachHang", hoaDonList);

        model.addAttribute("pagePurchaseUser",true);
        model.addAttribute("purchaseRefund",true);
        model.addAttribute("type7","active");
        return "online/user";
    }
//

    @GetMapping("/purchase/bill/detail/{idHD}")
    private String getDetailForm(Model model, @PathVariable UUID idHD){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");
        List<DiaChiKH> diaChiKHList = diaChiKHService.findbyKhachHangAndLoaiAndTrangThai(khachHang, false, 1);
        DiaChiKH diaChiKHDefault = diaChiKHService.findDCKHDefaulByKhachHang(khachHang);

        UserForm(model, khachHang);

        HoaDon hoaDon= hoaDonService.getOne(idHD);

        int trangThai = hoaDon.getTrangThai();
        if (trangThai == 0){

            Date ngayBatDau =  hoaDon.getTgTao();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ngayBatDau);

            // Thực hiện cộng thêm 30 ngày
            calendar.add(Calendar.DATE, 2);

            // Lấy ngày kết thúc
            Date ngayKetThuc = calendar.getTime();

            model.addAttribute("ngayKetThuc", ngayKetThuc);


            model.addAttribute("detailBillPay",true);
            model.addAttribute("modalThayDoiPhuongThucThanhToan",true);
            model.addAttribute("billDetailPay", hoaDon);

            session.removeAttribute("hoaDonPayDetail");
            session.setAttribute("hoaDonPayDetail", hoaDon);

        }else if (trangThai == 1){

            List<GiaoHang> giaoHangListActive = giaoHangService.listGiaoHangByHoaDon(hoaDon);
            model.addAttribute("giaoHangListActive", giaoHangListActive);
            model.addAttribute("modalThayDoiPhuongThucThanhToan",true);

            model.addAttribute("detailBillShip",true);
            model.addAttribute("billDetailShip", hoaDon);

            session.removeAttribute("hoaDonPayDetail");
            session.setAttribute("hoaDonPayDetail", hoaDon);

        }else if (trangThai == 2){

            List<GiaoHang> giaoHangListActive = giaoHangService.listGiaoHangByHoaDon(hoaDon);
            model.addAttribute("giaoHangListActive", giaoHangListActive);

            model.addAttribute("detailBillRecieve",true);
            model.addAttribute("billDetailRecieve", hoaDon);

        }else if (trangThai == 3){
            List<GiaoHang> giaoHangListActive = giaoHangService.listGiaoHangByHoaDon(hoaDon);
            model.addAttribute("giaoHangListActive", giaoHangListActive);

            Date ngayBatDau =  hoaDon.getTgThanhToan();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ngayBatDau);

            // Thực hiện cộng thêm 30 ngày
            calendar.add(Calendar.DATE, 2);

            // Lấy ngày kết thúc
            Date ngayKetThuc = calendar.getTime();
            model.addAttribute("ngayKetThucHoanHang", ngayKetThuc);

            model.addAttribute("detailBillCompleted",true);
            model.addAttribute("billDetailCompleted", hoaDon);
        }else if (trangThai == 4){
            List<GiaoHang> giaoHangListActive = giaoHangService.listGiaoHangByHoaDon(hoaDon);
            model.addAttribute("giaoHangListActive", giaoHangListActive);

            model.addAttribute("detailBillCancel",true);
            model.addAttribute("billDetailCancel", hoaDon);

        }else if (trangThai == 5){
            List<GiaoHang> giaoHangListActive = giaoHangService.listGiaoHangByHoaDon(hoaDon);
            model.addAttribute("giaoHangListActive", giaoHangListActive);

            model.addAttribute("detailBillRefund",true);
            model.addAttribute("billDetailRefund", hoaDon);

        }

        model.addAttribute("listAddressKH", diaChiKHList);
        model.addAttribute("diaChiKHDefault", diaChiKHDefault);

        return "online/user";
    }

    @PostMapping("/purchaser/bill/refund/{idHD}")
    private String getDetailRefundForm(Model model, @PathVariable UUID idHD){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");
        List<DiaChiKH> diaChiKHList = diaChiKHService.findbyKhachHangAndLoaiAndTrangThai(khachHang, false, 1);
        DiaChiKH diaChiKHDefault = diaChiKHService.findDCKHDefaulByKhachHang(khachHang);

        UserForm(model, khachHang);

        HoaDon hoaDon= hoaDonService.getOne(idHD);



        model.addAttribute("detailBillRefundMoney", true);
        model.addAttribute("billDetailRefund", hoaDon);


        return "online/user";                                                      
    }

    @GetMapping("/purchaser/bill/detail/refund/{idHD}")
    private String getDetailRequestRefundForm(Model model, @PathVariable UUID idHD){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");
        List<DiaChiKH> diaChiKHList = diaChiKHService.findbyKhachHangAndLoaiAndTrangThai(khachHang, false, 1);
        DiaChiKH diaChiKHDefault = diaChiKHService.findDCKHDefaulByKhachHang(khachHang);

        UserForm(model, khachHang);

        HoaDon hoaDon= hoaDonService.getOne(idHD);

        HoaDon hoaDonNew = hoaDonService.findByIdHoaDonOld(hoaDon.getIdHD());

        List<HoaDonChiTiet> hoaDonChiTietList = new ArrayList<>();

        Double tongTienHoan= 0.0;

        for (HoaDonChiTiet x:hoaDonNew.getHoaDonChiTiets()) {
            if(x.getTrangThai()==1){
                hoaDonChiTietList.add(x);
            }
            if(x.getTrangThai() ==2){
                tongTienHoan += x.getDonGia();
            }
        }

        PhieuTraHang phieuTraHang = phieuTraHangServices.findByHoaDon(hoaDonNew);

        if(hoaDonChiTietList != null){
            model.addAttribute("lyDoTuChoi", hoaDonChiTietList.get(0).getMoTa());
        }

        model.addAttribute("tongTienHoan", tongTienHoan);
        model.addAttribute("phieuTraHang", phieuTraHang);
        model.addAttribute("detailBillRefundMoneyRequest", true);
        model.addAttribute("billDetailRefund", hoaDon);
        model.addAttribute("billDetailRefundNew", hoaDonNew);

        if (phieuTraHang.getTrangThai() == 4){
            model.addAttribute("diaChiHoanHang", true);
            model.addAttribute("thongTinHoanHang", true);
            model.addAttribute("diaChiKHDefault", diaChiKHDefault);
            model.addAttribute("listAddressKH", diaChiKHList);
        }

        return "online/user";
    }

    @PostMapping("/purchase/bill/refund/request/{idHD}")
    private String getDetailRefundRequestForm(Model model, @PathVariable UUID idHD,
                                              @RequestParam("imagesRequest") List<MultipartFile> files,
                                              @RequestParam("ctspSelected") List<String> idCTGAndQuantity) {

        String lyDoMuonTra = request.getParameter("lyDoMuonTra");
        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        Date date = new Date();

        HoaDon hoaDon = hoaDonService.getOne(idHD);
        HoaDon hoaDonNew = new HoaDon();

        hoaDonNew.setTrangThai(7);
        hoaDonNew.setIdHDOld(hoaDon.getIdHD());
        hoaDonNew.setMaHDOld(hoaDon.getMaHD());
        hoaDonNew.setTgTao(date);
        hoaDonNew.setMaHD("HD001_" + khachHang.getMaKH() + date.getDay() + generateRandomNumbers());
        hoaDonNew.setKhachHang(khachHang);

        hoaDonService.add(hoaDonNew);
        List<HoaDonChiTiet> hoaDonChiTiets = new ArrayList<>();

        for (String x:idCTGAndQuantity) {
            String[] pairs = x.split(", ");

            Map<String, String> data = new HashMap<>();

            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                data.put(keyValue[0], keyValue[1]);
            }

            UUID productId = UUID.fromString(data.get("id"));
            Integer quantity = Integer.parseInt(data.get("quantity"));

            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietService.getOne(hoaDon.getIdHD(), productId);

            Double phanTramCTG = hoaDonChiTiet.getDonGia()/hoaDonChiTiet.getSoLuong()/hoaDon.getTongTien();

            Double giaBanCTG = hoaDon.getTongTienDG()*phanTramCTG;

            HoaDonChiTiet hoaDonChiTietNew = new HoaDonChiTiet();

            hoaDonChiTietNew.setSoLuong(quantity);
            hoaDonChiTietNew.setChiTietGiay(giayChiTietService.getByIdChiTietGiay(productId));
            hoaDonChiTietNew.setTrangThai(1);
            hoaDonChiTietNew.setHoaDon(hoaDonNew);
            hoaDonChiTietNew.setDonGia(giaBanCTG*quantity);

            hoaDonChiTietService.add(hoaDonChiTietNew);
            hoaDonChiTiets.add(hoaDonChiTietNew);

        }

        int sumQuantity = hoaDonChiTiets.stream()
                .mapToInt(HoaDonChiTiet::getSoLuong)
                .sum();

        double total = hoaDonChiTiets.stream()
                .mapToDouble(HoaDonChiTiet::getDonGia)
                .sum();

        hoaDonNew.setTongTien(total);
        hoaDonNew.setTongSP(sumQuantity);
        hoaDonNew.setTienShip(0.0);
        hoaDonNew.setTongTienDG(total);
        hoaDonNew.setHinhThucThanhToan(1);
        hoaDonNew.setLoaiHD(0);
        hoaDonNew.setGiamGiaHoaDon(0.0);
        hoaDonNew.setGiamGiaShip(0.0);
        hoaDonService.add(hoaDonNew);


        String maPHH = "PHH_" + khachHang.getMaKH() + date.getDay() + generateRandomNumbers();

        try {
            PhieuTraHang phieuTraHang = new PhieuTraHang();
            phieuTraHang.setKhachHang(khachHang);
            phieuTraHang.setHoaDon(hoaDonNew);
            phieuTraHang.setLyDoHoanHang(lyDoMuonTra);
            phieuTraHang.setTgTao(new Date());
            phieuTraHang.setTrangThai(0);
            phieuTraHang.setMaMPTH(maPHH);

            List<String> tenAnh = new ArrayList<>();

            for (MultipartFile file : files) {

                String originalFilename = file.getOriginalFilename();

                String newFileName = generateNewFileName(originalFilename);

                tenAnh.add(newFileName);

                String filePath = "F:/Final_Fpoly/SD74---Sneaker-Shop/src/main/resources/static/images/imagesRate/" + newFileName;
                File dest = new File(filePath);

                File parentDirectory = dest.getParentFile();
                if (!parentDirectory.exists() && !parentDirectory.mkdirs()) {
                    throw new IOException("Không thể tạo thư mục: " + parentDirectory);
                }
                file.transferTo(dest);

            }
            phieuTraHang.setHinh1(tenAnh.get(0));

            if (tenAnh.get(1) != null){
                phieuTraHang.setHinh2(tenAnh.get(1));
            }

            if (tenAnh.get(2) != null){
                phieuTraHang.setHinh3(tenAnh.get(2));
            }
            phieuTraHangServices.savePTH(phieuTraHang);

        } catch (IOException e) {
            e.printStackTrace();
        }

        hoaDon.setMaHDOld("0");
        hoaDonService.add(hoaDon);

        return "redirect:/buyer/home";
    }

    @GetMapping("/purchase/bill/detail/cancel/{idHD}")
    private String getDetailCancelForm(Model model, @PathVariable UUID idHD){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");
        List<DiaChiKH> diaChiKHList = diaChiKHService.findbyKhachHangAndLoaiAndTrangThai(khachHang, false, 1);
        DiaChiKH diaChiKHDefault = diaChiKHService.findDCKHDefaulByKhachHang(khachHang);

        UserForm(model, khachHang);

        HoaDon hoaDon= hoaDonService.getOne(idHD);

        int trangThai = hoaDon.getTrangThai();
        if (trangThai == 0){
            model.addAttribute("modalHuyHoaDonInDetailBillPay",true);
            model.addAttribute("modalThayDoiPhuongThucThanhToan",true);

            model.addAttribute("detailBillPay",true);
            model.addAttribute("billDetailPay", hoaDon);

            session.removeAttribute("hoaDonPayDetail");
            session.setAttribute("hoaDonPayDetail", hoaDon);

        }else if (trangThai == 1){
            model.addAttribute("modalHuyHoaDonInDetailBillPay",true);

            List<GiaoHang> giaoHangListActive = giaoHangService.listGiaoHangByHoaDon(hoaDon);
            model.addAttribute("giaoHangListActive", giaoHangListActive);
            model.addAttribute("modalThayDoiPhuongThucThanhToan",true);

            model.addAttribute("detailBillShip",true);
            model.addAttribute("billDetailShip", hoaDon);

            session.removeAttribute("hoaDonPayDetail");
            session.setAttribute("hoaDonPayDetail", hoaDon);

        }else if (trangThai == 2){

            List<GiaoHang> giaoHangListActive = giaoHangService.listGiaoHangByHoaDon(hoaDon);
            model.addAttribute("giaoHangListActive", giaoHangListActive);

            model.addAttribute("detailBillRecieve",true);
            model.addAttribute("billDetailRecieve", hoaDon);

        }else if (trangThai == 3){
            List<GiaoHang> giaoHangListActive = giaoHangService.listGiaoHangByHoaDon(hoaDon);
            model.addAttribute("giaoHangListActive", giaoHangListActive);

            model.addAttribute("detailBillCompleted",true);
            model.addAttribute("billDetailCompleted", hoaDon);
        }else if (trangThai == 4){
            List<GiaoHang> giaoHangListActive = giaoHangService.listGiaoHangByHoaDon(hoaDon);
            model.addAttribute("giaoHangListActive", giaoHangListActive);

            model.addAttribute("detailBillCancel",true);
            model.addAttribute("billDetailCancel", hoaDon);

        }else if (trangThai == 5){
            List<GiaoHang> giaoHangListActive = giaoHangService.listGiaoHangByHoaDon(hoaDon);
            model.addAttribute("giaoHangListActive", giaoHangListActive);

            model.addAttribute("detailBillRefund",true);
            model.addAttribute("billDetailRefund", hoaDon);

        }

        model.addAttribute("listAddressKH", diaChiKHList);
        model.addAttribute("diaChiKHDefault", diaChiKHDefault);

        return "online/user";
    }

    @PostMapping("/purchase/bill/refund/recall/{idHD}")
    private String getRecallRefundForm(Model model, @PathVariable UUID idHD) {
        HoaDon hoaDonOld = hoaDonService.getOne(idHD);

        HoaDon hoaDonNew = hoaDonService.findByIdHoaDonOld(hoaDonOld.getIdHD());

        PhieuTraHang phieuTraHang = phieuTraHangServices.findByHoaDon(hoaDonNew);

        phieuTraHang.setTrangThai(3);
        phieuTraHang.setTgKhachHuy(new Date());

        phieuTraHangServices.savePTH(phieuTraHang);

        hoaDonOld.setMaHDOld("3");
        hoaDonService.add(hoaDonNew);

        return "redirect:/buyer/purchase/refund";
    }

    @PostMapping("/purchase/bill/refund/accept/{idHD}")
    private String getAcceptRefundForm(Model model, @PathVariable UUID idHD){

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");
        DiaChiKH diaChiKHDefault = diaChiKHService.findDCKHDefaulByKhachHang(khachHang);
        List<DiaChiKH> diaChiKHList = diaChiKHService.findbyKhachHangAndLoaiAndTrangThai(khachHang, false, 1);


        HoaDon hoaDonOld = hoaDonService.getOne(idHD);

        HoaDon hoaDonNew = hoaDonService.findByIdHoaDonOld(hoaDonOld.getIdHD());

        List<HoaDonChiTiet> hoaDonChiTietList = new ArrayList<>();

        for (HoaDonChiTiet x: hoaDonNew.getHoaDonChiTiets()) {
            if(x.getTrangThai() == 2){
                hoaDonChiTietList.add(x);
            }
        }
        int sumQuantity = hoaDonChiTietList.stream()
                .mapToInt(HoaDonChiTiet::getSoLuong)
                .sum();
        double total = hoaDonChiTietList.stream()
                .mapToDouble(HoaDonChiTiet::getDonGia)
                .sum();

        hoaDonNew.setTongTien(total);
        hoaDonNew.setTongTienDG(total);
        hoaDonNew.setTongSP(sumQuantity);
        hoaDonService.add(hoaDonNew);

        PhieuTraHang phieuTraHang = phieuTraHangServices.findByHoaDon(hoaDonNew);
        phieuTraHang.setTrangThai(4);
        phieuTraHang.setTgXacNhan(new Date());
        phieuTraHangServices.savePTH(phieuTraHang);

        hoaDonOld.setMaHDOld("4");
        hoaDonService.add(hoaDonOld);

        model.addAttribute("thongTinHoanHang", true);

        model.addAttribute("tongTienHoan", hoaDonNew.getTongTien());
        model.addAttribute("phieuTraHang", phieuTraHang);
        model.addAttribute("diaChiHoanHang", true);
        model.addAttribute("detailBillRefundMoneyRequest", true);
        model.addAttribute("billDetailRefund", hoaDonOld);
        model.addAttribute("billDetailRefundNew", hoaDonNew);
        model.addAttribute("diaChiKHDefault", diaChiKHDefault);
        model.addAttribute("listAddressKH", diaChiKHList);

        return "redirect:/buyer/purchase/refund";
    }

    @PostMapping("/purchase/pay/change/payment/{idHD}")
    private String changePaymentMethod(Model model, @PathVariable UUID idHD){

        HoaDon hoaDon = (HoaDon) session.getAttribute("hoaDonPayDetail");
        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");

        String hinhThucThayDoi = request.getParameter("paymentMethod");

        if (hinhThucThayDoi.equals("qrCodeBanking")){
            UserForm(model, khachHang);
            hoaDon.setHinhThucThanhToan(1);
            hoaDon.setTrangThai(0);
            hoaDonService.add(hoaDon);

            return "redirect:/buyer/purchase/pay";
        }else{
            UserForm(model, khachHang);

            hoaDon.setHinhThucThanhToan(0);
            hoaDon.setTrangThai(1);
            hoaDonService.add(hoaDon);

            return "redirect:/buyer/purchase/ship";
        }

    }

    @PostMapping("/purchase/bill/pay/cancel/{idHD}")
    private String cancelBillPay(Model model, @PathVariable UUID idHD){
        String lyDoHuy = request.getParameter("lyDoHuy");

        KhachHang khachHang = (KhachHang) session.getAttribute("KhachHangLogin");
        DiaChiKH diaChiKHDefault = diaChiKHService.findDCKHDefaulByKhachHang(khachHang);

        HoaDon hoaDonHuy = hoaDonService.getOne(idHD);

        Date date = new Date();
        HoaDon hoaDonNew = new HoaDon();

        String maHD = "HD_" + khachHang.getMaKH()+ date.getDate() + generateRandomNumbers();

        if(lyDoHuy.equals("changeVoucher")){

            List<DiaChiKH> diaChiKHList = diaChiKHService.findbyKhachHangAndLoaiAndTrangThai(khachHang, false, 1);
            LoaiKhuyenMai loaiKhuyenMaiBill = loaiKhuyenMaiService.findByMaLKM("LKM01");
            LoaiKhuyenMai loaiKhuyenMaiShip = loaiKhuyenMaiService.findByMaLKM("LKM03");

            List<KhuyenMai> khuyenMaiListShipping = khuyenMaiService.findByLoaiKMAndTrangThai(loaiKhuyenMaiShip);
            List<KhuyenMai> khuyenMaiListBill =  khuyenMaiService.findByLoaiKMAndTrangThai(loaiKhuyenMaiBill);

            lyDoHuy = "Tôi muốn thêm/thay đổi mã giảm giá";

            hoaDonHuy.setTrangThai(4);
            hoaDonHuy.setLyDoHuy(lyDoHuy);
            hoaDonHuy.setTgHuy(new Date());
            hoaDonService.add(hoaDonHuy);

            hoaDonNew.setTrangThai(6);
            hoaDonNew.setTgTao(new Date());
            hoaDonNew.setLoaiHD(0);
            hoaDonNew.setMaHD(maHD);
            hoaDonNew.setKhachHang(khachHang);
            hoaDonNew.setDiaChiNguoiNhan(diaChiKHDefault.getDiaChiChiTiet());
            hoaDonNew.setSdtNguoiNhan(diaChiKHDefault.getSdtNguoiNhan());
            hoaDonNew.setTenNguoiNhan(diaChiKHDefault.getTenNguoiNhan());

            hoaDonService.add(hoaDonNew);

            List<HoaDonChiTiet> hoaDonChiTietListNew = hoaDonChiTietService.findByHoaDon(hoaDonHuy);

            for (HoaDonChiTiet hoaDonChiTiet:hoaDonChiTietListNew) {

                HoaDonChiTiet hoaDonChiTietNew = new HoaDonChiTiet();
                hoaDonChiTietNew.setHoaDon(hoaDonNew);
                hoaDonChiTietNew.setChiTietGiay(hoaDonChiTiet.getChiTietGiay());
                hoaDonChiTietNew.setSoLuong(hoaDonChiTiet.getSoLuong());
                hoaDonChiTietNew.setDonGia(hoaDonChiTiet.getDonGia());
                hoaDonChiTietNew.setTgThem(new Date());
                hoaDonChiTietNew.setTrangThai(1);

                hoaDonChiTietService.add(hoaDonChiTietNew);

            }
            List<HoaDonChiTiet> listHDCTCheckOut = hoaDonChiTietService.findByHoaDon(hoaDonNew);

            int sumQuantity = hoaDonChiTietListNew.stream()
                    .mapToInt(HoaDonChiTiet::getSoLuong)
                    .sum();

            double total = hoaDonChiTietListNew.stream()
                    .mapToDouble(HoaDonChiTiet::getDonGia)
                    .sum();

            hoaDonNew.setTongTien(total);
            hoaDonNew.setTongSP(sumQuantity);

            hoaDonService.add(hoaDonNew);

            Double shippingFee = shippingFeeService.calculatorShippingFee(hoaDonNew, 25000.0);

            session.removeAttribute("hoaDonTaoMoi");
            session.setAttribute("hoaDonTaoMoi", hoaDonNew);


//
            model.addAttribute("shippingFee", shippingFee);
            model.addAttribute("toTalOder", total  + shippingFee );
            model.addAttribute("tongTienDaGiamVoucherShip", total + shippingFee);
            model.addAttribute("sumQuantity", sumQuantity);
            model.addAttribute("total", total);
            model.addAttribute("listProductCheckOut", listHDCTCheckOut);
            model.addAttribute("fullNameLogin", khachHang.getHoTenKH());
            model.addAttribute("listVoucherBill", khuyenMaiListBill);
            model.addAttribute("listVoucherShipping", khuyenMaiListShipping);
            model.addAttribute("toTalOder", total);

            model.addAttribute("tienGiamVoucherShip", 0);
            model.addAttribute("tienGiamVoucherBill", 0);
            model.addAttribute("diaChiKHDefault", diaChiKHDefault);
            model.addAttribute("addNewAddressNotNull", true);
            model.addAttribute("listAddressKH", diaChiKHList);

            return "online/checkout";

        }else if(lyDoHuy.equals("changeSize")){
            lyDoHuy = "Tôi muốn thay đổi size/ màu";
        }else if(lyDoHuy.equals("changeProduct")){
            lyDoHuy = "Tôi muốn thay đổi sản phẩm";
            hoaDonHuy.setTrangThai(4);
            hoaDonHuy.setLyDoHuy(lyDoHuy);
            hoaDonHuy.setTgHuy(new Date());
            hoaDonService.add(hoaDonHuy);
        }else if(lyDoHuy.equals("none")){
            lyDoHuy = "Tôi không  có nhu cầu mua nữa";
            hoaDonHuy.setTrangThai(4);
            hoaDonHuy.setLyDoHuy(lyDoHuy);
            hoaDonHuy.setTgHuy(new Date());
            hoaDonService.add(hoaDonHuy);
        }else if (lyDoHuy.equals("lyDoKhac")) {
            lyDoHuy = request.getParameter("hutThuocNenDauDaDay");
            hoaDonHuy.setTrangThai(4);
            hoaDonHuy.setLyDoHuy(lyDoHuy);
            hoaDonHuy.setTgHuy(new Date());
            hoaDonService.add(hoaDonHuy);
        }else if(lyDoHuy.equals("changeSize")) {
            lyDoHuy = request.getParameter("hutThuocNenDauDaDay");
            hoaDonHuy.setTrangThai(4);
            hoaDonHuy.setLyDoHuy(lyDoHuy);
            hoaDonHuy.setTgHuy(new Date());
            hoaDonService.add(hoaDonHuy);
        }
    return "redirect:/buyer/purchase/cancel";

    }

    @GetMapping("/purchaser/bill/buy/again/{idHD}")
    private String buyAgain(Model model, @PathVariable UUID idHD){
        HoaDon hoaDonBuyAgain = hoaDonService.getOne(idHD);

        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.findByHoaDon(hoaDonBuyAgain);

        GioHang gioHang = (GioHang) session.getAttribute("GHLogged") ;

        for (HoaDonChiTiet x:hoaDonChiTietList) {

            GioHangChiTiet gioHangChiTietExist = ghctService.findByCTSPActive(x.getChiTietGiay());

            if (gioHangChiTietExist != null){

                gioHangChiTietExist.setSoLuong(gioHangChiTietExist.getSoLuong() + x.getSoLuong());
                gioHangChiTietExist.setTgThem(new Date());
                gioHangChiTietExist.setDonGia(x.getSoLuong()*x.getChiTietGiay().getGiaBan() + gioHangChiTietExist.getDonGia());
                ghctService.addNewGHCT(gioHangChiTietExist);

            }else {

                GioHangChiTiet gioHangChiTiet = new GioHangChiTiet();

                gioHangChiTiet.setGioHang(gioHang);
                gioHangChiTiet.setDonGia(x.getSoLuong() * x.getChiTietGiay().getGiaBan());
                gioHangChiTiet.setChiTietGiay(x.getChiTietGiay());
                gioHangChiTiet.setSoLuong(x.getSoLuong());
                gioHangChiTiet.setTrangThai(1);
                gioHangChiTiet.setTgThem(new Date());

                ghctService.addNewGHCT(gioHangChiTiet);
            }
        }

        return "redirect:/buyer/cart";
    }

    private void UserForm(Model model, KhachHang khachHang){
        GioHang gioHang = (GioHang) session.getAttribute("GHLogged") ;
        model.addAttribute("fullNameLogin", khachHang.getHoTenKH());

        List<GioHangChiTiet> listGHCTActive = ghctService.findByGHActive(gioHang);
        Integer sumProductInCart = listGHCTActive.size();
        model.addAttribute("sumProductInCart", sumProductInCart);
    }

    public String generateRandomNumbers() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int randomNumber = random.nextInt(10); // Tạo số ngẫu nhiên từ 0 đến 9
            sb.append(randomNumber);
        }
        return sb.toString();
    }

    private String generateNewFileName(String originalFileName) {
        long timestamp = System.currentTimeMillis();
        String[] parts = originalFileName.split("\\.");
        String extension = parts[parts.length - 1];
        return "new_file_" + timestamp + "." + extension;
    }

}

