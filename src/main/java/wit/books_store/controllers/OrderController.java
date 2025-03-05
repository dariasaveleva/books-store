package wit.books_store.controllers;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wit.books_store.dto.OrderDto;
import wit.books_store.kafka.OrderProducer;
import wit.books_store.mappers.OrderMapper;
import wit.books_store.services.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderProducer orderProducer;
    private final OrderMapper orderMapper;

    @GetMapping("/all")
    public List<OrderDto> getAll(@PositiveOrZero @RequestParam (defaultValue = "0") int from,
                                 @PositiveOrZero @RequestParam (defaultValue = "10") int size) {
        return orderService.findAll(PageRequest.of(from/size, size))
                .stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDto getById(@PathVariable long id) {
        return orderMapper.toOrderDto(orderService.getById(id));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody OrderDto orderDto) {
         orderProducer.sendOrder(orderDto);
    }
}
