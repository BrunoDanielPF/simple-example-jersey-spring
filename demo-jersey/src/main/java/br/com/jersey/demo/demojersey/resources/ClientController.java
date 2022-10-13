package br.com.jersey.demo.demojersey.resources;

import br.com.jersey.demo.demojersey.service.ClientService;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.common.TextFormat;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Component
@Path("/client")
public class ClientController {

    public static final double metricValue = 123;

    @Autowired
    private ClientService clientService;

    @GET
    @Path("/api")
    public String getAPI() {
        Client client = ClientBuilder.newClient(new ClientConfig().register(String.class));
        WebTarget webTarget = client.target("http://localhost:6060/");

        Invocation.Builder builder = webTarget.request(APPLICATION_JSON);
        Response response = builder.get();

        return response.readEntity(String.class);
    }

    @GET
    @Path("/client")
    public String getClientName() {
        return clientService.getName();
    }



}
