package mate.academy.bookstoreprod.service.shoppingcart;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.mapper.ShoppingCartMapper;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.model.CartItem;
import mate.academy.bookstoreprod.model.ShoppingCart;
import mate.academy.bookstoreprod.model.User;
import mate.academy.bookstoreprod.repository.book.BookRepository;
import mate.academy.bookstoreprod.repository.cartitem.CartItemRepository;
import mate.academy.bookstoreprod.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstoreprod.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final ShoppingCartMapper mapper;

    @Override
    public ShoppingCartResponseDto getShoppingCartForCurrentUser() {
        return mapper.toDto(getShoppingCart());
    }

    @Override
    public ShoppingCartResponseDto addItemToCart(Long bookId, int quantity) {
        ShoppingCart cart = getShoppingCart();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: "
                        + bookId));

        CartItem givenBook = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .orElse(null);

        if (givenBook != null) {
            givenBook.setQuantity(givenBook.getQuantity() + quantity);
            cartItemRepository.save(givenBook);
        } else {
            CartItem newItem = new CartItem();
            newItem.setBook(book);
            newItem.setQuantity(quantity);
            newItem.setShoppingCart(cart);
            cart.getCartItems().add(newItem);
        }
        return mapper.toDto(shoppingCartRepository.save(cart));
    }

    @Override
    public ShoppingCartResponseDto updateCartItemQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with ID: "
                        + cartItemId));

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return mapper.toDto(item.getShoppingCart());
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with ID: "
                        + cartItemId));

        ShoppingCart cart = item.getShoppingCart();
        cart.getCartItems().remove(item);
        cartItemRepository.delete(item);
    }

    private ShoppingCart getShoppingCart() {
        User user = userService.getCurrentUser();

        return shoppingCartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart();
                    cart.setUser(user);
                    user.setShoppingCart(cart);
                    return shoppingCartRepository.save(cart);
                });
    }

}
