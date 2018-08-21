package com.java.mobile.phone.pay.controller;

import com.java.mobile.phone.pay.service.WeixinPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/21
 */
@RestController
@RequestMapping("pay/wx")
public class WeixinPayController {

    private Logger logger = LoggerFactory.getLogger(WeixinPayController.class);

    @Autowired
    private WeixinPayService weixinPayService;



    @RequestMapping("prepay")
    public String pay(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> prepay = weixinPayService.prepay(params);








        return null;
    }




}
