package br.com.jersey.demo.demojersey.configuration;

import io.micrometer.core.instrument.*;
import org.glassfish.jersey.process.internal.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Configuration
public class MeterRegistryComponent implements Filter {
//    WebMvcConfigurer
    private MeterRegistry registry = Metrics.globalRegistry;
    private Counter totalCounter;
    private static final String HTTP_CLIENT_METRIC_NAME = "http.client.requests";

    @Autowired
    public MeterRegistryComponent(MeterRegistry registry) throws Exception {
        this.registry = registry;
        try{
            totalCounter = Counter
                    .builder(HTTP_CLIENT_METRIC_NAME)
                    .description("this is custom counter for client request")
                    .tags(Tags.of("method", "tag customizada"))
                    .register(registry);
            this.totalCounter = this.registry.counter("http_client_requests_seconds_sum");
            totalCounter.increment();
        }catch (IllegalArgumentException exception){
            throw new Exception("elemento informado incorretamente" + exception.getMessage());
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = ((HttpServletRequest) servletRequest);
        totalCounter = Counter
                .builder(HTTP_CLIENT_METRIC_NAME)
                .description("this is custom counter for client request")
                .tags(Tags.of("method", httpRequest.getMethod()))
                .register(registry);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }


//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new HandlerInterceptor() {
//            @Override
//            public void afterCompletion(HttpServletRequest request,
//                                        HttpServletResponse response, Object handler, Exception ex)
//                    throws Exception {
//                System.out.println("Counter incremented");
//                totalCounter.increment();
//            }
//        });
//    }
}
