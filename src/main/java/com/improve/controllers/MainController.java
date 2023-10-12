package com.improve.controllers;

import com.improve.models.Album;
import com.improve.repos.AlbumRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private AlbumRepo repo;
    @GetMapping("/")
    public String main(Model model){
        model.addAttribute("albums", repo.findAll());
        return "main";
    }
    @GetMapping("/add")
    public String addAlbum(){
        return "add";
    }
    @PostMapping("/add")
    public String addAlbum (@ModelAttribute Album album,
                          @RequestParam("files") MultipartFile[] files) throws IOException {
        List<String> encodedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            encodedImages.add(Base64.getEncoder().encodeToString(file.getBytes()));
        }
        album.setImages(encodedImages);
        repo.save(album);
        return "redirect:/";
    }
    @GetMapping("{id}")
    public String viewAlbum(@PathVariable Long id, Model model){
        model.addAttribute("album",repo.findById(id).orElseThrow());
        return "album";
    }
    @PostMapping("{id}/delete")
    public String deleteAlbum(@PathVariable Long id){
        repo.delete(repo.findById(id).orElseThrow());
        return "redirect:/";
    }
    @GetMapping("{id}/update")
    public String updateAlbum(@PathVariable Long id, Model model) {
        model.addAttribute("album", repo.findById(id).orElseThrow());
        return "update";
    }
    @PostMapping("{id}/update")
    public String updateAlbum (@PathVariable Long id,
                               @RequestParam("files") MultipartFile[] files) throws IOException {
        List<String> encodedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            encodedImages.add(Base64.getEncoder().encodeToString(file.getBytes()));
        }
        Album album = repo.findById(id).orElseThrow();
        album.setImages(encodedImages);
        repo.save(album);
        return "redirect:/{id}";
    }
}