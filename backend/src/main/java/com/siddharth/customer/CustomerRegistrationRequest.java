package com.siddharth.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age,
        String gender) {
}
