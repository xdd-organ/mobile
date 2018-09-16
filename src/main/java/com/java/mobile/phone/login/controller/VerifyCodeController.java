package com.java.mobile.phone.login.controller;

import com.java.mobile.common.utils.VerifyCodeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xdd
 * @date 2018/8/1
 */
@Controller
@CrossOrigin
public class VerifyCodeController {

    @RequestMapping("anon/createVerifyCode")
    public void createVerifyCode(@RequestParam(value = "width", defaultValue = "200") int width,
                                 @RequestParam(value = "weight", defaultValue = "80") int weight,
                                 @RequestParam(value = "verifySize", defaultValue = "4") int verifySize,
                                 @RequestParam(value = "key", defaultValue = "") String key,
                                 HttpServletResponse response) {
        try (ServletOutputStream outputStream = response.getOutputStream()){
            String verifyCode = VerifyCodeUtils.outputVerifyImage(width, weight, outputStream, verifySize);
        } catch (Exception e) {

        }
    }
}
