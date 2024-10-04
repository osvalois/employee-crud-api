package com.picura.employee.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${picura.openapi.dev-url}")
    private String devUrl;

    @Value("${picura.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Info info = new Info()
                .title("Employee Management API")
                .version("1.0")
                .description("Esta API proporciona endpoints para gestionar empleados.")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
