package com.deliverytech.delivery_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Delivery Tech API")
                .version("1.0.0")
                .description("API REST completa para sistema de delivery desenvolvida com Spring Boot e Java 21. " +
                    "Esta API permite gerenciar restaurantes, produtos, pedidos e relatórios de vendas.")
                .contact(new Contact()
                    .name("Gabriel de Freitas Monguilhott")
                    .email("gfreitas15@example.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Servidor de Desenvolvimento"),
                new Server()
                    .url("https://api.deliverytech.com.br")
                    .description("Servidor de Produção")
            ));
    }
}

