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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper mapper;

    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartForCurrentUser(Long userId) {
        return mapper.toDto(shoppingCartRepository.findByUserId(userId));
    }

    @Override
    public ShoppingCartResponseDto addItemToCart(Long userId, Long bookId, int quantity) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: "
                        + bookId));

        CartItem givenBook = cartItemRepository.findByIdAndShoppingCartId(userId, bookId)
                .orElse(null);

        if (givenBook != null) {
            givenBook.setQuantity(givenBook.getQuantity() + quantity);
            cartItemRepository.save(givenBook);
        } else {
            addCartItem(book, quantity, cart);
        }
        return mapper.toDto(shoppingCartRepository.save(cart));
    }

    private void addCartItem(Book book, int quantity, ShoppingCart cart) {
        CartItem newItem = new CartItem();
        newItem.setBook(book);
        newItem.setQuantity(quantity);
        newItem.setShoppingCart(cart);
        cart.getCartItems().add(newItem);
    }

    @Override
    public ShoppingCartResponseDto updateCartItemQuantity(Long shoppingCartId,
                                                          Long cartItemId,
                                                          int quantity) {
        CartItem item = cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found cartItem id: "
                        + cartItemId + ", shoppingCartId: " + shoppingCartId));

        if (item.getQuantity() != quantity) {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        ShoppingCart fullCart = shoppingCartRepository.findShoppingCartById(shoppingCartId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found with id: "
                        + shoppingCartId));

        return mapper.toDto(fullCart);
    }

    @Override
    public void deleteCartItem(Long shoppingCartId, Long cartItemId) {
        CartItem item = cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found cartItem id: "
                        + cartItemId + ", shoppingCartId: " + shoppingCartId));

        ShoppingCart cart = item.getShoppingCart();
        cart.getCartItems().remove(item);
        cartItemRepository.delete(item);
    }
}
