package com.siddharth.customer;

import java.util.Optional;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {

}
