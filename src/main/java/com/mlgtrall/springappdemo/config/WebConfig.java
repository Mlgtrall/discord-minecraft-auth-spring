package com.mlgtrall.springappdemo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Защита среды запросов
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ApiKeyInterceptor apiKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Применяем проверку только к API для плагина
//        registry.addInterceptor(apiKeyInterceptor)
//                .addPathPatterns("/api/players/check/**"); //TODO: Почему-то приходит не полный URL и похоже без HEADERS
    }
}
