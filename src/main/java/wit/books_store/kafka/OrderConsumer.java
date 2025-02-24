package wit.books_store.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import wit.books_store.dto.OrderDto;
import wit.books_store.dto.OrderResult;
import wit.books_store.services.OrderService;

@Service
@AllArgsConstructor
@Slf4j
public class OrderConsumer {

    private OrderService orderService;
    private KafkaTemplate<String, OrderResult> kafkaTemplate;


    @KafkaListener(topics = "orders-topic", groupId = "order-group")
    public void consumerOrder(OrderDto orderDto) {
        try {
            log.info("Received Order: {}", orderDto);
            orderService.create(orderDto);
            kafkaTemplate.send("orders-reply-topic", new OrderResult(true, "Order created"));
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage());
            kafkaTemplate.send("orders-reply-topic", new OrderResult(false, e.getMessage()));
        }
    }
}
