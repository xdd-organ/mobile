package com.java.mobile.common.service.impl;

import com.java.mobile.common.mapper.FileMapper;
import com.java.mobile.common.service.FileService;
import com.java.mobile.common.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/7/26
 */
@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${imgPath:}")
    private String imgPath;
    @Value("${imgBaseUrl:}")
    private String imgBaseUrl;

    @Autowired
    private FileMapper fileMapper;

    @Override
    public List<Map<String, Object>> upload(MultipartFile[] files) {
        List<Map<String, Object>> params = null;
        try {
            params = new ArrayList<>();
            for (MultipartFile file : files) {
                String filename = file.getOriginalFilename();
                long size = file.getSize()/1000; //KB
                String suffix = filename.substring(filename.lastIndexOf("."));
                String filePath = imgPath + File.separator + DateUtil.getDateyyyyMMdd();
                File saveFile = new File(filePath);
                if (!saveFile.exists()) saveFile.mkdirs();
                filePath = filePath + File.separator + DateUtil.getDateyyyyMMddHHmmssSSSS() + suffix;
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(filePath));

                Map<String, Object> param = new HashMap<>();
                String imgBasePath = DateUtil.getDateyyyyMMdd()+ "/" + DateUtil.getDateyyyyMMddHHmmssSSSS() + suffix;
                param.put("name", imgBasePath);
                param.put("file_name", filename);
                param.put("size", size);
                param.put("url", imgBaseUrl + imgBasePath);
                params.add(param);
            }
            int ret = fileMapper.upload(params);
            logger.info(ret +"");
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return params;
    }
}
