package org.example.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("page-support")
public class PageSupportController {

    @RequestMapping(value = "get-vue-global-js", method = RequestMethod.GET)
    public void getVueGlobalJs(HttpServletResponse response) throws IOException {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("static/manage/javascript/vue.global.js");
        IOUtils.copy(inputStream, response.getOutputStream());
    }

}
