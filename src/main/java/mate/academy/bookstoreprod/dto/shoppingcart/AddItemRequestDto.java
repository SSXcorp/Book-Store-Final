package mate.academy.bookstoreprod.dto.shoppingcart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddItemRequestDto {
    @Positive
    @NotNull
    private Long bookId;
    @Positive
    private int quantity;
}
