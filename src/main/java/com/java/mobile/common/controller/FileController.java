package com.java.mobile.common.controller;

import com.java.mobile.common.service.FileService;
import com.java.mobile.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
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
    public Result upload(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) {
        List<Map<String, Object>> rsp = fileService.upload(files);
        return new Result(100, rsp);
    }


    /**
     * 单文件下载
     *
     * @return
     */
    @RequestMapping(value = "/download/{key}")
    public void download(@PathVariable("key") String key, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> file = fileService.download(key);
        try (FileInputStream inputStream = new FileInputStream(String.valueOf(file.get("filePath")))) {
            String userAgent = request.getHeader("User-Agent").toLowerCase();
            String fileName = String.valueOf(file.get("file_name"));
            if (userAgent.contains("msie")) {//IE浏览器
                fileName = URLEncoder.encode(fileName, "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/octet-stream");
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
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