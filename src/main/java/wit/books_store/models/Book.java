package wit.books_store.models;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BOOKS")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long book_id;
    @Column(name = "title")
    String title;
    @Column(name = "author")
    String author;
    @Column(name = "price")
    int price;
    @Column(name = "isPresent")
    boolean isPresent;
}
