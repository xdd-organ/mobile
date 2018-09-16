package com.java.mobile.phone.user.controller;

import com.github.pagehelper.PageInfo;
import com.java.mobile.common.vo.Result;
import com.java.mobile.phone.user.service.FaultFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("faultFeedback")
@CrossOrigin
public class FaultFeedbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FaultFeedbackController.class);

    @Autowired
    private FaultFeedbackService faultFeedbackService;

    @RequestMapping("save")
    public Result save(@RequestBody Map<String, Object> params, HttpSession session) {
        params.put("user_id", session.getAttribute("userId"));
        LOGGER.info("保存用户故障反馈，参数：{}", params);
        Long insert = faultFeedbackService.insert(params);
        LOGGER.info("保存用户故障反馈，结果：{}", insert);
        return new Result(100, insert);
    }

    @RequestMapping("update")
    public Result update(@RequestBody Map<String, Object> params, HttpSession session) {
        params.put("update_author", session.getAttribute("userId"));
        LOGGER.info("更新用户故障反馈，参数：{}", params);
        int insert = faultFeedbackService.update(params);
        LOGGER.info("更新用户故障反馈，结果：{}", insert);
        return new Result(100, insert);
    }

    @RequestMapping("pageByFaultFeedback")
    public Result pageByFaultFeedback(@RequestBody Map<String, Object> params) {
        LOGGER.info("分页查询用户故障反馈，参数：{}", params);
        PageInfo<Map<String, Object>> page = faultFeedbackService.pageByFaultFeedback(params);
        LOGGER.info("分页查询用户故障反馈，结果：{}", page);
        return new Result(100, page);
    }


}
