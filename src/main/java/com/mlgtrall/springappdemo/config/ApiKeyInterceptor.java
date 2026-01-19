package com.mlgtrall.springappdemo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component //TODO: ИЗУЧИТЬ
@Slf4j
public class ApiKeyInterceptor implements HandlerInterceptor { //TODO: AsyncHandlerInterceptor?

    @Value("{API_SECURITY_HEADER}")
    private String apiTokenHeader;

    @Value("{API_SECURITY_TOKEN}") //Токен из конфига
    private String apiToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("Handling incoming request API Key.");
        log.debug("Request URL: {}", request.getRequestURL().toString());
        log.debug("Request HEADERS: {}", request.getHeaderNames().toString());

        // Извлекаем ключ из заголовка "X-API-KEY"
        String requestToken = request.getHeader(apiTokenHeader);

        if (requestToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid or missing API Key");
            log.warn("Request token is empty.");
            return false;
        }

        if (apiToken.equals(requestToken)) {
            return true;
        }

        //Если ключ неверный - отправляем ошибку
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid or missing API Key");
        log.warn("Invalid API Key. Was provided: {}", requestToken);
        return false;
    }
}
