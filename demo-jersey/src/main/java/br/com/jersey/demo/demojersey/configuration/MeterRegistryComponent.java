package br.com.jersey.demo.demojersey.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import org.glassfish.jersey.client.ClientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;


@Configuration
public class MeterRegistryComponent {

    private MeterRegistry meterRegistry;
    private Counter totalCounter;

    @Autowired
    public MeterRegistryComponent(MeterRegistry registry) {
        this.meterRegistry = registry;
//        myCounter = Counter
//                .builder("http_client_requests_seconds_sum").tag()
//                .description("this is custom counter for client request")
//                .register(registry);
        this.totalCounter = this.meterRegistry.counter("http_client_requests_seconds_sum");
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            public void afterCompletion(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Object handler,
                                        Exception ex) {
                System.out.println("Counter incremented");
                totalCounter.increment();
            }
        });
    }
}
