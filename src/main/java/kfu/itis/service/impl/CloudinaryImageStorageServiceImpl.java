package kfu.itis.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kfu.itis.service.ImageStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class CloudinaryImageStorageServiceImpl implements ImageStorageService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryImageStorageServiceImpl.class);

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    @Value("${app.cloudinary.cloud-name:}")
    private String cloudName;

    @Value("${app.cloudinary.upload-preset:}")
    private String uploadPreset;

    public CloudinaryImageStorageServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String uploadOrderImage(MultipartFile file) {
        if (file == null || file.isEmpty() || cloudName == null || cloudName.isBlank() || uploadPreset == null || uploadPreset.isBlank()) {
            return null;
        }
        try {
            String dataUri = "data:" + file.getContentType() + ";base64," + Base64.getEncoder().encodeToString(file.getBytes());
            String form = "upload_preset=" + URLEncoder.encode(uploadPreset, StandardCharsets.UTF_8)
                    + "&file=" + URLEncoder.encode(dataUri, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.cloudinary.com/v1_1/" + cloudName + "/image/upload"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            return root.path("secure_url").asText(null);
        } catch (Exception e) {
            log.error("Ошибка загрузки фото в Cloudinary", e);
            return null;
        }
    }
}
