package mate.academy.bookstoreprod.service.shoppingcart;

import mate.academy.bookstoreprod.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookstoreprod.model.User;

public interface ShoppingCartService {

    void createShoppingCart(User user);

    ShoppingCartResponseDto getShoppingCartForCurrentUser(Long userId);

    ShoppingCartResponseDto addItemToCart(Long userId, Long bookId, int quantity);

    ShoppingCartResponseDto updateCartItemQuantity(Long shoppingCartId,
                                                   Long cartItemId,
                                                   int quantity);

    void deleteCartItem(Long shoppingCartId, Long cartItemId);
}
