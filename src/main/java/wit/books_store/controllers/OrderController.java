package wit.books_store.controllers;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wit.books_store.dto.OrderDto;
import wit.books_store.services.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public List<OrderDto> getAll(@PositiveOrZero @RequestParam (defaultValue = "0") int from,
                                 @PositiveOrZero @RequestParam (defaultValue = "10") int size) {
        return orderService.findAll(PageRequest.of(from/size, size));
    }

    @GetMapping("/{id}")
    public OrderDto getById(@PathVariable long id) {
        return orderService.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createBook(@RequestBody OrderDto orderDto) {
        orderService.create(orderDto);
    }
}
