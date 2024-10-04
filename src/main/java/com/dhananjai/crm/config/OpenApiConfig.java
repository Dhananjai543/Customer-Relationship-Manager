package com.dhananjai.crm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Relationship Manager")
                        .description("This project is a Customer Relationship Manager (CRM) application that allows users to manage customer data, orders, and more.\n\n"
                                + "## Features\n"
                                + "- Manage customers: Add, update, delete, and list customers.\n"
                                + "- Manage orders: Add, update, delete, and list orders for customers.\n"
                                + "- Bulk upload: Upload customer data via Excel files.\n\n"
                                + "- Analyze Data using AI: Use customer ordering history to analyze and get insights.\n\n")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Dhananjai")
                                .email("dhananjaisaini9@gmail.com")
                                .url("https://www.linkedin.com/in/dhananjai-saini-7007/")));
    }
}