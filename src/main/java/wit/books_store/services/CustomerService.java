package wit.books_store.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.models.Customer;
import wit.books_store.models.Order;
import wit.books_store.repository.CustomerRepository;
import wit.books_store.repository.OrderRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomerService {
    private final CustomerRepository repository;
    private final OrderRepository orderRepository;

    public List<Customer> findAll() {
        log.info("show all customers");
        return repository.findAll();
    }

    public Customer getById(Long id) {
        Customer customer = repository.findById(id);
        log.info("found the customer with id {}", id);
        return customer;
    }

    public List<Order> getOrdersByCustomer(Long id) {
        List<Order> orders = orderRepository.getOrdersByCustomer(id);
        if (orders.isEmpty()) {
            throw new NotFoundException("customer made no orders");
        }
        log.info("show customer's orders");
        return orders;
    }

    @Transactional()
    public void create(Customer customer) {
        repository.save(customer);
        log.info("new book was created");
    }
}
