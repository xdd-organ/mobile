package com.java.mobile.phone.user.service;

import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/21
 */
public interface UserService {

    Map<String, Object> getByUsername(String username);

    Map<String, Object> getByUserId(Map<String,Object> params);
}
