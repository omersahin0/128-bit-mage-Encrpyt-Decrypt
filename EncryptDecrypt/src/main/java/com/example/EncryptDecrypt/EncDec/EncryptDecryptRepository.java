package com.example.EncryptDecrypt.EncDec;

import com.example.EncryptDecrypt.EncDec.EncryptDecrypt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EncryptDecryptRepository extends JpaRepository<EncryptDecrypt, Long> {
    EncryptDecrypt findFirstByOrderByIdDesc();
}