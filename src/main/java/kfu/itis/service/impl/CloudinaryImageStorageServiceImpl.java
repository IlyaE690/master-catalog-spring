package kfu.itis.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kfu.itis.service.ImageStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@Service
public class CloudinaryImageStorageServiceImpl implements ImageStorageService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryImageStorageServiceImpl.class);

    private Cloudinary cloudinary;

    @Value("${app.cloudinary.cloud-name:}")
    private String cloudName;

    @Value("${app.cloudinary.api-key:}")
    private String apiKey;

    @Value("${app.cloudinary.api-secret:}")
    private String apiSecret;

    @Value("${app.cloudinary.upload-preset:}")
    private String uploadPreset;

    @PostConstruct
    public void init() {
        if (cloudName != null && !cloudName.isBlank() &&
                apiKey != null && !apiKey.isBlank() &&
                apiSecret != null && !apiSecret.isBlank()) {

            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret,
                    "secure", true
            ));
            log.info("Cloudinary initialized: {}", cloudName);
        }
    }

    @Override
    public String uploadOrderImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            Map<String, Object> params = ObjectUtils.asMap(
                    "folder", "household_orders",
                    "upload_preset", uploadPreset
            );

            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), params);
            return (String) result.get("secure_url");

        } catch (Exception e) {
            log.error("Upload failed", e);
            return null;
        }
    }
}