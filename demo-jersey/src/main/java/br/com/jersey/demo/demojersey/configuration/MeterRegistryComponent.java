package br.com.jersey.demo.demojersey.configuration;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.binder.jersey.server.JerseyTags;
import org.glassfish.jersey.server.ContainerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class MeterRegistryComponent implements Filter {

    private final MeterRegistry registry = Metrics.globalRegistry;
    private final MetricsProperties properties;

    @Autowired
    public MeterRegistryComponent(MetricsProperties properties)  {
        this.properties = properties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        ContainerResponse containerResponse = (ContainerResponse) response;
        long start = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
            long durationNanos = System.nanoTime() - start;
            registry.timer(properties.getWeb().getClient().getRequest().getMetricName(), Tags.of(JerseyTags.outcome(containerResponse))).record(durationNanos, TimeUnit.NANOSECONDS);
        } catch (Exception ex) {
            //Equivalent exception timer recording
            throw ex;
        }
    }
}
