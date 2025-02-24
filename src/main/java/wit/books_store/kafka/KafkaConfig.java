package wit.books_store.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic ordersTopic() {
        return TopicBuilder.name("orders-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ordersReplyTopic() {
        return TopicBuilder.name("orders-reply-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
