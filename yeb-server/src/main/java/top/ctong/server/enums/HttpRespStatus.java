package top.ctong.server.enums;

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
 * http 响应状态码
 * </p>
 * @author Clover
 * @email cloveryou02@163.com
 * @create 2022-10-08 10:58 PM
 */
public enum HttpRespStatus {

    OK(200, "success"),

    FAIL(500, "service fail!!"),

    USER_LOGIN_PASSWORD_ERROR(1000, "密码错误!!"),

    USER_LOGIN_USERNAME_NOT_FOUND(1001, "用户名不存在!!"),

    USER_LOGIN_DISABLED(1002, "用户已被禁止登录!!"),

    USER_NOT_FOUND(1003, "用户不存在!"),

    VERIFICATION_CODE_ERROR(1004, "验证码错误!!"),

    NO_LOGIN(401, "请登录!!"),

    NO_PERMISSIONS(403, "您没有访问权限!!"),

    ;

    private long code;

    private String message;

    HttpRespStatus(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
