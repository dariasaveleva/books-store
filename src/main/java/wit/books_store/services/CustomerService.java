package wit.books_store.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.exceptions.ValidationException;
import wit.books_store.models.Customer;
import wit.books_store.models.Order;
import wit.books_store.repository.CustomerRepository;
import wit.books_store.repository.OrderRepository;

import java.util.List;
import java.util.regex.Pattern;

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

    public Customer getByEmail(String email) {
        Customer customer = repository.findByEmail(email);
        log.info(customer != null ? "found the customer with email {}" : "customer with email {} does not exist", email);
        return customer;
    }

    public Customer getByPhone(String phone) {
        Customer customer = repository.findByPhone(phone);
        log.info(customer != null ? "found the customer with phone {}" : "customer with phone {} does not exist", phone);
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
        boolean isNew = checkIfCustomerNew(customer.getEmail(), customer.getPhone());
        boolean isEmailValid = isEmailValid(customer.getEmail());
        if (isNew && isEmailValid) {
            repository.save(customer);
            log.info("new customer was registered");
        } else {
            String errorText = !isNew ? "customer already exists" : "email is not valid";
            log.error(errorText);
            throw new ValidationException(errorText);
        }
    }

    private boolean checkIfCustomerNew(String email, String phone) {
        return getByEmail(email) == null && getByPhone(phone) == null;
    }

    private boolean isEmailValid(String email) {
        return Pattern.compile("([a-zA-Z0-9._-]+@[a-zA-Z=]+\\.[a-zA-Z]+)").matcher(email).matches();
    }



}
