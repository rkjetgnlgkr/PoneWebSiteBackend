package com.pone.website.controller;

import com.pone.website.service.FileService;
import com.pone.website.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping
    public Result<List<String>> upload(@RequestParam("files") List<MultipartFile> files) {
        List<String> paths = fileService.uploadFiles(files);
        return Result.success(paths);
    }
}
