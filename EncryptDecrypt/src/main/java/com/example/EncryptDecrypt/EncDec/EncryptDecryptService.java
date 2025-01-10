package com.example.EncryptDecrypt.EncDec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

@Service
public class EncryptDecryptService {

    @Autowired
    private EncryptDecryptRepository encryptDecryptRepository;

    // Görseli şifreleme ve Base64 olarak döndürme
    public String encryptImageToBase64(byte[] imageBytes, String imageName) throws Exception {
        SecretKey key = generateSecretKey();
        byte[] iv = generateIv();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] encryptedImage = cipher.doFinal(imageBytes);
        String base64Encoded = Base64.getEncoder().encodeToString(encryptedImage);

        EncryptDecrypt encryptDecryptEntity = new EncryptDecrypt();
        encryptDecryptEntity.setImageName(imageName);
        encryptDecryptEntity.setEncryptedImage(encryptedImage);
        encryptDecryptEntity.setIv(iv);
        encryptDecryptEntity.setKey(key.getEncoded());
        encryptDecryptRepository.save(encryptDecryptEntity);

        return base64Encoded;
    }

    // Base64 formatındaki şifreli veriyi çözerek görsel döndürme
    public byte[] decryptBase64ToImage(String base64Content) throws Exception {
        EncryptDecrypt encryptDecrypt = encryptDecryptRepository.findFirstByOrderByIdDesc();

        byte[] encryptedData = Base64.getDecoder().decode(base64Content);
        byte[] iv = encryptDecrypt.getIv();
        byte[] key = encryptDecrypt.getKey();

        SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        return cipher.doFinal(encryptedData);
    }

    private SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    private byte[] generateIv() {
        byte[] iv = new byte[16]; // 128-bit IV
        new java.security.SecureRandom().nextBytes(iv);
        return iv;
    }
}