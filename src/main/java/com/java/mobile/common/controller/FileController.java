package com.java.mobile.common.controller;

import com.java.mobile.common.service.FileService;
import com.java.mobile.common.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("file")
public class FileController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);
    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".gif", ".png"};

    @Autowired
    private FileService fileService;

    /**
     * 单/多文件普通上传
     * @return
     */
    @RequestMapping(value = "/upload")
    public List<Map<String, Object>> upload(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) {
        return fileService.upload(files);
    }


    /**
     * 单文件下载
     *
     * @return
     */
    @RequestMapping(value = "/downloadFile")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
        return "fileUpload2";
    }


    /**
     *
     *
     * @return
     */
    @RequestMapping(value = "/test1")
    public String test1(HttpServletRequest request, HttpServletResponse response) {
        return "common/file/fileUpload";
    }


    /**
     * 单文件分片上传
     *
     * @return
     */
    @RequestMapping(value = "/uploadPart")
    public String uploadPart(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            InputStream inputStream = file.getInputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
        return "上传成功！";
    }

    /**
     * 单文件分片上传
     *
     * @return
     */
    @RequestMapping(value = "/uploadMorePart")
    public String uploadMorePart(@RequestParam("file") MultipartFile[] file, HttpServletRequest request) {
        try {
            System.out.println("多文件上传");
        } catch (Exception e){
            e.printStackTrace();
        }
        return "上传成功！";
    }
}