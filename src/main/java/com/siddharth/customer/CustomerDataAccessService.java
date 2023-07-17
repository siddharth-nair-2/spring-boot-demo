package com.siddharth.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDataAccessService implements CustomerDao{
    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(1, "Alex", "alex@gmail.com", 21);
        customers.add(alex);
        Customer alexa = new Customer(2, "Alexa", "alexa@gmail.com", 24);
        customers.add(alexa);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {

        return customers.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst();
    }
}
