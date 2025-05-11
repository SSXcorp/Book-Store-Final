package mate.academy.bookstoreprod.dto.shoppingcart;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateItemRequestDto {
    @Positive
    private int quantity;
}
