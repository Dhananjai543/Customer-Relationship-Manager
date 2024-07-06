package com.dhananjai.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CustomerRelationshipManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerRelationshipManagerApplication.class, args);
	}

}
