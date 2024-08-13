package org.jboss.as.quickstarts.kitchensink;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = "org.jboss.as.quickstarts.kitchensink")
@ComponentScan(basePackages = "org.jboss.as.quickstarts.kitchensink")
@EntityScan(basePackages = {"org.jboss.as.quickstarts.kitchensink.model"})
public class KitchensinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitchensinkApplication.class, args);
    }
}