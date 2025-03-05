package wit.books_store.models;

import lombok.Getter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Order {
    private long order_id;
    private List<Long> books;
    private long customerId;
    private OffsetDateTime createdDate;
    private Double sum;
}
