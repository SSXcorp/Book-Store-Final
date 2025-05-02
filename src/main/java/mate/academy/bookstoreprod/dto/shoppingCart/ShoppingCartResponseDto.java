package mate.academy.bookstoreprod.dto.shoppingCart;

import java.util.List;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private List<CartItemResponseDto> cartItems;
}
