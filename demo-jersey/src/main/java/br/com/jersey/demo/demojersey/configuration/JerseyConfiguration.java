package br.com.jersey.demo.demojersey.configuration;

import br.com.jersey.demo.demojersey.provider.ExceptionMapperGeneric;
import br.com.jersey.demo.demojersey.resources.ClientController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/rest")
public class JerseyConfiguration extends ResourceConfig {

    @PostConstruct
    public void init() {
        register(ClientController.class);
        register(ExceptionMapperGeneric.class);
    }
}
