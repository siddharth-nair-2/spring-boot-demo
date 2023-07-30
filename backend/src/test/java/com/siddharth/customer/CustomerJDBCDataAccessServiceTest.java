package com.siddharth.customer;

import com.siddharth.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // Given
        insertLocalCustomer();

        // When
        List<Customer> actual = underTest.selectAllCustomers();

        //Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        Customer customer = insertLocalCustomer();

        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        // Given
        int id = -1;

        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = insertLocalCustomer();

        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void existsCustomerWithEmail() {
        // Given
        Customer customer = insertLocalCustomer();

        // When
        boolean actual = underTest.existsCustomerWithEmail(customer.getEmail());

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithEmailReturnsFalseWhenInvalidEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        // When
        boolean actual = underTest.existsCustomerWithEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomer() {
        // Given
        Customer customer = insertLocalCustomer();

        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        underTest.deleteCustomer(id);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isNotPresent();
    }

    @Test
    void willNotDeleteCustomerIfIncorrectId() {
        // Given
        Customer customer = insertLocalCustomer();

        int customerCount1 = underTest.selectAllCustomers().size();
        // When
        underTest.deleteCustomer(-1);

        int actual = underTest.selectAllCustomers().size();

        // Then
        assertThat(actual).isEqualTo(customerCount1);

    }

    @Test
    void existsCustomerWithId() {
        // Given
        Customer customer = insertLocalCustomer();

        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        boolean actual = underTest.existsCustomerWithId(id);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithIdReturnsFalseWithInvalidId() {
        // Given
        int id = -1;

        // When
        boolean actual = underTest.existsCustomerWithId(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void updateCustomerWithOnlyName() {
        // Given
        Customer customer = insertLocalCustomer();

        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        String updatedName = FAKER.name().fullName();
        Customer updatedCustomer = new Customer(
                id,
                updatedName,
                customer.getEmail(),
                customer.getAge());

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValue(updatedCustomer);
    }

    @Test
    void updateCustomerWithOnlyEmail() {
        // Given
        Customer customer = insertLocalCustomer();

        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        String updatedEmail = FAKER.internet().safeEmailAddress();
        Customer updatedCustomer = new Customer(
                id,
                customer.getName(),
                updatedEmail,
                customer.getAge());

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValue(updatedCustomer);
    }

//    @Test
//    void updateCustomerWithOnlyAge() {
//        // Given
//        Customer customer = insertLocalCustomer();
//
//        int id = underTest.selectAllCustomers().stream()
//                .filter(c -> c.getEmail().equals(customer.getEmail()))
//                .map(Customer::getId)
//                .findFirst()
//                .orElseThrow();
//
//        // When
//        int updatedAge = 57;
//        Customer updatedCustomer = new Customer(
//                id,
//                customer.getName(),
//                customer.getEmail(),
//                updatedAge);
//
//        underTest.updateCustomer(updatedCustomer);
//
//        Optional<Customer> actual = underTest.selectCustomerById(id);
//
//        //Then
//        assertThat(actual).isPresent().hasValue(updatedCustomer);
//    }

    @Test
    void updateCustomerWithAllFields() {
        // Given
        Customer customer = insertLocalCustomer();

        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When

        String updatedName = FAKER.name().fullName();
        String updatedEmail = FAKER.internet().safeEmailAddress();
        Customer updatedCustomer = new Customer(
                id,
                updatedName,
                updatedEmail,
                26);

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValue(updatedCustomer);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        // Given
        Customer customer = insertLocalCustomer();

        int id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Customer updatedCustomer = new Customer(
                id,
                customer.getName(),
                customer.getEmail(),
                customer.getAge());

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValue(updatedCustomer);
    }

    private Customer insertLocalCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20
        );
        underTest.insertCustomer(customer);

        return customer;
    }
}
