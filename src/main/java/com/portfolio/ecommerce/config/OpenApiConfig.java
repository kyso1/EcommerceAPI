package com.portfolio.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de E-commerce Simplificada")
                        .description("API construída para portfólio, com funcionalidades de Carrinho, Produtos e Checkout.")
                        .contact(new Contact()
                                .name("TestName")
                                .email("test_mail@example.com"))
                        .version("1.0.0"));
    }
}