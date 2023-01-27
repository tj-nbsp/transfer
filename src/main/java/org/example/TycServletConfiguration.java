package org.example;

import com.google.common.collect.ImmutableMap;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class TycServletConfiguration {

    @Value("${tyc.login_url}")
    private String loginUrl;
    @Value("${tyc.target_login_url}")
    private String targetLoginUrl;

    @Value("${tyc.servlet_url}")
    private String servletUrl;
    @Value("${tyc.target_url}")
    private String targetUrl;

    @Bean
    public ServletRegistrationBean tycLoginRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new ProxyServlet(), new String[]{this.loginUrl});
        registrationBean.setName("tycLogin");
        Map<String, String> map = ImmutableMap.of("targetUri", this.targetLoginUrl, "log", "true");
        registrationBean.setInitParameters(map);
        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean tycServletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new ProxyServlet(), new String[]{this.servletUrl});
        registrationBean.setName("tycServlet");
        Map<String, String> map = ImmutableMap.of("targetUri", this.targetUrl, "log", "true");
        registrationBean.setInitParameters(map);
        return registrationBean;
    }

}
