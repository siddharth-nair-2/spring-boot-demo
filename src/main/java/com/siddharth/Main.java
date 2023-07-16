package com.siddharth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/greet")
    public GreetResponse greet() {
        return new GreetResponse("Hello!",
                List.of("Java", "Golang", "JavaScript"),
                new Person("Siddharth", 21, 12_412.43));
    }

    record Person(String name, int age, double savings) {

    }

    record GreetResponse(
            String greet,
            List<String> favProgrammingLanguages,
            Person person) {
    }

//    static class GreetResponse {
//        private final String greet;
//        GreetResponse(String greet) {
//            this.greet = greet;
//        }
//
//        public String getGreet() {
//            return greet;
//        }
//
//        @Override
//        public String toString() {
//            return super.toString();
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            return super.equals(obj);
//        }
//
//        @Override
//        public int hashCode() {
//            return super.hashCode();
//        }
//    }
}
