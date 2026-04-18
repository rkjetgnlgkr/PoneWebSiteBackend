package com.pone.website.service.impl;

import com.pone.website.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> paths = new ArrayList<>();

        // 確保上傳目錄存在
        String absolutePath = System.getProperty("user.dir") + "/" + uploadPath;
        File dir = new File(absolutePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + extension;

            File dest = new File(absolutePath + newFilename);
            try {
                file.transferTo(dest);
                paths.add(urlPrefix + newFilename);
            } catch (IOException e) {
                throw new RuntimeException("檔案上傳失敗: " + originalFilename, e);
            }
        }

        return paths;
    }
}
