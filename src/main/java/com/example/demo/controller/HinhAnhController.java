package com.example.demo.controller;

import com.example.demo.model.HinhAnh;
import com.example.demo.service.HinhAnhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@RequestMapping("/manage")
@Controller
public class HinhAnhController {
    @Autowired
    private HinhAnhService hinhAnhService;

    @ModelAttribute("dsTrangThai")
    public Map<Integer, String> getDsTrangThai() {
        Map<Integer, String> dsTrangThai = new HashMap<>();
        dsTrangThai.put(1, "Hoạt động");
        dsTrangThai.put(0, "Không hoạt động");
        return dsTrangThai;
    }

    @GetMapping("/hinh-anh")
    public String dsHinhAnh(Model model) {
        List<HinhAnh> hinhAnh = hinhAnhService.getAllHinhAnh();
        model.addAttribute("hinhAnh", hinhAnh);
        return "manage/hinh-anh";
    }

    @GetMapping("/hinh-anh/viewAdd")
    public String viewAddHinhAnh(Model model) {
        model.addAttribute("hinhAnh", new HinhAnh());
        return "manage/add-hinh-anh";
    }

    @PostMapping("/hinh-anh/viewAdd/add")
    public String addHinhAnh(@ModelAttribute("hinhAnh") HinhAnh hinhAnh) {
        hinhAnhService.save(hinhAnh);
        return "redirect:/hinh-anh";
    }

    @GetMapping("/hinh-anh/delete/{id}")
    public String deleteHinhAnh(@PathVariable UUID id) {
        hinhAnhService.deleteByIdHinhAnh(id);
        return "redirect:/hinh-anh";
    }

    @GetMapping("/hinh-anh/viewUpdate/{id}")
    public String viewUpdateHinhAnh(@PathVariable UUID id, Model model) {
        HinhAnh hinhAnh = hinhAnhService.getByIdHinhAnh(id);
        model.addAttribute("hinhAnh", hinhAnh);
        return "manage/update-hinh-anh";
    }

    @PostMapping("/hinh-anh/viewUpdate/{id}")
    public String updateHinhAnh(@PathVariable UUID id, @ModelAttribute("hinhAnh") HinhAnh hinhAnh) {
        HinhAnh hinhAnhDb = hinhAnhService.getByIdHinhAnh(id);
        if (hinhAnhDb != null) {
            hinhAnhDb.setTgSua(hinhAnh.getTgSua());
            hinhAnhDb.setTgThem(hinhAnh.getTgThem());
            hinhAnhDb.setTrangThai(hinhAnh.getTrangThai());
            hinhAnhDb.setUrl1(hinhAnh.getUrl1());
            hinhAnhDb.setUrl2(hinhAnh.getUrl2());
            hinhAnhDb.setUrl3(hinhAnh.getUrl3());
            hinhAnhDb.setUrl4(hinhAnh.getUrl4());
            hinhAnhService.save(hinhAnhDb);
        }
        return "redirect:/hinh-anh";
    }
}
