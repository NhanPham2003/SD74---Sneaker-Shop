package com.example.demo.controller;

import com.example.demo.config.*;
import com.example.demo.model.ChiTietGiay;
import com.example.demo.model.Giay;
import com.example.demo.model.Hang;
import com.example.demo.model.Size;
import com.example.demo.repository.GiayChiTietRepository;
import com.example.demo.service.GiayChiTietService;
import com.example.demo.service.GiayService;
import com.example.demo.service.HangService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/manage")
@Controller
public class HangController {
    @Autowired
    private HangService hangService;
    @Autowired
    private GiayService giayService;
    @Autowired
    private GiayChiTietService giayChiTietService;

    @ModelAttribute("dsTrangThai")
    public Map<Integer, String> getDsTrangThai() {
        Map<Integer, String> dsTrangThai = new HashMap<>();
        dsTrangThai.put(1, "Hoạt động");
        dsTrangThai.put(0, "Không hoạt động");
        return dsTrangThai;
    }

    @GetMapping("/hang")
    public String dsHang(Model model) {
        List<Hang> hang = hangService.getALlHang();
        Collections.sort(hang, (a, b) -> b.getTgThem().compareTo(a.getTgThem()));
        model.addAttribute("hang", hang);
        //
        model.addAttribute("hangAdd", new Hang());
        return "manage/hang";
    }

//    @GetMapping("/hang/viewAdd")
//    public String viewAddHang(Model model) {
//        model.addAttribute("hang", new Hang());
//        return "manage/add-hang";
//    }

    @PostMapping("/hang/viewAdd/add")
    public String addHang(@Valid @ModelAttribute("hang") Hang hang, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "manage/add-hang";
        }
        Hang hang1 = new Hang();
        hang1.setLogoHang(hang.getLogoHang());
        hang1.setMaHang(hang.getMaHang());
        hang1.setTenHang(hang.getTenHang());
        hang1.setTgThem(new Date());
        hang1.setTrangThai(1);
        hangService.save(hang1);
        return "redirect:/manage/hang";
    }

    @GetMapping("/hang/delete/{id}")
    public String deleteHang(@PathVariable UUID id) {
        Hang hang = hangService.getByIdHang(id);
        hang.setTrangThai(0);
        hang.setTgSua(new Date());
        hangService.save(hang);
        // Cập nhật trạng thái của tất cả sản phẩm chi tiết của hãng thành 0
        List<Giay> giays = giayService.findByHang(hang);
        for (Giay giay : giays) {
            giay.setTrangThai(0);
            giayService.save(giay);
        }
        //List giày mới
        List<Giay> giaysNew = giayService.findByTrangThai(0);
        return "redirect:/manage/hang";
    }

    @GetMapping("/hang/viewUpdate/{id}")
    public String viewUpdateHang(@PathVariable UUID id, Model model) {
        Hang hang = hangService.getByIdHang(id);
        model.addAttribute("hang", hang);
        return "manage/update-hang";
    }

    @PostMapping("/hang/viewUpdate/{id}")
    public String updateHang(@PathVariable UUID id, @ModelAttribute("hang") Hang hang) {
        Hang hangDb = hangService.getByIdHang(id);
        if (hangDb != null) {
            hangDb.setLogoHang(hang.getLogoHang());
            hangDb.setMaHang(hang.getMaHang());
            hangDb.setTenHang(hang.getTenHang());
            hangDb.setTgSua(new Date());
            hangDb.setTrangThai(hang.getTrangThai());
            hangService.save(hangDb);
        }
        // Nếu trạng thái của hãng là 1, hãy cập nhật trạng thái của tất cả sản phẩm chi tiết của hãng thành 1.
        if (hangDb.getTrangThai() == 1) {
            List<Giay> giays = giayService.findByHang(hangDb);
            for (Giay giay : giays) {
                giay.setTrangThai(1);
                giayService.save(giay);
            }
        }
        return "redirect:/manage/hang";
    }

    @GetMapping("/hang/export/pdf")
    public void exportToPDFHang(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=hang_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Hang> listHang = hangService.getALlHang();

        PDFExporterHang exporter = new PDFExporterHang(listHang);
        exporter.export(response);
    }

    @GetMapping("/hang/export/excel")
    public void exportToExcelSize(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=hang_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Hang> listHang = hangService.getALlHang();

        ExcelExporterHang excelExporter = new ExcelExporterHang(listHang);

        excelExporter.export(response);
    }

    @GetMapping("/hang/filter")
    public String filterData(Model model,
                             @RequestParam(value = "maHang", required = false) String maHang,
                             @RequestParam(value = "tenHang", required = false) String tenHang) {
        // Thực hiện lọc dữ liệu dựa trên selectedSize (và trạng thái nếu cần)
        List<Hang> filteredHangs;
        if ("Mã Hãng".equals(maHang) && "Tên Hãng".equals(tenHang)) {
            // Nếu người dùng chọn "Tất cả", hiển thị tất cả dữ liệu
            filteredHangs = hangService.getALlHang();
        } else {
            // Thực hiện lọc dữ liệu dựa trên selectedSize
            filteredHangs = hangService.fillterHang(maHang, tenHang);
        }
        model.addAttribute("hang", filteredHangs);
        model.addAttribute("hangAll", hangService.getALlHang());

        return "manage/hang"; // Trả về mẫu HTML chứa bảng dữ liệu sau khi lọc
    }

    @PostMapping("/hang/import")
    public String importData(@RequestParam("file") MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                InputStream excelFile = file.getInputStream();
                hangService.importDataFromExcel(excelFile); // Gọi phương thức nhập liệu từ Excel
            } catch (Exception e) {
                e.printStackTrace();
                // Xử lý lỗi
            }
        }
        return "redirect:/manage/hang"; // Chuyển hướng sau khi nhập liệu thành công hoặc không thành công
    }
}
