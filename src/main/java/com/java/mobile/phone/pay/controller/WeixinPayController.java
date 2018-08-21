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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public String prepay(@RequestBody Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> prepay = weixinPayService.prepay(params);








        return null;
    }
    @RequestMapping("payNotify")
    public String payNotify(HttpServletRequest request, HttpServletResponse response) {
        String requestParams = this.getRequestParams(request);




        return null;
    }

    /**
     * 从request中获取请求字符串(xml字符串)
     *
     * @param request
     * @return
     */
    private String getRequestParams(HttpServletRequest request) {
        String str = "";
        BufferedReader br = null;
        try {
            request.setCharacterEncoding("utf-8");
            InputStream inStream = request.getInputStream();
            br = new BufferedReader(new InputStreamReader(inStream));
            String s = null;
            while ((s = br.readLine()) != null) {
                str += s;
            }
        } catch (Exception e) {
            logger.error("解析报文过程失败" + e.getMessage(), e);
        } finally {

            if (br != null){

                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return str;
    }






}
