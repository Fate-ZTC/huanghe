package com.parkbobo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by lijunhong on 18/3/29.
 */
@Controller
@RequestMapping("/upload")
public class FileUpload {

    @RequestMapping("/test")
    public void upload(@RequestParam("file")MultipartFile file) {
        System.out.println(file);
    }
}
