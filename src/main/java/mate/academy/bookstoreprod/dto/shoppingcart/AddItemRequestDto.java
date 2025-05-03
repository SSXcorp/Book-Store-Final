package mate.academy.bookstoreprod.dto.shoppingcart;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AddItemRequestDto {
    @PositiveOrZero
    private Long bookId;
    @Positive
    private int quantity;
}
