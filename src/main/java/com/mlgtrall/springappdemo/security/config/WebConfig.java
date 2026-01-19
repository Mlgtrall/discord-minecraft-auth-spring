package com.mlgtrall.springappdemo.security.config;

import com.mlgtrall.springappdemo.security.ApiKeyInterceptor;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Защита среды запросов
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ApiKeyInterceptor apiKeyInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // Применяем проверку только к API для плагина
//        registry.addInterceptor(apiKeyInterceptor)
//                .addPathPatterns("/api/players/check/**"); //TODO: Почему-то приходит не полный URL и похоже без HEADERS
    }
}
