package mate.academy.bookstoreprod.controller;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.shoppingCart.ShoppingCartResponseDto;
import mate.academy.bookstoreprod.model.ShoppingCart;
import mate.academy.bookstoreprod.service.shoppingCart.ShoppingCartService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @GetMapping
    public ShoppingCartResponseDto getShoppingCart() {
        return cartService.getShoppingCartForCurrentUser();
    }

    @PostMapping
    public ShoppingCartResponseDto addItemToShoppingCart(@RequestParam Long bookId, @RequestParam int quantity) {
        return cartService.addItemToCart(bookId, quantity);
    }

    @PutMapping("/cart-items/{cartItemId}")
    public ShoppingCartResponseDto updateCartItemQuantity(@PathVariable Long cartItemId, @RequestParam int quantity) {
        return cartService.updateCartItemQuantity(cartItemId, quantity);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    public void deleteItemFromShoppingCart(@PathVariable Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
    }
}
