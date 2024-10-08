package com.example.chopar_1.config;




import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
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
           Server devServer=new Server();
            devServer.setUrl(url);
            devServer.description("Server URL");

            Contact contact = new Contact();
            contact.setEmail("kun.uz");
            contact.setName("Bez" +
                    "Koder");
            contact.url("https://www.bezkoder.com");

               Info info=new Info()
                    .title("Kun uz Management API")
                    .version("1.0")
                    .contact(contact)
                    .description("This API exposes endpoints to manage tutorials.")
                    .termsOfService("https://www.bezkoder.com/terms")
                    .license(null);

            return new OpenAPI().info(info).servers(List.of(devServer));

    }
}

