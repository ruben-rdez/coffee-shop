package com.coffee.shop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "CoFfee Shop REST API",
        version = "1.0",
        description = "Project for managing coffees and orders in a coffee shop. " +
            "This project is designed to provide a RESTful API for managing coffee products, " +
            "customer orders, and related functionalities. " +
            "It includes features such as adding, updating, deleting, and retrieving coffee products and orders."
    )
)
public class SwaggerConfig {

}
