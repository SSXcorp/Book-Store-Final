package mate.academy.bookstoreprod.service.shoppingCart;

import mate.academy.bookstoreprod.dto.shoppingCart.ShoppingCartResponseDto;
import mate.academy.bookstoreprod.model.ShoppingCart;

public interface ShoppingCartService {

    ShoppingCartResponseDto  getShoppingCartForCurrentUser();

    ShoppingCartResponseDto  addItemToCart(Long bookId, int quantity);

    ShoppingCartResponseDto updateCartItemQuantity(Long cartItemId, int quantity);

    void deleteCartItem(Long cartItemId);
}
