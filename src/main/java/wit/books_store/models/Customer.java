package wit.books_store.models;
import jakarta.validation.constraints.NotNull;
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
    Long customer_id;
    @NotNull(message = "Name cannot be null")
    String name;
    String surname;
    @NotNull(message = "email cannot be null")
    String email;
    @NotNull
    String phone;
}
