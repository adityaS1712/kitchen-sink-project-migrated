package org.jboss.as.quickstarts.kitchensink;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "org.jboss.as.quickstarts.kitchensink")
@EnableJpaRepositories(basePackages = {"org.jboss.as.quickstarts.kitchensink.data"})
@EntityScan(basePackages = {"org.jboss.as.quickstarts.kitchensink.model"})
@ComponentScan(basePackages = "org.jboss.as.quickstarts.kitchensink")
public class KitchensinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitchensinkApplication.class, args);
    }
}