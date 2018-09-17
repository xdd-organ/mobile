package com.java.mobile.phone.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.mobile.common.service.RedisService;
import com.java.mobile.common.utils.httpclient.HttpClientUtil;
import com.java.mobile.common.utils.httpclient.HttpResult;
import com.java.mobile.common.vo.Result;
import com.java.mobile.common.weixin.WeixinDecrypt;
import com.java.mobile.phone.pay.constant.PayConstant;
import com.java.mobile.phone.user.service.TransFlowInfoService;
import com.java.mobile.phone.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.VariableElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/21
 */
@RequestMapping("user")
@RestController
@CrossOrigin
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private TransFlowInfoService transFlowInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private HttpClientUtil httpClientUtil;
    @Value("${appid:}")
    private String appid;
    @Value("${appSecret:}")
    private String appSecret;

    @RequestMapping("pageByTransFlowInfo")
    public Result pageByTransFlowInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        logger.info("分页查询流水参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        params.put("user_id", userId);
        PageInfo pageInfo = transFlowInfoService.pageByTransFlowInfo(params);
        logger.info("分页查询流水返回：{},userId:{}", JSONObject.toJSONString(pageInfo));
        return new Result(100, pageInfo);
    }

    @RequestMapping("recharge")//充值
    public Result recharge(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        logger.info("充值参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        params.put("user_id", userId);
        return new Result(100, null);
    }

    @RequestMapping("deposit")//押金
    public Result deposit(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        logger.info("缴纳押金参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        params.put("user_id", userId);
        return new Result(100, null);
    }

    @RequestMapping("getUserInfo")//押金
    public Result getUserInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        logger.info("获取用户信息参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        params.put("user_id", userId);
        Map<String, Object> user = userService.getByUserId(userId.toString());
        logger.info("获取用户信息返回：{}", JSONObject.toJSONString(user));
        return new Result(100, user);
    }

    @RequestMapping("getUserByUserId")//
    public Result getUserByUserId(@RequestBody Map<String, Object> params) {
        logger.info("获取用户信息参数：{},userId:{}", JSONObject.toJSONString(params));
        Object userId = params.get("user_id");
        Map<String, Object> user = userService.getByUserId(userId.toString());
        logger.info("获取用户信息返回：{}", JSONObject.toJSONString(user));
        return new Result(100, user);
    }

    @RequestMapping("bindPhone")//绑定手机
    public Result bindPhone(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        logger.info("绑定手机参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        String userVerifyCode = params.remove("verifyCode").toString();
        String verifyCode = redisService.get(params.remove("randomKey").toString());
        logger.info("用户验证码：{}，redis验证码:{}", userVerifyCode, verifyCode);
        if (!userVerifyCode.equals(verifyCode)) return new Result(500, "验证码错误", null);
        params.put("user_id", userId);
        int i = userService.updateByUserId(params);
        logger.info("绑定手机返回：{}", i);
        return new Result(100);
    }

    @RequestMapping("bindWeixinInfo")//绑定微信信息
    public Result bindWeixinInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        logger.info("绑定微信信息参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        String code = params.get("code").toString();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
        try {
            logger.info("发送到微信获取openid参数：{}", url);
            HttpResult httpResult = httpClientUtil.doGet(url, null, null);
            String body = httpResult.getBody();
            logger.info("调用接口jscode2session，返回code：{}：body:{}", httpResult.getCode(),body);
            if (body != null && body.contains(PayConstant.OPENID)) {
                Map<String, String> bodyMap = JSONObject.parseObject(body, Map.class);
                String encrypteData = String.valueOf(params.get("encryptedData"));
                String iv = String.valueOf(params.get("iv"));
                String openId = String.valueOf(params.get("openId"));
                String sessionKey = bodyMap.get("session_key");
                String decrypt = WeixinDecrypt.decrypt(appid, encrypteData, sessionKey, iv);
                logger.info("解密encrypteData结果：{}", decrypt);
                Map<String, String> decryptMap = JSONObject.parseObject(decrypt, Map.class);

                Map<String, Object> userParams = new HashMap<>();
                userParams.put("user_id", userId);
                userParams.put("openid", bodyMap.get("openid"));
                userParams.put("unionid", bodyMap.get("unionid"));
                userParams.put("nickname", decryptMap.get("nickName"));
                userParams.put("gender", decryptMap.get("gender"));
                userParams.put("avatar", decryptMap.get("avatarUrl"));
                userParams.put("phone", decryptMap.get("phoneNumber"));
                logger.info("更新用户信息参数：{}", JSONObject.toJSONString(userParams));
                int i = userService.updateByUserId(userParams);
                logger.info("更新用户信息结果：{}", i);
                return new Result(100, userService.getByUserId(userId.toString()));
            }
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        }
        return new Result(500);
    }

    @RequestMapping("updateMoney")
    public Result updateMoney(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userId = session.getAttribute("userId").toString();
        String money = params.get("money").toString();
        Long i = transFlowInfoService.insert(null, money, "0", "", "0", userId);
        if (i != null) {
            return new Result(100);
        } else {
            return new Result(500);
        }
    }

    @RequestMapping("pageByUser")
    public Result pageByUser(@RequestBody Map<String, Object> params) {
        logger.info("分页查询用户参数：{}", params);
        PageInfo pageInfo = userService.pageByUser(params);
        logger.info("分页查询用户返回：{}", pageInfo);
        return new Result(100, pageInfo);
    }

    @RequestMapping("login")
    public Result login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        logger.info("账号：{}，密码：{}", username, password);
        Map<String, Object> user = userService.getByUsername(username);
        if (user == null) {
            return new Result(500, "用户名或密码错误");
        } else {
            String pwd = String.valueOf(user.remove("password"));
            if (pwd.equalsIgnoreCase(password)) {
                return new Result(100, user);
            } else {
                return new Result(500, "用户名或密码错误");
            }
        }
    }


}
