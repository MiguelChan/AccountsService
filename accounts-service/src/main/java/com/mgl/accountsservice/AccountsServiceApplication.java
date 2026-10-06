package com.mgl.accountsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main entry point of the AccountsService.
 */
@SpringBootApplication
@EnableTransactionManagement
public class AccountsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsServiceApplication.class, args);
    }

}
