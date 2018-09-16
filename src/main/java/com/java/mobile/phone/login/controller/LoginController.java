package com.java.mobile.phone.login.controller;

import com.java.mobile.phone.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/1
 */
@RestController
@CrossOrigin
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

//    @PostMapping("login")
    @RequestMapping("login")
    public void login(@RequestParam("username") String username, @RequestParam("password") String password,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("账号：{}，密码：{}", username, password);
        Map<String, Object> user = userService.getByUsername(username);
        if (user == null) {
            request.setAttribute("errMsg", "用户不存在");
            request.getRequestDispatcher("/index.jsp").forward(request,response);
        } else {
            String pwd = String.valueOf(user.get("password"));
            if (pwd.equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("userInfo", user);
                session.setAttribute("userId", user.get("id"));
                request.getRequestDispatcher("/WEB-INF/page/lock/home.jsp").forward(request,response);
            } else {
                request.setAttribute("errMsg", "密码错误");
                request.getRequestDispatcher("/index.jsp").forward(request,response);
            }
        }
    }

    @RequestMapping("index")
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/index.jsp").forward(request,response);
    }

    @RequestMapping("send")
    public String send(@RequestParam("send") String send) {
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 9090);// 建立TCP服务,连接本机的TCP服务器
             InputStream inputStream2 = socket.getInputStream();// 获得输入流
             OutputStream outputStream = socket.getOutputStream()) {
            // 写入数据
            outputStream.write(send.getBytes());
            byte[] buf = new byte[1024];
            int len = inputStream2.read(buf);
            System.out.println(new String(buf, 0, len));
            //关闭资源
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "OK";
    }
}
