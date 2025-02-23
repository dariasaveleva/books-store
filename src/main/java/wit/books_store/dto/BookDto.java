package wit.books_store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDto {
    long id;
    @NotNull(message = "Title cannot be null")
    String title;
    String author;
    @NotNull
    @Min(value = 0, message = "Price cannot be negative")
    int price;
    boolean isPresent;
}
