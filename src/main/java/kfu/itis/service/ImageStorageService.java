package kfu.itis.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    String uploadOrderImage(MultipartFile file);
}
