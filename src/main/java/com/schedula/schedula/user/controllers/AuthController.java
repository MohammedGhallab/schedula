package com.schedula.schedula.user.controllers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.schedula.schedula.config.JWT.JWTService;
import com.schedula.schedula.user.CustomUserDetails;
import com.schedula.schedula.user.models.dto.LoginRequset;
import com.schedula.schedula.user.models.dto.UserDTO;
import com.schedula.schedula.user.services.UserServices;

import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserServices userServices;
    private final JWTService jwtService;

    @PostMapping("/login")
    public ResponseEntity<CustomUserDetails> login(@Valid @RequestBody LoginRequset data,
            HttpServletResponse response, HttpServletRequest request) {
        String userInput = data.getCaptcha();
        // جلب القيمة الأصلية التي خزنها السيرفر في الجلسة عند توليد الصورة
        String sessionCaptcha = (String) request.getSession().getAttribute("captcha");

        if (sessionCaptcha != null && sessionCaptcha.equalsIgnoreCase(userInput)) {
            CustomUserDetails login = userServices.login(data);
            // String token = jwtService.generateToken(login.getEmail(), false);
            // SimpleGrantedAuthority authority = new
            // SimpleGrantedAuthority(login.getRole());
            String token = jwtService.generateToken(login);
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // HTTPS only
            cookie.setPath("/");
            if (data.getRememberMe())
                cookie.setMaxAge(60 * 60 * 24 * 7); // 1 week
            else
                cookie.setMaxAge(60 * 60); // 1 hour

            response.addCookie(cookie);
            // login.setPassword("Pass Is Hash");
            return ResponseEntity.ok(login);
        } else
            return ResponseEntity.status(422).body(null);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO data, HttpServletResponse response) {
        data = userServices.saveUser(data);
        if (data.getId() != null) {
            // SimpleGrantedAuthority authority = new SimpleGrantedAuthority(data.getRole());
            String token = jwtService.generateToken(new CustomUserDetails(data.getId(),
                    data.getEmail(),
                    data.getPassword(),
                    data.getActive(),
                    data.getRole(),
                    data.getName(),
                    data.getEmail()));
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // HTTPS only
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 1 hour
            response.addCookie(cookie);
        }
        data.setPassword("Pass Is Hash");
        return ResponseEntity.ok(data);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captchaText = generateRandomText(5); // لن يظهر خطأ الآن
        request.getSession().setAttribute("captcha", captchaText);

        BufferedImage img = createCaptchaImage(captchaText); // لن يظهر خطأ الآن

        response.setContentType("image/png");
        ImageIO.write(img, "png", response.getOutputStream());
    }

    @PostMapping("/api/verify-captcha")
    public ResponseEntity<?> verifyCaptcha(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String userInput = payload.get("captchaInput");
        // جلب القيمة الأصلية التي خزنها السيرفر في الجلسة عند توليد الصورة
        String sessionCaptcha = (String) request.getSession().getAttribute("captcha");

        if (sessionCaptcha != null && sessionCaptcha.equalsIgnoreCase(userInput)) {
            // في حال التطابق
            return ResponseEntity.ok(Map.of("success", true, "message", "تم التحقق بنجاح"));
        } else {
            // في حال الخطأ
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "رمز التحقق غير صحيح"));
        }
    }

    // دالة لتوليد نص عشوائي
    private String generateRandomText(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // دالة لتحويل النص إلى صورة (BufferedImage)
    private BufferedImage createCaptchaImage(String text) {
        int width = 150;
        int height = 50;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();

        // خلفية الصورة
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // إضافة خطوط عشوائية (Noise) لمنع الـ Bots
        g2d.setColor(Color.LIGHT_GRAY);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g2d.drawLine(x1, y1, x2, y2);
        }

        // كتابة النص
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(Color.BLUE);
        g2d.drawString(text, 25, 35);

        g2d.dispose();
        return img;
    }
}
