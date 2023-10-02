package com.example.demo.controller;

import com.example.demo.model.ChatLieu;
import com.example.demo.service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Controller
public class ChatLieuController {
    @Autowired
    private ChatLieuService chatLieuService;

    @GetMapping("/chat-lieu")
    public String dsChatLieu(Model model) {
        List<ChatLieu> chatLieu = chatLieuService.getAllChatLieu();
        model.addAttribute("chatLieu", chatLieu);
        return "manage/chat-lieu";
    }

    @GetMapping("/chat-lieu/viewAdd")
    public String viewAddChatLieu(Model model) {
        model.addAttribute("chatLieu", new ChatLieu());
        return "manage/add-chat-lieu";
    }

    @PostMapping("/chat-lieu/viewAdd/add")
    public String addChatLieu(@ModelAttribute("chatLieu") ChatLieu chatLieu) {
        chatLieuService.save(chatLieu);
        return "redirect:/chat-lieu";
    }

    @GetMapping("/chat-lieu/delete/{id}")
    public String deleteChatLieu(@PathVariable UUID id) {
        chatLieuService.deleteByIdChatLieu(id);
        return "redirect:/chat-lieu";
    }

    @GetMapping("/chat-lieu/viewUpdate/{id}")
    public String viewUpdateChatLieu(@PathVariable UUID id, Model model) {
        ChatLieu chatLieu = chatLieuService.getByIdChatLieu(id);
        model.addAttribute("chatLieu", chatLieu);
        return "manage/update-chat-lieu";
    }

    @PostMapping("/chat-lieu/viewUpdate/{id}")
    public String updateChatLieu(@PathVariable UUID id, @ModelAttribute("chatLieu") ChatLieu chatLieu) {
        ChatLieu chatLieuDb = chatLieuService.getByIdChatLieu(id);
        if (chatLieuDb != null) {
            chatLieuDb.setMaChatLieu(chatLieu.getMaChatLieu());
            chatLieuDb.setTenChatLieu(chatLieu.getTenChatLieu());
            chatLieuDb.setTgSua(chatLieu.getTgSua());
            chatLieuDb.setTgThem(chatLieu.getTgThem());
            chatLieuDb.setTrangThai(chatLieu.getTrangThai());
            chatLieuService.save(chatLieuDb);
        }
        return "redirect:/chat-lieu";
    }
}
