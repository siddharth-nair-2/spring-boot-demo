package com.siddharth.customer;

import com.siddharth.exception.DuplicateResourceException;
import com.siddharth.exception.RequestValidationException;
import com.siddharth.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;

    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();

        // Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id,
                "Sid",
                "sid@gmail.com",
                20,
                new Random().nextBoolean() ? "MALE" : "FEMALE"
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        // When
        Customer actual = underTest.getCustomer(id);

        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerEmpty() {
        // Given
        int id = 1;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {
        // Given
        String email = "sid@gmail.com";

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Sid",
                email,
                20,
                new Random().nextBoolean() ? "MALE" : "FEMALE"
        );

        // When
        underTest.addCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(request.gender());
    }

    @Test
    void willThrowWhenCustomerAlreadyExists() {
        // Given
        String email = "sid@gmail.com";

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Sid",
                email,
                20,
                new Random().nextBoolean() ? "MALE" : "FEMALE"
        );

        // When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken.");

        // Then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void removeCustomer() {
        // Given
        int id = 1;
        when(customerDao.existsCustomerWithId(id))
                .thenReturn(true);

        // When
        underTest.removeCustomer(id);

        // Then
        verify(customerDao).deleteCustomer(id);

    }

    @Test
    void willThrowWhenCustomerIdInvalidWhileRemovingCustomer() {
        // Given
        int id = 1;
        when(customerDao.existsCustomerWithId(id))
                .thenReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> underTest.removeCustomer(id))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        verify(customerDao, never()).deleteCustomer(id);
    }

    @Test
    void canUpdateAllCustomerValues() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "Sid", "sid@gmail.com", 20,
                new Random().nextBoolean() ? "MALE" : "FEMALE"
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        String email = "sid2@cyborg.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Sid2", email, 43,
                Objects.equals(customer.getGender(), "MALE") ? "FEMALE" : "MALE"
        );

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer captured = customerArgumentCaptor.getValue();

        assertThat(captured.getAge()).isEqualTo(updateRequest.age());
        assertThat(captured.getName()).isEqualTo(updateRequest.name());
        assertThat(captured.getEmail()).isEqualTo(updateRequest.email());
        assertThat(captured.getGender()).isEqualTo(updateRequest.gender());

    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "Sid", "sid@gmail.com", 20,
                new Random().nextBoolean() ? "MALE" : "FEMALE"
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Cyborg", null, null, null
        );

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer captured = customerArgumentCaptor.getValue();

        assertThat(captured.getAge()).isEqualTo(customer.getAge());
        assertThat(captured.getName()).isEqualTo(updateRequest.name());
        assertThat(captured.getEmail()).isEqualTo(customer.getEmail());
        assertThat(captured.getGender()).isEqualTo(customer.getGender());

    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "Sid", "sid@gmail.com", 20,
                new Random().nextBoolean() ? "MALE" : "FEMALE"
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));


        String email = "cyborg@sid.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, email, null, null
        );

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer captured = customerArgumentCaptor.getValue();

        assertThat(captured.getAge()).isEqualTo(customer.getAge());
        assertThat(captured.getName()).isEqualTo(customer.getName());
        assertThat(captured.getEmail()).isEqualTo(email);

    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "Sid", "sid@gmail.com", 20,
                new Random().nextBoolean() ? "MALE" : "FEMALE"
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null, 43, null
        );

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer captured = customerArgumentCaptor.getValue();

        assertThat(captured.getAge()).isEqualTo(updateRequest.age());
        assertThat(captured.getName()).isEqualTo(customer.getName());
        assertThat(captured.getEmail()).isEqualTo(customer.getEmail());
        assertThat(captured.getGender()).isEqualTo(customer.getGender());

    }

    @Test
    void canUpdateOnlyCustomerGender() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "Sid", "sid@gmail.com", 20,
                new Random().nextBoolean() ? "MALE" : "FEMALE"
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null,
                null,
                null,
                Objects.equals(customer.getGender(), "MALE") ? "FEMALE" : "MALE"
        );

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer captured = customerArgumentCaptor.getValue();

        assertThat(captured.getGender()).isEqualTo(updateRequest.gender());
        assertThat(captured.getName()).isEqualTo(customer.getName());
        assertThat(captured.getEmail()).isEqualTo(customer.getEmail());
        assertThat(captured.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void willThrowWhenCustomerEmailAlreadyExists() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "Sid", "sid@gmail.com", 20,
                new Random().nextBoolean() ? "MALE" : "FEMALE"
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));


        String email = "cyborg@sid.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, email, null, null
        );

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken.");

        verify(customerDao, never()).updateCustomer(any());

    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        int id = 1;
        Customer customer = new Customer(
                id, "Sid", "sid@gmail.com", 20,
                new Random().nextBoolean() ? "MALE" : "FEMALE"
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge(), customer.getGender()
        );

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No changes found!");

        verify(customerDao, never()).updateCustomer(any());

    }
}