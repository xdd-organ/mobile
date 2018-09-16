package com.java.mobile.common.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.java.mobile.common.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成二维码
 */
@Controller
@RequestMapping("anon/qrcode")
@CrossOrigin
public class QrcodeController {

    private Logger logger = LoggerFactory.getLogger(QrcodeController.class);

    /**
     * 生成二维码
     *
     * @param request
     * @param response
     * @param height
     * @param width
     * @param content
     * @return
     * @throws Exception
     */
    @RequestMapping("qrcode")
    @ResponseBody
    public Map<String, Object> qrcode(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestParam(value = "height", defaultValue = "200", required = false) Integer height,
                                       @RequestParam(value = "width", defaultValue = "200", required = false) Integer width,
                                       @RequestParam(value = "content", defaultValue = "二维码内容", required = false) String content) throws Exception {
        /** 定义Map集合封装二维码需要全局配置信息 */
        Map<EncodeHintType, Object> hints = new HashMap<>();
        /** 设置二维码图片中内容编码 */
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        /** 设置二维码图片上下左右的边距 */
        hints.put(EncodeHintType.MARGIN, 0);
        /** 设置二维码的纠错级别 */
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        /**
         * 创建二维码字节转换对象
         * 第一个参数：二维码图片中的内容
         * 第二个参数：二维码的格式器
         * 第三个参数：二维码的宽度
         * 第四个参数：二维码的高度
         * 第五个参数：二维码的全局配置信息
         */

        content = content.replace("{key}", "key=");
        content = content.replace("{value}", "&value=");
        content = CommonConstant.BASEURL + content;

        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        /** 获取二维码实际宽度 */
        int matrixWidth = matrix.getWidth();
        /** 获取二维码实际高度 */
        int matrixHeight = matrix.getHeight();

        /** 创建缓冲流图片(空白) */
        BufferedImage image = new BufferedImage(matrixWidth, matrixHeight, BufferedImage.TYPE_INT_RGB);
        /** 把二维码字节转换对象中的内容绘制缓冲流图片 */
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                /** 获取一个像点，判断是白色点还是黑色的点  true : 黑色点   false: 白色点 */
                int rgb = matrix.get(x, y) ? 0x000000 : 0xffffff;
                image.setRGB(x, y, rgb);
            }
        }

        ImageIO.write(image, "png", response.getOutputStream());

        Map<String, Object> result = new HashMap<>();
        result.put("width", width);
        result.put("height", height);
        result.put("content", content);
        return result;
    }

}
