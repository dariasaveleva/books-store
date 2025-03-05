package wit.books_store.models;
import lombok.Builder;
import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Book {
    private long book_id;
    private String title;
    private String author;
    private int price;
    private boolean isPresent;
}
