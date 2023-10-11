package com.example.demo.repository;

import com.example.demo.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SizeRepository extends JpaRepository<Size, UUID> {

    List<Size> findByTrangThai(int trangThai);

    List<Size> findBySoSizeAndMaSize(Integer selectedSize,String maSize);
}
