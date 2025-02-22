package wit.books_store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CustomerDto {
    long customer_id;
    @NotNull(message = "Name cannot be null")
    String name;
    String surname;
    @NotNull(message = "email cannot be null")
    @Email(message = "email is not valid")
    String email;
    @NotNull
    String phone;
}

