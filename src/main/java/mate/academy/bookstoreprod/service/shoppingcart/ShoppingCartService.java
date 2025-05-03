package mate.academy.bookstoreprod.service.shoppingcart;

import mate.academy.bookstoreprod.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {

    ShoppingCartResponseDto getShoppingCartForCurrentUser();

    ShoppingCartResponseDto addItemToCart(Long bookId, int quantity);

    ShoppingCartResponseDto updateCartItemQuantity(Long cartItemId, int quantity);

    void deleteCartItem(Long cartItemId);
}
