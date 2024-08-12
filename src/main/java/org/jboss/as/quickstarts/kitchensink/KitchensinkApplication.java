package org.jboss.as.quickstarts.kitchensink;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.jboss.as.quickstarts.kitchensink")
public class KitchensinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitchensinkApplication.class, args);
    }
}