package wit.books_store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    long order_id;
    @NotNull(message = "order cannot be created without books")
    List<Long> books;
    @NotNull(message = "customerId cannot be empty")
    long customerId;
    OffsetDateTime createdDate;
    Double sum;
}
