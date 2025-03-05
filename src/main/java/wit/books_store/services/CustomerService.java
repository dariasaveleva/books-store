package wit.books_store.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wit.books_store.exceptions.DuplicationException;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.models.Customer;
import wit.books_store.models.Order;
import wit.books_store.repository.CustomerRepository;
import wit.books_store.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository repository;
    private final OrderRepository orderRepository;

    public List<Customer> findAll(Pageable pageable) {
        log.info("show all customers");
        return repository.findAll(pageable);
    }

    public Customer getById(long id) {
        Customer customer = repository.findById(id).orElseThrow(() -> new NotFoundException("customer not found"));
        log.info("found the customer with id {}", id);
        return customer;
    }

    public Customer getByEmailOrPhone(String email, String phone) {
        Optional <Customer> customer = repository.findByEmailOrPhone(email, phone);
        log.info(customer.isPresent() ? "customer found" : "customer already exists with email {} or phone {}", email, phone);
        return customer.orElse(null);
    }

    public List<Order> getOrdersByCustomer(long id) {
        List<Order> orders = orderRepository.getOrdersByCustomer(id);
        log.info(orders.isEmpty() ? "customer made no orders" : "show customer's orders");
        return orders;
    }

    public void create(Customer customer) {
        if (checkIfCustomerNew(customer.getEmail(), customer.getPhone())) {
            repository.save(customer);
            log.info("new customer was registered");
        } else {
            String errorText = "customer already exists";
            log.error(errorText);
            throw new DuplicationException(errorText);
        }
    }

    public boolean checkIfCustomerNew(String email, String phone) {
        return getByEmailOrPhone(email, phone) == null;
    }



}
