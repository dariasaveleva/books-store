package wit.books_store.models;
import lombok.Getter;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@Getter
@EqualsAndHashCode
public class Customer {
    private long customer_id;
    private String name;
    private String surname;
    private String email;
    private String phone;
}
