package com.java.mobile.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/7/26
 */
public interface FileService {
    /**
     * 文件上传，支持多文件上传
     * @param file
     * @return
     */
    List<Map<String,Object>> upload(MultipartFile[] file);

    /**
     * 文件下载
     * @param key
     * @return
     */
    Map<String, Object> download(String key);
}
