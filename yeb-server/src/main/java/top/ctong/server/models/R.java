package top.ctong.server.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import top.ctong.server.enums.HttpRespStatus;

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
 * api 默认返回结构
 * </p>
 * @author Clover
 * @email cloveryou02@163.com
 * @create 2022-10-08 10:50 PM
 */
@Data
public class R<T> {

    private long code;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private R(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private R(HttpRespStatus hrs, T data) {
        this.code = hrs.getCode();
        this.message = hrs.getMessage();
        this.data = data;
    }

    public static <T> R<T> ok(HttpRespStatus hrs, T data) {
        return new R<>(hrs, data);
    }

    public static <T> R<T> ok(HttpRespStatus hrs) {
        return new R<>(hrs, null);
    }


    public static <T> R<T> ok(T data) {
        return new R<>(HttpRespStatus.OK, data);
    }

    public static <T> R<T> ok() {
        return new R<>(HttpRespStatus.OK, null);
    }

    public static <T> R<T> fail(HttpRespStatus hrs) {
        return new R<>(hrs, null);
    }

    public static <T> R<T> fail(HttpRespStatus hrs, T data) {
        return new R<>(hrs, data);
    }

    public static <T> R<T> fail(int code, String message) {
        return new R<T>(code, message, null);
    }

    public static <T> R<T> fail() {
        return new R<>(HttpRespStatus.FAIL, null);
    }

    public String json() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{" +
                "\"code\":" + HttpRespStatus.FAIL.getCode() + "," +
                "\"message\":" + HttpRespStatus.FAIL.getMessage() +
                "}";
        }
    }
}
