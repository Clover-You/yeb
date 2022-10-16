package top.ctong.server.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
 * jwt 工具类
 * </p>
 * @author Clover
 * @email cloveryou02@163.com
 * @create 2022-10-04 11:40 PM
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtTokenUtils {

    private static final String CLAIM_KEY_USERNAME = "sub";

    private static final String CLAIM_KEY_CREATE = "created";

    /**
     * jwt 密钥
     */
    private String secrete;

    /**
     * 过期时间
     */
    private Long expiration;

    /**
     * token 名称
     */
    private String tokenHeader;

    /**
     * token 前缀
     */
    private String tokenHead;

    /**
     * 根据用户信息获取生成token
     * @param userDetails 用户信息
     * @return String 根据用户信息生成的 token
     * @author Clover
     * @date 2022/10/5 12:02 AM
     */
    public String generatorToken(UserDetails userDetails) {
        HashMap<String, Object> jwtParams = new HashMap<>();
        jwtParams.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        jwtParams.put(CLAIM_KEY_CREATE, new Date());
        return generatorToken(jwtParams);
    }

    /**
     * 生成 jwt token
     * @param claims token内容
     * @return String
     * @author Clover
     * @date 2022/10/5 12:03 AM
     */
    public String generatorToken(Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(generatorExpirationDate())
            .signWith(SignatureAlgorithm.HS512, secrete)
            .compact();
    }

    /**
     * 生成 token 失效时间
     * @return Date
     * @author Clover
     * @date 2022/10/5 12:08 AM
     */
    private Date generatorExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 通过 token 获取用户名
     * @param token 验证字符串
     * @return 用户名
     */
    public String getUserNameFormToken(String token) {
        try {
            Claims claims = getClaimsFormToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过 token 获取 token 中的荷载内容
     * @param token 验证字符串
     * @return 荷载内容
     */
    public Claims getClaimsFormToken(String token) {
        return Jwts.parser()
            .setSigningKey(secrete)
            .parseClaimsJws(token).getBody();
    }

    /** 
     * 验证 token 是否有效
     * @param token token
     * @param userDetails 用户信息
     * @return boolean true 验证通过，false 验证不通过
     * @author Clover 
     * @date 2022/10/8 10:34 PM
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String userName = getUserNameFormToken(token);
        return userDetails.getUsername().equals(userName) && isTokenExpired(token);
    }

    /**
     * 验证 token 是否过期
     * @param token token
     * @return true 过期，false 有效
     */
    public boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFormToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 获取 token 过期时间
     * @param token token
     * @return token 过期时间
     */
    public Date getExpiredDateFormToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.getExpiration();
    }

    /**
     * 刷新 token 过期时间
     * @param token token
     * @return 新的token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFormToken(token);
        claims.put(CLAIM_KEY_CREATE, new Date());
        return generatorToken(claims);
    }

}
