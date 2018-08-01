package com.java.mobile.phone.login.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/1
 */
@RestController
public class LoginController {

    @PostMapping("login")
    public Map<String, Object> login() {
        return null;
    }
}
