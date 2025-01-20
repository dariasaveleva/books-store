package wit.books_store.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ORDERS")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    Long order_id;
    @NotNull(message = "order cannot be created without books")
    List<Long> books;
    @NotNull(message = "customerId cannot be empty")
    Long customerId;
    LocalDate createdDate;
    Double sum;
}
