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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper mapper;

    @Override
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

        CartItem givenBookItem = cartItemRepository.findByBookIdAndShoppingCartId(bookId, userId)
                .orElse(null);

        if (givenBookItem != null) {
            givenBookItem.setQuantity(givenBookItem.getQuantity() + quantity);
            cartItemRepository.save(givenBookItem);
        } else {
            addCartItem(book, quantity, cart);
        }
        return mapper.toDto(shoppingCartRepository.save(cart));
    }

    @Override
    public ShoppingCartResponseDto updateCartItemQuantity(Long userId,
                                                          Long cartItemId,
                                                          int quantity) {
        CartItem item = cartItemRepository.findByIdAndShoppingCartId(cartItemId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found cartItem id: "
                        + cartItemId + ", shoppingCartId: " + userId));

        if (item.getQuantity() != quantity) {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        ShoppingCart fullCart = shoppingCartRepository.findShoppingCartById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found with id: "
                        + userId));

        return mapper.toDto(fullCart);
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) {
        CartItem item = cartItemRepository.findByIdAndShoppingCartId(cartItemId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found cartItem id: "
                        + cartItemId + ", userId: " + userId));

        ShoppingCart cart = item.getShoppingCart();
        cart.getCartItems().remove(item);
        cartItemRepository.delete(item);
    }

    private void addCartItem(Book book, int quantity, ShoppingCart cart) {
        CartItem newItem = new CartItem();
        newItem.setBook(book);
        newItem.setQuantity(quantity);
        newItem.setShoppingCart(cart);
        cart.getCartItems().add(newItem);
    }
}
