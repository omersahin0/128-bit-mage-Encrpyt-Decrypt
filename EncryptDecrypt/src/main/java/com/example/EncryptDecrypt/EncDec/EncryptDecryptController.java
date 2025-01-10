package com.example.EncryptDecrypt.EncDec;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@RestController
@RequestMapping("/encryptdecrypt")
public class EncryptDecryptController {

    @Autowired
    private EncryptDecryptService encryptDecryptService;

    // Şifreleme işlemi ve Base64 .txt olarak indirme
    @PostMapping("/encrypt")
    public void encryptImage(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        byte[] imageBytes = file.getBytes();
        String imageName = file.getOriginalFilename();
        String base64Encoded = encryptDecryptService.encryptImageToBase64(imageBytes, imageName);

        // Base64 çıktısını .txt olarak indirme
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=encrypted_image.txt");
        response.getOutputStream().write(base64Encoded.getBytes());
        response.getOutputStream().close();
    }

    // Base64 .txt dosyasını yükleyerek deşifreleme
    @PostMapping("/decrypt")
    public void decryptBase64Txt(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        String base64Content = new String(file.getBytes());
        byte[] decryptedImage = encryptDecryptService.decryptBase64ToImage(base64Content);

        response.setContentType("image/jpeg");
        response.getOutputStream().write(decryptedImage);
        response.getOutputStream().close();
    }

}