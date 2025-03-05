package wit.books_store.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import wit.books_store.dto.OrderDto;
import wit.books_store.mappers.OrderMapper;
import wit.books_store.services.OrderService;

@Service
@AllArgsConstructor
@Slf4j
public class OrderConsumer {

    private OrderService orderService;
    private OrderMapper orderMapper;

    @KafkaListener(topics = "orders-topic", groupId = "order-group")
    public void consumerOrder(OrderDto orderDto) {
            orderService.create(orderMapper.toOrder(orderDto));
    }
}
