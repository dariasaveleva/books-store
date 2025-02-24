package wit.books_store.kafka;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import wit.books_store.dto.OrderDto;
import wit.books_store.dto.OrderResult;
import wit.books_store.exceptions.KafkaProcessingException;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderProducer {
    KafkaTemplate<String, OrderDto> kafkaTemplate;

    public CompletableFuture<OrderResult> sendOrder(OrderDto orderDto) {
        CompletableFuture<SendResult<String, OrderDto>> future = kafkaTemplate.send("orders-topic", orderDto);
        return future.handle((result, ex) -> {
            if (ex != null) {
                throw new KafkaProcessingException("Failed to send order to Kafka: " + ex.getMessage());
            } else {
                return new OrderResult(true, "Order was sent to Kafka successfully");
            }
        });
    }
}
