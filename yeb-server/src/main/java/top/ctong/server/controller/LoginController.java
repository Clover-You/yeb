package top.ctong.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ctong.server.models.R;
import top.ctong.server.models.dto.UserLoginDto;
import top.ctong.server.models.entity.AdminEntity;
import top.ctong.server.service.IAdminService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

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
 * 前端登录控制器
 * </p>
 * @author Clover
 * @email cloveryou02@163.com
 * @create 2022-10-08 11:20 PM
 */
@RestController
@RequestMapping("/")
@Api(value = "前端登录控制器", tags = "LoginController")
public class LoginController {

    @Setter(onMethod = @__(@Autowired))
    private IAdminService iAdminService;

    /**
     * 用户登录
     * @param dto 用户登录参数
     * @return R<String> 登录成功后返回 token
     * @author Clover
     * @date 2022/10/8 11:22 PM
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R login(@RequestBody UserLoginDto dto, HttpServletRequest req) {
        return R.ok(iAdminService.doLogin(dto.getUsername(), dto.getPassword(), dto.getCode(), req));
    }

    /**
     * 注销登录
     * @return R<?>
     * @author Clover
     * @date 2022/10/9 12:23 AM
     */
    @ApiOperation("注销登录")
    @PostMapping("/logout")
    public void logout() {

    }

    @ApiOperation("获取用户信息")
    @RequestMapping("/admin/info")
    public AdminEntity getUserInfo(Principal principal) {
        String userName = principal.getName();
        AdminEntity admin = iAdminService.getAdminByUserName(userName);
        admin.setPassword(null);
        return admin;
    }


}
