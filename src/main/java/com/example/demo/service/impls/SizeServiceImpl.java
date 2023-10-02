package com.example.demo.service.impls;

import com.example.demo.model.Size;
import com.example.demo.repository.SizeRepository;
import com.example.demo.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SizeServiceImpl implements SizeService {
    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<Size> getAllSize() {
        return sizeRepository.findAll();
    }

    @Override
    public void save(Size size) {
        sizeRepository.save(size);
    }

    @Override
    public void deleteByIdSize(UUID id) {
        sizeRepository.deleteById(id);
    }

    @Override
    public Size getByIdSize(UUID id) {
        return sizeRepository.findById(id).orElse(null);
    }
}
