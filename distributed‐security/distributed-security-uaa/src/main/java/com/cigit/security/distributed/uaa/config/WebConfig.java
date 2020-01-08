package com.cigit.security.distributed.uaa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration//就相当于springmvc.xml文件
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/login");
        //registry.addViewController("/login-view").setViewName("redirect:/login");
    }

}
