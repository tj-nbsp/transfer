package org.example.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 当前包中发起 http 请求的封装.
 * based on hutool-http
 */
public class DoHttpUtil {

    private static final Logger log = LoggerFactory.getLogger(DoHttpUtil.class);

    public static <T> T post(String url, String json, Consumer<HttpRequest> extractHeader, Function<HttpResponse, T> logic) {
        log.debug("准备调用接口：{}", url);
        HttpRequest request = HttpUtil.createPost(url);
        request.header("Content-Type", "application/json");
        request.header("version", "TYC-Web");
        if (extractHeader != null) {
            extractHeader.accept(request);
        }
        request.body(json);
        HttpResponse response = request.execute();
        return logic.apply(response);
    }

}
