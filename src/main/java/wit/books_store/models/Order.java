package wit.books_store.models;

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
    int id;
    List<Book> books;
    Customer customer;
    LocalDate createdDate;
    Double sum;
}
