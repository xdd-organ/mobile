package com.java.mobile.common.controller;

import com.java.mobile.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ExceptionHandlerController {

   private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

   /**
   * 用于处理异常的
   * @return
   */
   @ExceptionHandler({Exception.class})
   public Result exception(Exception e) {
       LOGGER.error("异常：" + e.getMessage(), e);
       return new Result(500);
   }
}