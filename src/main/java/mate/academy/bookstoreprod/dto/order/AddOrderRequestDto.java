package mate.academy.bookstoreprod.dto.order;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddOrderRequestDto {
    @NotEmpty
    private String shippingAddress;
}
