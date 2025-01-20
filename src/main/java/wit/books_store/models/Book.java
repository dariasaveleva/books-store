package wit.books_store.models;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Title cannot be null")
    String title;
    String author;
    @NotNull
    @Min(value = 0, message = "Price cannot be negative")
    int price;
    boolean isPresent;
}
