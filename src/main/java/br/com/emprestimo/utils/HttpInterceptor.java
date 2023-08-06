package br.com.emprestimo.utils;

import br.com.emprestimo.dtos.LogRequestDto;
import br.com.emprestimo.kafka.producer.LogProducer;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
public class HttpInterceptor extends WebRequestHandlerInterceptorAdapter {

    private final WebRequestInterceptor requestInterceptor;

    private final AccessTokenRepository repository;

    private final LogProducer producer;

    private final AccessTokenService accessTokenService;

    public HttpInterceptor(WebRequestInterceptor requestInterceptor, AccessTokenRepository repository, LogProducer producer, AccessTokenService accessTokenService) {
        super(requestInterceptor);
        this.requestInterceptor = requestInterceptor;
        this.repository = repository;
        this.producer = producer;
        this.accessTokenService = accessTokenService;
    }

    @Bean
    public WebMvcConfigurerAdapter adapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new HttpInterceptor(requestInterceptor, repository, producer, accessTokenService))
                        .addPathPatterns("/**")
                        .excludePathPatterns("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**" /*, "/auth/**"*/);
            }
        };
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        generateLog(request);

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
        }

        return true;
    }

    private void generateLog(HttpServletRequest request) throws ServletException, IOException {
        var logInfo = new LogRequestDto();
        try {
            logInfo.setMessage(request.getParts().toString());
            logInfo.setRequest(request.getParts().toString());
            producer.sendLog(logInfo);
        } catch (ServletException | IOException exception) {
            log.error("Error while send log -> message {} ", exception.toString());
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
