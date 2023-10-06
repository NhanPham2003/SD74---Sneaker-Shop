package com.example.demo.controller;

import com.example.demo.model.ChiTietGiay;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/manage")
@Controller
public class GiayChiTietController {
    @Autowired
    private GiayChiTietService giayChiTietService;
    @Autowired
    private GiayService giayService;
    @Autowired
    private SizeService sizeService;
    @Autowired
    private MauSacService mauSacService;
    @Autowired
    private HinhAnhService hinhAnhService;

    @ModelAttribute("dsTrangThai")
    public Map<Integer, String> getDsTrangThai() {
        Map<Integer, String> dsTrangThai = new HashMap<>();
        dsTrangThai.put(1, "Hoạt động");
        dsTrangThai.put(0, "Không hoạt động");
        return dsTrangThai;
    }

    @GetMapping("/giay-chi-tiet")
    public String dsGiayChiTiet(Model model) {
        List<ChiTietGiay> items = giayChiTietService.getAllChiTietGiay();
        model.addAttribute("items", items);
        return "manage/giay-chi-tiet";
    }

    @GetMapping("/giay-chi-tiet/detail/{id}")
    public String detailGiayChiTiet(@PathVariable UUID id, Model model) {
        ChiTietGiay chiTietGiay = giayChiTietService.getByIdChiTietGiay(id);
        model.addAttribute("giayChiTietDetail", chiTietGiay);
        return "manage/giay-chi-tiet-detail";
    }

    @GetMapping("/giay-chi-tiet/viewAdd")
    public String viewAddGiayChiTiet(Model model) {
        model.addAttribute("giayChiTiet", new ChiTietGiay());
        model.addAttribute("giay", giayService.getAllGiay());
        model.addAttribute("mauSac", mauSacService.getALlMauSac());
        model.addAttribute("size", sizeService.getAllSize());
        model.addAttribute("hinhAnh", hinhAnhService.getAllHinhAnh());
        return "manage/add-giay-chi-tiet";
    }

    @PostMapping("/giay-chi-tiet/viewAdd/add")
    public String addGiayChiTiet(@ModelAttribute("giayChiTiet") ChiTietGiay chiTietGiay) {
        ChiTietGiay chiTietGiay1 = new ChiTietGiay();
        chiTietGiay1.setGiay(chiTietGiay.getGiay());
        chiTietGiay1.setNamSX(chiTietGiay.getNamSX());
        chiTietGiay1.setNamBH(chiTietGiay.getNamBH());
        chiTietGiay1.setTrongLuong(chiTietGiay.getTrongLuong());
        chiTietGiay1.setGiaBan(chiTietGiay.getGiaBan());
        chiTietGiay1.setSoLuong(chiTietGiay.getSoLuong());
        chiTietGiay1.setTrangThai(chiTietGiay.getTrangThai());
        chiTietGiay1.setMauSac(chiTietGiay.getMauSac());
        chiTietGiay1.setHinhAnh(chiTietGiay.getHinhAnh());
        chiTietGiay1.setSize(chiTietGiay.getSize());
        chiTietGiay1.setTgThem(new Date());
        giayChiTietService.save(chiTietGiay1);
        return "redirect:/manage/giay-chi-tiet";
    }

    @GetMapping("/giay-chi-tiet/delete/{id}")
    public String deleteGiayChiTiet(@PathVariable UUID id) {
        ChiTietGiay chiTietGiay = giayChiTietService.getByIdChiTietGiay(id);
        chiTietGiay.setTrangThai(0);
        chiTietGiay.setTgSua(new Date());
        giayChiTietService.save(chiTietGiay);
        return "redirect:/manage/giay-chi-tiet";
    }

    @GetMapping("/giay-chi-tiet/viewUpdate/{id}")
    public String viewUpdateGiayChiTiet(@PathVariable UUID id, Model model) {
        ChiTietGiay chiTietGiay = giayChiTietService.getByIdChiTietGiay(id);
        model.addAttribute("giayChiTiet", chiTietGiay);
        model.addAttribute("giay", giayService.getAllGiay());
        model.addAttribute("mauSac", mauSacService.getALlMauSac());
        model.addAttribute("size", sizeService.getAllSize());
        model.addAttribute("hinhAnh", hinhAnhService.getAllHinhAnh());
        return "manage/update-giay-chi-tiet";
    }

    @PostMapping("/giay-chi-tiet/viewUpdate/{id}")
    public String updateGiayChiTiet(@PathVariable UUID id, @ModelAttribute("giayChiTiet") ChiTietGiay chiTietGiay) {
        ChiTietGiay chiTietGiayDb = giayChiTietService.getByIdChiTietGiay(id);
        if (chiTietGiayDb != null) {
            chiTietGiayDb.setGiay(chiTietGiay.getGiay());
            chiTietGiayDb.setHinhAnh(chiTietGiay.getHinhAnh());
            chiTietGiayDb.setMauSac(chiTietGiay.getMauSac());
            chiTietGiayDb.setSize(chiTietGiay.getSize());
            chiTietGiayDb.setGiaBan(chiTietGiay.getGiaBan());
            chiTietGiayDb.setMaNVSua(chiTietGiay.getMaNVSua());
            chiTietGiayDb.setNamBH(chiTietGiay.getNamBH());
            chiTietGiayDb.setNamSX(chiTietGiay.getNamSX());
            chiTietGiayDb.setSoLuong(chiTietGiay.getSoLuong());
            chiTietGiayDb.setTgSua(new Date());
            chiTietGiayDb.setTrangThai(chiTietGiay.getTrangThai());
            chiTietGiayDb.setTrongLuong(chiTietGiay.getTrongLuong());
            giayChiTietService.save(chiTietGiayDb);
        }
        return "redirect:/manage/giay-chi-tiet";
    }
}
