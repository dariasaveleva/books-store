package wit.books_store.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.exceptions.ValidationException;
import wit.books_store.models.Book;
import wit.books_store.models.Customer;
import wit.books_store.models.Order;
import wit.books_store.repository.BookRepository;
import wit.books_store.repository.CustomerRepository;
import wit.books_store.repository.OrderRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderService service;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    Order order0;
    Order order1;
    Order order2;
    Book book0;
    Book book1;
    Book book2;
    Customer customer0;
    Customer customer1;

    @BeforeEach
    void createOrder() {
        book0 =  Book.builder()
                .book_id(0L)
                .title("A rabbit")
                .author("J. Dan")
                .price(579)
                .isPresent(true)
                .build();

        book1 =  Book.builder()
                .book_id(1L)
                .title("Harry Potter")
                .author("J.K. Rowling")
                .price(1599)
                .isPresent(false)
                .build();
        book2 =  Book.builder()
                .book_id(2L)
                .title("Inside")
                .author("D. Humphrey")
                .price(3999)
                .isPresent(true)
                .build();


        customer0 = Customer.builder()
                .customer_id(0L)
                .name("Stella")
                .surname("Greak")
                .email("stellaG@mail.com")
                .phone("89173543674")
                .build();

        customer1 = Customer.builder()
                .customer_id(1L)
                .name("Lessi")
                .surname("Toprok")
                .email("lessi@mail.com")
                .phone("89123643489")
                .build();


        order0 = Order.builder()
                .order_id(0L)
                .books(List.of(book0.getBook_id(), book1.getBook_id()))
                .customerId(customer0.getCustomer_id())
                .createdDate(OffsetDateTime.parse("2025-02-28T18:24:45+03:00"))
                .sum((double) book1.getPrice() + book0.getPrice())
                .build();

        order1 = Order.builder()
                .order_id(1L)
                .books(List.of(book0.getBook_id(), book2.getBook_id()))
                .customerId(customer1.getCustomer_id())
                .createdDate(OffsetDateTime.parse("2025-02-04T20:00:00+03:00"))
                .sum((double) book0.getPrice() + book2.getPrice())
                .build();

        order2 = Order.builder()
                .books(List.of(book0.getBook_id(), book2.getBook_id()))
                .customerId(customer1.getCustomer_id())
                .build();
    }


    @Test
    void shouldReturnOrderIfExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(order1));
        Order foundOrder = service.getById(1L);
        assertEquals(4578.0, foundOrder.getSum());
        assertEquals(1L, foundOrder.getCustomerId());
        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnErrorIfOrderNotExist() {
        OrderService OrderService = mock(OrderService.class);
        when(OrderService.getById(1L)).thenThrow(new NotFoundException("Order is not found"));
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> OrderService.getById(1L));
        assertEquals("Order is not found", thrown.getMessage());
    }

    @Test
    void shouldReturnListOfOrders() {
        Pageable pageable = PageRequest.of(1, 10);
        when(repository.findAll(pageable)).thenReturn(List.of(order0, order1));
        service.findAll(pageable);
        verify(repository).findAll(pageable);
    }

    @Test
    void shouldCountSum() {
        when(bookRepository.findBooksByIds(order0.getBooks())).thenReturn(List.of(book0, book1));
        assertEquals(order0.getSum(), service.countSum(order0.getBooks()));
    }

    @Test
    void shouldCreateOrder() {
        when(customerRepository.findById(customer1.getCustomer_id())).thenReturn(Optional.of(customer1));
        when(bookRepository.findBooksByIds(List.of(book0.getBook_id(), book2.getBook_id()))).thenReturn(List.of(book0, book2));
        service.create(order2);
        verify(repository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertEquals(savedOrder.getCustomerId(), order2.getCustomerId());
        assertEquals(savedOrder.getSum(), service.countSum(order2.getBooks()));
    }

    @Test
    void shouldReturnErrorIfOrderIsNotValid() {
        assertThrows(ValidationException.class, () -> service.create(order2));
    }
}