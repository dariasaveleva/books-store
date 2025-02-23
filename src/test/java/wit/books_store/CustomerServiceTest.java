package wit.books_store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import wit.books_store.dto.CustomerDto;
import wit.books_store.exceptions.DuplicationException;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.models.Customer;
import wit.books_store.repository.CustomerRepository;
import wit.books_store.services.CustomerService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerService service;

    Customer customer0;
    Customer customer1;

    @BeforeEach
    void createCustomer() {
        customer0 = new Customer(0L, "Stella", "Greak", "stellaG@mail.com", "89173543674");
        customer1 = new Customer(1L, "Lessi", "Toprok", "lessi@mail.com", "89123643489");
    }


    @Test
    void shouldReturnCustomerIfExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(customer0));
        CustomerDto foundCustomer = service.getById(1L);
        assertEquals("Stella", foundCustomer.getName());
        assertEquals("Greak", foundCustomer.getSurname());
        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnErrorIfCustomerNotExist() {
        CustomerService customerService = mock(CustomerService.class);
        when(customerService.getById(1L)).thenThrow(new NotFoundException("Customer is not found"));
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> customerService.getById(1L));
        assertEquals("Customer is not found", thrown.getMessage());
    }

    @Test
    void shouldReturnListOfCustomers() {
        Pageable pageable = PageRequest.of(1, 10);
        when(repository.findAll(pageable)).thenReturn(List.of(customer0, customer1));
        service.findAll(pageable);
        verify(repository).findAll(pageable);
    }

    @Test
    void shouldReturnCustomerByEmail() {
        String email = "stellaG@mail.com";
        String phone = "89162632637";
        when(repository.findByEmailOrPhone(email, phone)).thenReturn(Optional.of(customer0));
        service.getByEmailOrPhone(email, phone);
        verify(repository).findByEmailOrPhone(email, phone);
    }

    @Test
    void shouldReturnCustomerByPhone() {
        String email = "abcdefG@mail.com";
        String phone = "89123643489";
        when(repository.findByEmailOrPhone(email, phone)).thenReturn(Optional.of(customer1));
        assertEquals(Mapper.toCustomerDto(customer1), service.getByEmailOrPhone(email, phone));
        verify(repository).findByEmailOrPhone(email, phone);
    }

    @Test
    void shouldNotReturnCustomerByEmailOrPhone() {
        String email = "abcdefG@mail.com";
        String phone = "89888888888";
        when(repository.findByEmailOrPhone(email, phone)).thenReturn(Optional.empty());
        assertNull( service.getByEmailOrPhone(email, phone));
        verify(repository).findByEmailOrPhone(email, phone);
    }

    @Test
    void shouldCreateCustomer() {
        service.create(Mapper.toCustomerDto(customer0));
        verify(repository).save(customer0);
    }

    @Test
    void shouldReturnErrorIfCustomerAlreadyExists() {
        when(repository.findByEmailOrPhone("stellaG@mail.com", "89173543674")).thenReturn(Optional.of(customer0));
        assertThrows(DuplicationException.class, () -> service.create(Mapper.toCustomerDto(customer0)));
    }
}
