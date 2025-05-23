package mate.academy.bookstoreprod.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.shoppingcart.AddItemRequestDto;
import mate.academy.bookstoreprod.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookstoreprod.dto.shoppingcart.UpdateItemRequestDto;
import mate.academy.bookstoreprod.model.User;
import mate.academy.bookstoreprod.service.shoppingcart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "ShoppingCart management",
        description = "Endpoints for managing User's ShoppingCart entity")
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get ShoppingCart",
            description = "Get ShoppingCart for current user")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        Long id = getCurrentUserId(authentication);
        return cartService.getShoppingCartForCurrentUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add to ShoppingCart",
            description = "Add item to ShoppingCart")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ShoppingCartResponseDto addItemToShoppingCart(@RequestBody @Valid AddItemRequestDto dto,
                                                         Authentication authentication) {
        Long id = getCurrentUserId(authentication);
        return cartService.addItemToCart(id, dto.getBookId(), dto.getQuantity());
    }

    @PutMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update item quantity",
            description = "Update item quantity in ShoppingCart")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ShoppingCartResponseDto updateCartItemQuantity(@PathVariable Long cartItemId,
                                                      @RequestBody @Valid UpdateItemRequestDto dto,
                                                      Authentication authentication) {
        Long id = getCurrentUserId(authentication);
        return cartService.updateCartItemQuantity(id, cartItemId, dto.getQuantity());
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete item",
            description = "Delete item from ShoppingCart")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteItemFromShoppingCart(@PathVariable Long cartItemId,
                                           Authentication authentication) {
        Long id = getCurrentUserId(authentication);
        cartService.deleteCartItem(id, cartItemId);
    }

    private Long getCurrentUserId(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}
