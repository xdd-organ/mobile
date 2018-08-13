package com.java.mobile.phone.login.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/1
 */
@RestController
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

//    @PostMapping("login")
    @RequestMapping("login")
    public Map<String, Object> login() {
        logger.debug("------ DEBUG -------------");
        logger.info("------- INFO -------------");
        logger.warn("-------- WARN -------------");
        logger.error("------- ERROR -------------");
        return null;
    }
}
