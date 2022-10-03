package br.com.jersey.demo.demojersey.resources;

import br.com.jersey.demo.demojersey.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Component
@Path("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GET
    @Produces(APPLICATION_JSON)
    public String getClients() {
        return clientService.getName();
    }

}
