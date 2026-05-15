package com.pone.website.controller;

import com.pone.website.entity.User;
import com.pone.website.service.FileService;
import com.pone.website.service.UserProfileService;
import com.pone.website.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private FileService fileService;

    @GetMapping
    public Result<User> get(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userProfileService.getProfile(userId));
    }

    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                       HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<String> paths = fileService.uploadFiles(List.of(file));
        String avatarPath = paths.get(0);
        userProfileService.updateAvatar(userId, avatarPath);
        return Result.success(avatarPath);
    }
}
