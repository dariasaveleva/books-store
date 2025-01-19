package wit.books_store.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wit.books_store.models.Order;
import wit.books_store.services.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public List<Order> getAll() {
        return orderService.findAll();
    }

    @GetMapping
    public Order getById(@RequestParam Long id) {
        return orderService.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createBook(@RequestBody Order order) {
        orderService.create(order);
        return "the order was created";
    }
}
