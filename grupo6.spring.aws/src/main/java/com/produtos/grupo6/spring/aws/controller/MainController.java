package com.produtos.grupo6.spring.aws.controller;

import java.net.URI;

import com.produtos.grupo6.spring.aws.util.KafkaUtil;
import com.produtos.grupo6.spring.aws.util.S3Util;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {

    @GetMapping("/")
    public String viewHomePage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleUploadForm(Model model, String description,
            @RequestParam("file") MultipartFile multipart) {
        String fileName = multipart.getOriginalFilename();

        System.out.println("Description: " + description);
        System.out.println("filename: " + fileName);

        String message = "";

        try {
            URI res = S3Util.uploadFile(fileName, multipart.getInputStream());
            message = res.toString();
            KafkaUtil.sendMessage(fileName, message);
        } catch (Exception ex) {
            message = "Error uploading file: " + ex.getMessage();
        }

        model.addAttribute("message", message);

        return "message";
    }
}
