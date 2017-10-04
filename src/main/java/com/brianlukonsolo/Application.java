package com.brianlukonsolo;

import com.brianlukonsolo.routes.FileToDocumentRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class Application {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new FileToDocumentRoute());
        context.start();
        Thread.sleep(5000);
        context.stop();

        SpringApplication.run(Application.class, args);

    }

}
