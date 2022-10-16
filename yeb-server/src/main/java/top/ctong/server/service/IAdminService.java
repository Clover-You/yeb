package top.ctong.server.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import top.ctong.server.exception.DataFoundRespException;
import top.ctong.server.exception.UserLoginException;
import top.ctong.server.models.entity.AdminEntity;

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
public interface IAdminService extends IService<AdminEntity> {

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
    String doLogin(String username, String password, String code, HttpServletRequest req) throws UserLoginException;

    /**
     * 通过用户名获取用户对象
     * @param userName 用户名
     * @return AdminEntity
     * @author Clover
     * @date 2022/10/9 12:28 AM
     */
    AdminEntity getAdminByUserName(String userName) throws DataFoundRespException;
}
