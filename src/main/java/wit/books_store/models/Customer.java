package wit.books_store.models;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CUSTOMERS")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    Long id;
    String name;
    String surname;
    String email;
    String phone;
}
