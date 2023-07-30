package com.siddharth.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE id = ?
                """;
        return jdbcTemplate
                .query(sql, customerRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age)
                VALUES (?, ?, ?)
                """;

        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );

        System.out.println("jdbcTemplate.update: " + result);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE name = ?
                """;

        return jdbcTemplate.query(sql, customerRowMapper, email).size() > 0;
    }

    @Override
    public void deleteCustomer(Integer id) {
        var sql = """
                DELETE FROM customer
                WHERE id = ?
                """;

        int result = jdbcTemplate.update(sql, id);
        System.out.println("jdbcTemplate.delete: " + result);
    }

    @Override
    public boolean existsCustomerWithId(Integer id) {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, customerRowMapper, id).size() > 0;
    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getName() != null) {
            var sql = """
                UPDATE customer
                SET name = ?
                WHERE id = ?
                """;
            int result = jdbcTemplate.update(sql, customer.getName(), customer.getId());
            System.out.println("updated customer name result= " + result);
        }
        if (customer.getEmail() != null) {
            var sql = """
                UPDATE customer
                SET email = ?
                WHERE id = ?
                """;
            int result = jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
            System.out.println("updated customer email result= " + result);
        }
        if (customer.getAge() != null) {
            var sql = """
                UPDATE customer
                SET age = ?
                WHERE id = ?
                """;
            int result = jdbcTemplate.update(sql, customer.getAge(), customer.getId());
            System.out.println("updated customer age result= " + result);
        }

    }
}
