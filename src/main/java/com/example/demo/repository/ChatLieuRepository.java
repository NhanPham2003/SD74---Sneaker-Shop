package com.example.demo.repository;

import com.example.demo.model.ChatLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ChatLieuRepository extends JpaRepository<ChatLieu, UUID> {
}
