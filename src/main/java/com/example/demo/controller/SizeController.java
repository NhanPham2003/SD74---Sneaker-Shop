package com.example.demo.controller;

import com.example.demo.model.Size;
import com.example.demo.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/manage")
@Controller
public class SizeController {
    @Autowired
    private SizeService sizeService;

    @ModelAttribute("dsTrangThai")
    public Map<Integer, String> getDsTrangThai() {
        Map<Integer, String> dsTrangThai = new HashMap<>();
        dsTrangThai.put(1, "Hoạt động");
        dsTrangThai.put(0, "Không hoạt động");
        return dsTrangThai;
    }

    @ModelAttribute("currentTime")
    public Date getCurrentTime() {
        return new Date();
    }

    @GetMapping("/size")
    public String dsSize(Model model) {
        List<Size> size = sizeService.getAllSize();
        model.addAttribute("size", size);
        return "manage/size-giay";
    }

    @GetMapping("/size/viewAdd")
    public String viewAddSize(Model model) {
        model.addAttribute("size", new Size());
        return "manage/add-size";
    }

    @PostMapping("/size/viewAdd/add")
    public String addSize(@ModelAttribute("size") Size size) {
        Size sizeAdd = new Size();
        sizeAdd.setMaSize(size.getMaSize());
        sizeAdd.setTenSize(size.getTenSize());
        sizeAdd.setTgThem(new Date());
        sizeAdd.setTrangThai(size.getTrangThai());
        sizeService.save(sizeAdd);
        return "redirect:/manage/size";
    }

    @GetMapping("/size/delete/{id}")
    public String deleteSize(@PathVariable UUID id) {
        sizeService.deleteByIdSize(id);
        return "redirect:/manage/size";
    }

    @GetMapping("/size/viewUpdate/{id}")
    public String viewUpdateSize(@PathVariable UUID id, Model model) {
        Size size = sizeService.getByIdSize(id);
        model.addAttribute("size", size);
        return "manage/update-size";
    }

    @PostMapping("/size/viewUpdate/{id}")
    public String updateSize(@PathVariable UUID id, @ModelAttribute("size") Size size) {
        Size sizeDb = sizeService.getByIdSize(id);
        if (sizeDb != null) {
            sizeDb.setMaSize(size.getMaSize());
            sizeDb.setTenSize(size.getTenSize());
            sizeDb.setTgSua(new Date());
            sizeDb.setTrangThai(size.getTrangThai());
            sizeService.save(sizeDb);
        }
        return "redirect:/manage/size";
    }
}