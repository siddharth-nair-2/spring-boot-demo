package com.siddharth;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.siddharth.customer.Customer;
import com.siddharth.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            Faker faker = new Faker();
            Random rnd = new Random();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            Customer customer = new Customer(
                    firstName + " " + lastName,
                    firstName.toLowerCase() +
                            "." +
                            lastName.toLowerCase() +
                            "@gmail.com",
                    rnd.nextInt(16, 99)
            );
            customerRepository.save(customer);
        };
    }

}
