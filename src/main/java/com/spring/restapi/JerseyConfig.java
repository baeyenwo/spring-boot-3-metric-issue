package com.spring.restapi;

import com.spring.restapi.controller.TutorialController;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/mysite")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(TutorialController.class);
    }

}
