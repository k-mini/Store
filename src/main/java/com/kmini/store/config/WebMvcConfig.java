package com.kmini.store.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + fileDir)
                .setCachePeriod(60 * 10 * 6)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
