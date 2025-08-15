package com.example.customer.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class OpenApIConfig {
    @Value("${server.url")
    private String url;

    @Bean
    public OpenAPI myOpenAPI() {
           var devServer=new Server();
            devServer.setUrl(url);
            devServer.description("Server URL");

            var contact = new Contact();
            contact.setEmail("chopar.uz");
            contact.setName("Bez" +
                    "Koder");
            contact.url("https://www.da.com");

               var info=new Info()
                    .title("Chopar  Management API")
                    .version("1.0")
                    .contact(contact)
                    .description("This API exposes endpoints to manage tutorials.")
                    .termsOfService("https://www.ad.com/terms")
                    .license(null);

            return new OpenAPI().info(info).servers(List.of(devServer));

    }
}

