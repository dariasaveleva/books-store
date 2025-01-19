package wit.books_store.models;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOOKS")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {
    Long book_id;
    String title;
    String author;
    int price;
    boolean isPresent;
}
