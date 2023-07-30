package com.siddharth.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {

}
