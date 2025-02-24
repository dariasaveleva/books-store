package wit.books_store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResult {
    private boolean success;
    private String message;
}

