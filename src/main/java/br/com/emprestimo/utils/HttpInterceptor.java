package br.com.emprestimo.utils;

import br.com.emprestimo.repositories.AccessTokenRepository;
import br.com.emprestimo.services.AccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Objects;

@Service
@Slf4j
public class HttpInterceptor extends WebRequestHandlerInterceptorAdapter {

    private final WebRequestInterceptor requestInterceptor;

    private final AccessTokenRepository repository;

    private final AccessTokenService accessTokenService;

    public HttpInterceptor(WebRequestInterceptor requestInterceptor, AccessTokenRepository repository, AccessTokenService accessTokenService) {
        super(requestInterceptor);
        this.requestInterceptor = requestInterceptor;
        this.repository = repository;
        this.accessTokenService = accessTokenService;
    }

    @Bean
    public WebMvcConfigurerAdapter adapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new HttpInterceptor(requestInterceptor, repository, accessTokenService))
                        .addPathPatterns("/**")
                        .excludePathPatterns("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**" /*, "/auth/**"*/);
            }
        };
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getServletPath().contains("api/v1/user") && Objects.equals(request.getMethod(), "GET")) {
            var token = repository.findByToken(request.getHeader("auth-token")).orElse(null);
            if (null == token || accessTokenService.validateToken(token)) {
                throw new SecurityException("Forbidden");
            }
            UserThreadConfig.setToken(token);
        } else if (request.getServletPath().contains("api/v1/loan") && Objects.equals(request.getMethod(), "POST")) {
            var token = repository.findByToken(request.getHeader("auth-token")).orElse(null);
            if (null == token || accessTokenService.validateToken(token)) {
                throw new SecurityException("Forbidden");
            }
            UserThreadConfig.setToken(token);
        } else if (request.getServletPath().contains("api/v1/loan-payment") && Objects.equals(request.getMethod(), "PATCH")) {
            var token = repository.findByToken(request.getHeader("auth-token")).orElse(null);
            if (null == token || accessTokenService.validateToken(token)) {
                throw new SecurityException("Forbidden");
            }
            UserThreadConfig.setToken(token);
        }

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
