package org.example.configuration;

import com.google.common.collect.ImmutableMap;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Map;

public class ProxyServletConfiguration {
    @Value("${proxy.servlet_url}")
    private String servletUrl;
    @Value("${proxy.target_url}")
    private String targetUrl;
    @Value("${proxy.servlet_test_url}")
    private String servletTestUrl;
    @Value("${proxy.target_test_url}")
    private String targetTestUrl;

    @Bean
    public ServletRegistrationBean proxyServletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new ProxyServlet(), new String[]{this.servletUrl});
        registrationBean.setName("proxyServlet");
        Map<String, String> map = ImmutableMap.of("targetUri", this.targetUrl, "log", "true");
        registrationBean.setInitParameters(map);
        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean testProxyServletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new ProxyServlet(), new String[]{this.servletTestUrl});
        registrationBean.setName("testProxyServlet");
        Map<String, String> map = ImmutableMap.of("targetUri", this.targetTestUrl, "log", "true");
        registrationBean.setInitParameters(map);
        return registrationBean;
    }

}
