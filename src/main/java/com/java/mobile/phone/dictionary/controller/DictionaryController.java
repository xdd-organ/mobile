package com.java.mobile.phone.dictionary.controller;

import com.java.mobile.common.vo.Result;
import com.java.mobile.phone.dictionary.service.DictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dictionary")
@CrossOrigin
public class DictionaryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryController.class);

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping("insert")
    public Result insert(@RequestBody Map<String, Object> params, HttpSession session) {
        Object userId = session.getAttribute("userId");
        params.put("user_id", userId);
        LOGGER.info("保存数据字典參數：{}", params);
        long id = dictionaryService.insert(params);
        LOGGER.info("保存数据字典返回：{}", id);
        return new Result(100, id);
    }

    @RequestMapping("getByKey")
    public Result getByKey(@RequestBody Map<String, Object> params) {
        LOGGER.info("根据key查询数据字典参数：{}", params);
        String key = String.valueOf(params.get("key"));
        Map<String, Object> rsp = dictionaryService.getByKey(key);
        LOGGER.info("根据key查询数据字典返回：{}", rsp);
        return new Result(100, rsp);
    }

    @RequestMapping("updateByKey")
    public Result updateByKey(@RequestBody Map<String, Object> params, HttpSession session) {
        LOGGER.info("根据key更新数据字典参数：{}", params);
        Object userId = session.getAttribute("userId");
        params.put("user_id", userId);
        int rsp = dictionaryService.updateByKey(params);
        LOGGER.info("根据key更新数据字典返回：{}", rsp);
        return new Result(100, rsp);
    }

    @RequestMapping("listByDictionary")
    public Result listByDictionary(@RequestBody Map<String, Object> params) {
        LOGGER.info("根据条件查询数据字典参数：{}", params);
        List<Map<String, Object>> rsp = dictionaryService.listByDictionary(params);
        LOGGER.info("根据条件查询数据字典返回：{}", rsp);
        return new Result(100, rsp);
    }

}
