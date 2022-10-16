package top.ctong.server.controller;

import com.google.code.kaptcha.Producer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ctong.server.constant.SessionKey;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * █████▒█      ██  ▄████▄   ██ ▄█▀     ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒      ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░      ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄      ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄     ██████╔╝╚██████╔╝╚██████╔╝
 * ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒     ╚═════╝  ╚═════╝  ╚═════╝
 * ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 * ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 * ░     ░ ░      ░  ░
 * Copyright 2022 Clover You.
 * <p>
 * 验证码
 * </p>
 * @author Clover You
 * @email 2621869236@qq.com
 * @create 2022-10-15 00:39
 */
@Slf4j
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Setter(onMethod = @__(@Autowired))
    private Producer producer;

    @GetMapping("/web")
    public void captcha(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws IOException {
        try (OutputStream os = resp.getOutputStream()) {

            resp.setHeader("Cache-Control", "no-store,no-cache");
            resp.setContentType("image/jpeg");

            // 生成随机码
            String verifyCode = producer.createText();
            log.debug("verifyCode: {}", verifyCode);
            BufferedImage image = producer.createImage(verifyCode);

            session.setAttribute(SessionKey.WEB_LOGIN_CAPTCHA_KEY, verifyCode);

            ImageIO.write(image, "jpg", os);
            os.flush();
        } catch (IOException e) {
            throw e;
        }
    }

}
