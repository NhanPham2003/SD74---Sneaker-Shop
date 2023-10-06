package com.example.demo.service;

import com.example.demo.model.ChatLieu;
import com.example.demo.model.Giay;

import java.util.List;
import java.util.UUID;

public interface GiayService {
    public List<Giay> getAllGiay();

    public void save(Giay giay);

    public void deleteByIdGiay(UUID id);

    public Giay getByIdGiay(UUID id);

    public Giay getByName(String name);
}
