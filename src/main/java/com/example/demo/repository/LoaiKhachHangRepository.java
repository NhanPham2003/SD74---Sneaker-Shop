package com.example.demo.repository;

import com.example.demo.model.LoaiKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoaiKhachHangRepository extends JpaRepository<LoaiKhachHang, UUID> {
    List<LoaiKhachHang> getByTrangThai(int trangThai);
    List<LoaiKhachHang> findByMaLKHOrTenLKH(String maLKH, String tenLKH);

    LoaiKhachHang findByMaLKH(String maLKH);
    LoaiKhachHang findByTenLKH(String name);

}
