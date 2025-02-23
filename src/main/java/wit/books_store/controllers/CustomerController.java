package wit.books_store.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wit.books_store.dto.CustomerDto;
import wit.books_store.models.Order;
import wit.books_store.services.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/all")
    public List<CustomerDto> getAll(@PositiveOrZero @RequestParam (defaultValue = "0") int from,
                                    @PositiveOrZero @RequestParam (defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(from/size, size);
        return customerService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public CustomerDto getById(@PathVariable long id) {
        return customerService.getById(id);
    }

    @GetMapping("/orders")
    public List<Order> getOrdersByCustomer(@RequestParam long id) {
        return customerService.getOrdersByCustomer(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createCustomer(@Valid @RequestBody CustomerDto customer) {
        customerService.create(customer);
        return "the customer was created";
    }
}
