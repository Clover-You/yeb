package top.ctong.server.config.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.ctong.server.dao.AdminDao;
import top.ctong.server.exception.DataFoundRespException;
import top.ctong.server.filter.JwtAuthorizationFilter;
import top.ctong.server.models.entity.AdminEntity;

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
 * security 配置
 * </p>
 * @author Clover
 * @email cloveryou02@163.com
 * @create 2022-10-10 10:36 PM
 */
@Configuration
public class SecurityConfig {
    @Setter(onMethod = @__(@Autowired))
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Setter(onMethod = @__(@Autowired))
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Setter(onMethod = @__(@Autowired))
    private AdminDao adminDao;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // 使用 jwt 验证不需要csrf
            .csrf()
            .disable()
            // 基于token不需要session
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(
                "/login",
                "/webjars/**",
                "/test",
                "/logout",
                "/doc.html",
                "/index.html",
                "/favicon.ico",
                "/css/**",
                "/js/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/swagger-ui/index.html",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/captcha/**"
            )
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .headers()
            .cacheControl()
            .disable()
            .and()
            .userDetailsService(userDetailsService());

        // 添加过滤器
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 添加自定义未授权/未登录结果返回
        http.exceptionHandling()
            .accessDeniedHandler(restAccessDeniedHandler)
            .authenticationEntryPoint(restAuthenticationEntryPoint);

        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> {
//            web.ignoring()
//                .antMatchers(
//                    "/login",
//                    "/webjars/**",
//                    "/test",
//                    "/logout",
//                    "/doc.html",
//                    "/index.html",
//                    "/favicon.ico",
//                    "/css/**",
//                    "/js/**",
//                    "/swagger-resources/**",
//                    "/v2/api-docs/**"
//                );
//        };
//    }

    /**
     * 自定义UserDetailsService
     * @return UserDetailsService
     * @author Clover
     * @date 2022/10/10 11:15 PM
     */
    @Bean
    public UserDetailsService userDetailsService() throws DataFoundRespException {
        return username -> adminDao.selectOne(new QueryWrapper<AdminEntity>().eq("username", username));
    }

    /**
     * 自定义加密
     * @return PasswordEncoder
     * @author Clover
     * @date 2022/10/10 11:15 PM
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录拦截器
     * @return UsernamePasswordAuthenticationFilter
     * @author Clover
     * @date 2022/10/10 11:55 PM
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }

}
