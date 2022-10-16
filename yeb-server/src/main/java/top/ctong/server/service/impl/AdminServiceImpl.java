package top.ctong.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.ctong.server.constant.SessionKey;
import top.ctong.server.dao.AdminDao;
import top.ctong.server.enums.HttpRespStatus;
import top.ctong.server.exception.DataFoundRespException;
import top.ctong.server.exception.UserLoginException;
import top.ctong.server.models.entity.AdminEntity;
import top.ctong.server.config.security.JwtTokenUtils;
import top.ctong.server.service.IAdminService;

import javax.servlet.http.HttpServletRequest;

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
 * 用户service
 * </p>
 * @author Clover
 * @email cloveryou02@163.com
 * @create 2022-10-08 11:37 PM
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminDao, AdminEntity> implements IAdminService {

    @Setter(onMethod = @__(@Autowired))
    private UserDetailsService userDetailsService;

    @Setter(onMethod = @__(@Autowired))
    private PasswordEncoder passwordEncoder;

    @Setter(onMethod = @__(@Autowired))
    private JwtTokenUtils jwtTokenUtils;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param req 当前请求servlet
     * @return String
     * @author Clover
     * @date 2022/10/8 11:52 PM
     */
    @Override
    public String doLogin(String username, String password, String code, HttpServletRequest req) {

        String myCaptcha = (String) req.getSession().getAttribute(SessionKey.WEB_LOGIN_CAPTCHA_KEY);
        if (!StringUtils.hasText(code) || !myCaptcha.equalsIgnoreCase(code)) {
            // 验证码校验失败
            throw new UserLoginException(HttpRespStatus.VERIFICATION_CODE_ERROR);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new UserLoginException(HttpRespStatus.USER_LOGIN_USERNAME_NOT_FOUND);
        }

        boolean matches = passwordEncoder.matches(password, userDetails.getPassword());
        if (!matches) {
            throw new UserLoginException(HttpRespStatus.USER_LOGIN_PASSWORD_ERROR);
        }

        if (userDetails.isEnabled()) {
            throw new UserLoginException(HttpRespStatus.USER_LOGIN_DISABLED);
        }

        // 添加登录信息到 spring security 上下文中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 生成用户 token
        return jwtTokenUtils.generatorToken(userDetails);
    }

    /**
     * 通过用户名获取用户对象
     * @param userName 用户名
     * @return AdminEntity
     * @author Clover
     * @date 2022/10/9 12:28 AM
     */
    @Override
    public AdminEntity getAdminByUserName(String userName) {
        QueryWrapper<AdminEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username", userName);
        AdminEntity entity = baseMapper.selectOne(wrapper);
        if (entity == null) {
            throw new DataFoundRespException(HttpRespStatus.USER_NOT_FOUND);
        }
        return entity;
    }
}
