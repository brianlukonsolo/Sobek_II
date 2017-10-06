package com.brianlukonsolo;

import com.brianlukonsolo.routes.FileToDocumentRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);

        CamelContext context = new DefaultCamelContext();
        //Spring automatically finds the routes
        context.start();
        Thread.sleep(5000);
        context.stop();
    }

}
