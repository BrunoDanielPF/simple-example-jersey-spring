package br.com.jersey.demo.demojersey.configuration;

import io.micrometer.core.instrument.*;
import org.glassfish.jersey.process.internal.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class MeterRegistryComponent implements WebMvcConfigurer {

    private MeterRegistry registry = Metrics.globalRegistry;
    private Counter totalCounter;

    private static final String HTTP_CLIENT_METRIC_NAME = "http.client.requests";

    @Autowired
    public MeterRegistryComponent(MeterRegistry registry, HttpRequest request) throws Exception {
        this.registry = registry;
        try{
            totalCounter = Counter
                    .builder(HTTP_CLIENT_METRIC_NAME)
                    .description("this is custom counter for client request")
                    .tags(Tags.of("method",request.getMethod().toString()))
                    .register(registry);
            this.totalCounter = this.registry.counter("http_client_requests_seconds_sum");
        }catch (IllegalArgumentException exception){
            throw new Exception("elemento informado incorretamente" + exception.getMessage());
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public void afterCompletion(HttpServletRequest request,
                                        HttpServletResponse response, Object handler, Exception ex)
                    throws Exception {
                System.out.println("Counter incremented");
                totalCounter.increment();
            }
        });
    }
//    @Override
//    public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) throws IOException {
//        HttpClientMetricsAutoConfiguration http = new HttpClientMetricsAutoConfiguration();
//        Timer.Builder builder = Timer.builder(HTTP_CLIENT_METRIC_NAME)
//                .tags(Tags.of(clientRequestContext.getMethod()));
//        builder.register(meterRegistry);
//    }
//
//    private ClientRequestContext getRequestMetric(ClientRequestContext requestContext) {
//        return (ClientRequestContext) requestContext.getProperty(REQUEST_METRIC_PROPERTY);
//    }


//    @Override
//    public void filter(ClientRequestContext clientRequestContext) throws IOException {
//        totalCounter = Counter
//                .builder("http_client_requests_seconds_sum")
//                .tags(Tags.of(clientRequestContext.getMethod()))
//                .description("this is custom counter for client request")
//                .register(registry);
//        this.totalCounter = this.registry.counter("http_client_requests_seconds_sum");
//    }
//
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new HandlerInterceptorAdapter() {
//            public void afterCompletion(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Object handler,
//                                        Exception ex) {
//                System.out.println("Counter incremented");
//                totalCounter.increment();
//            }
//        });
//    }
}
