package wit.books_store.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wit.books_store.models.Customer;
import wit.books_store.models.Order;
import wit.books_store.services.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/all")
    public List<Customer> getAll() {
        return customerService.findAll();
    }

    @GetMapping
    public Customer getById(@RequestParam Long id) {
        return customerService.getById(id);
    }

    @GetMapping("/orders")
    public List<Order> getOrdersByCustomer(@RequestParam Long id) {
        return customerService.getOrdersByCustomer(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createBook(@RequestBody Customer customer) {
        customerService.create(customer);
        return "the customer was created";
    }
}
