package com.siddharth.customer;

import com.siddharth.exception.DuplicateResourceException;
import com.siddharth.exception.RequestValidationException;
import com.siddharth.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(id)
                ));

    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {

        String email = customerRegistrationRequest.email();
        // check if email exists
        if (customerDao.existsCustomerWithEmail(email)) {
            throw new DuplicateResourceException(
                    "Email already taken."
            );
        }
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender()
        );
        //
        customerDao.insertCustomer(customer);
    }

    public void removeCustomer(Integer id) {
        // check if id exists
        if(!customerDao.existsCustomerWithId(id)) {
            throw new RequestValidationException("customer with id [%s] not found".formatted(id));
        }

        customerDao.deleteCustomer(id);
    }

    public void updateCustomer(Integer id, CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomer(id);

        boolean changes = false;

        if(updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }
        if(updateRequest.gender() != null && !updateRequest.gender().equals(customer.getGender())) {
            customer.setGender(updateRequest.gender());
            changes = true;
        }
        if(updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }
        if(updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            // check if email exists
            if (customerDao.existsCustomerWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "Email already taken."
                );
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if(!changes) {
            throw new RequestValidationException(
                    "No changes found!"
            );
        }

        customerDao.updateCustomer(customer);
    }
}
