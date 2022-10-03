package br.com.jersey.demo.demojersey.service.impl;

import br.com.jersey.demo.demojersey.service.ClientService;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    @Override
    public String getName() {
        return "Bruno";
    }
}
