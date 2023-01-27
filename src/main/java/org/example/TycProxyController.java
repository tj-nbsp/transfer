package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("tyc-proxy")
public class TycProxyController {
    private static final Logger logger = LoggerFactory.getLogger(TycProxyController.class);

    @PostMapping("transfer")
    public void transfer(@RequestParam("url") String url,
                         @RequestBody Map<String, Object> params,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        logger.info("代理的 url: {}", url);
        String result = DoHttp.post(url, new ObjectMapper().writeValueAsString(params),
                httpRequest -> httpRequest.header("x-auth-token", request.getHeader("x-auth-token")),
                httpResponse -> {
            response.setStatus(httpResponse.getStatus());
            return httpResponse.body();
        });
        logger.info("代理的接口返回的数据是: {}", result);
        response.getWriter().write(result);
        response.getWriter().flush();
    }

}
