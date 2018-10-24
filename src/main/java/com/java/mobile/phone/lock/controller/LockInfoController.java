package com.java.mobile.phone.lock.controller;

import com.github.pagehelper.PageInfo;
import com.java.mobile.common.utils.ExcelUtil;
import com.java.mobile.common.vo.Result;
import com.java.mobile.phone.lock.service.LockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/10/23
 */
@RestController
@RequestMapping("lockInfo")
@CrossOrigin
public class LockInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockInfoController.class);

    @Autowired
    private LockInfoService lockInfoService;

    @RequestMapping("pageByLockInfo")
    public Result pageByLockInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        final Object userId = session.getAttribute("userId");
        PageInfo<Map<String, Object>> res = lockInfoService.pageByLockInfo(params);
        return new Result(100, res);
    }

    @RequestMapping("importLockInfoData")
    public Result importLockInfoData(@RequestParam("file") MultipartFile file, HttpSession session){
        try {
            final Object userId = session.getAttribute("userId");
            ExcelUtil excelUtil = new ExcelUtil(file.getInputStream(), "2007");
            List<List<String>> res = excelUtil.read(0);
            LOGGER.info("导入锁数据参数：{}：{}", userId, excelUtil);
            List<List<String>> ret = lockInfoService.importLockInfoData(res);
            LOGGER.info("导入锁数据失败返回：{}：{}", userId, excelUtil);
            return new Result(100, ret);
        } catch (Exception e) {
            LOGGER.error("导入锁数据异常：" + e.getMessage(), e);
            return new Result(500);
        }
    }




}
