package mate.academy.bookstoreprod.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import mate.academy.bookstoreprod.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.mapper.ShoppingCartMapper;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.model.CartItem;
import mate.academy.bookstoreprod.model.ShoppingCart;
import mate.academy.bookstoreprod.repository.book.BookRepository;
import mate.academy.bookstoreprod.repository.cartitem.CartItemRepository;
import mate.academy.bookstoreprod.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstoreprod.service.shoppingcart.ShoppingCartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    private static final Long ONE = 1L;
    private static final Long USER_ID = 1L;
    private static final Long BOOK_ID = 10L;
    private static final Long NON_EXISTING_ID = 999L;
    private static final Long CART_ITEM_ID = 1L;
    private static final Long TWO = 2L;
    private static final Long THREE = 3L;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ShoppingCartMapper mapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("Get cart for current user by user id returns ShoppingCartResponseDto")
    void getShoppingCartForCurrentUser_WithValidUserId_ReturnsShoppingCartResponseDto() {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(USER_ID);
        ShoppingCartResponseDto dto = new ShoppingCartResponseDto();
        dto.setId(USER_ID);

        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(cart);
        when(mapper.toDto(cart)).thenReturn(dto);

        ShoppingCartResponseDto result =
                shoppingCartService.getShoppingCartForCurrentUser(USER_ID);

        assertEquals(USER_ID, result.getId());
        verify(shoppingCartRepository).findByUserId(USER_ID);
    }

    @Test
    @DisplayName("Add item to cart returns ShoppingCartResponseDto")
    void addItemToCart_ValidNewBook_ReturnsShoppingCartResponseDto() {
        Book book = new Book();
        book.setId(BOOK_ID);

        ShoppingCart cart = new ShoppingCart();
        cart.setId(USER_ID);
        cart.setCartItems(new HashSet<>());

        ShoppingCartResponseDto expectedDto = new ShoppingCartResponseDto();
        expectedDto.setId(USER_ID);

        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(cart);
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(cartItemRepository.findByBookIdAndShoppingCartId(BOOK_ID, USER_ID))
                .thenReturn(Optional.empty());
        when(shoppingCartRepository.save(cart)).thenReturn(cart);
        when(mapper.toDto(cart)).thenReturn(expectedDto);

        ShoppingCartResponseDto result =
                shoppingCartService.addItemToCart(USER_ID, BOOK_ID, TWO.intValue());

        assertEquals(USER_ID, result.getId());
        assertEquals(ONE, cart.getCartItems().size());
    }

    @Test
    @DisplayName("Add already existing item to cart returns ShoppingCartResponseDto, adds quantity")
    void addItemToCart_ValidAlreadyAddedBook_IncreasesQuantityReturnsShoppingCartResponseDto() {
        Book book = new Book();
        book.setId(BOOK_ID);

        ShoppingCart cart = new ShoppingCart();
        cart.setId(USER_ID);
        cart.setCartItems(new HashSet<>());

        CartItem existingItem = new CartItem();
        existingItem.setId(CART_ITEM_ID);
        existingItem.setQuantity(ONE.intValue());
        existingItem.setBook(book);
        existingItem.setShoppingCart(cart);
        cart.getCartItems().add(existingItem);

        ShoppingCartResponseDto expectedDto = new ShoppingCartResponseDto();
        expectedDto.setId(USER_ID);

        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(cart);
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(cartItemRepository.findByBookIdAndShoppingCartId(BOOK_ID, USER_ID))
                .thenReturn(Optional.of(existingItem));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(inv -> inv.getArgument(0));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);
        when(mapper.toDto(cart)).thenReturn(expectedDto);

        ShoppingCartResponseDto result =
                shoppingCartService.addItemToCart(USER_ID, BOOK_ID, TWO.intValue());
        CartItem updatedItem = cart.getCartItems().iterator().next();

        assertEquals(USER_ID, result.getId());
        assertEquals(ONE, cart.getCartItems().size());
        assertEquals(3, updatedItem.getQuantity());
        verify(cartItemRepository).save(updatedItem);
        verify(mapper).toDto(cart);
    }

    @Test
    @DisplayName("Add item to cart with non existing Book throws EntityNotFoundException")
    void addItemToCart_BookDoesNotExists_ThrowsEntityNotFoundException() {
        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(new ShoppingCart());
        when(bookRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.addItemToCart(USER_ID, NON_EXISTING_ID, ONE.intValue()));

        verify(bookRepository).findById(NON_EXISTING_ID);
    }

    @Test
    @DisplayName("Update cart item quantity of existing item returns ShoppingCartResponseDto")
    void updateCartItemQuantity_ValidExistingCartItem_ReturnsDtoUpdatesQuantity() {
        CartItem item = new CartItem();
        item.setId(ONE);
        item.setQuantity(ONE.intValue());

        ShoppingCart cart = new ShoppingCart();
        cart.setId(USER_ID);

        ShoppingCartResponseDto expectedDto = new ShoppingCartResponseDto();
        expectedDto.setId(USER_ID);

        when(cartItemRepository.findByIdAndShoppingCartId(ONE, USER_ID))
                .thenReturn(Optional.of(item));
        when(shoppingCartRepository.findShoppingCartById(USER_ID)).thenReturn(Optional.of(cart));
        when(mapper.toDto(cart)).thenReturn(expectedDto);

        ShoppingCartResponseDto result =
                shoppingCartService.updateCartItemQuantity(USER_ID, ONE, THREE.intValue());

        assertEquals(USER_ID, result.getId());
        assertEquals(THREE, item.getQuantity());
    }

    @Test
    @DisplayName("Update cart item quantity throws exception when cart item does not exist")
    void updateCartItemQuantity_NonExistingCartItem_ThrowsEntityNotFoundException() {
        when(cartItemRepository.findByIdAndShoppingCartId(NON_EXISTING_ID, USER_ID))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.updateCartItemQuantity(
                        USER_ID,
                        NON_EXISTING_ID,
                        TWO.intValue())
        );

        assertTrue(exception.getMessage().contains("Cart item not found cartItem id: "
                + NON_EXISTING_ID));
        verify(cartItemRepository).findByIdAndShoppingCartId(NON_EXISTING_ID, USER_ID);
    }

    @Test
    @DisplayName("Update cart item quantity throws exception when shopping cart does not exist")
    void updateCartItemQuantity_NonExistingShoppingCart_ThrowsEntityNotFoundException() {
        CartItem item = new CartItem();
        item.setId(ONE);
        item.setQuantity(ONE.intValue());

        when(cartItemRepository.findByIdAndShoppingCartId(ONE, USER_ID))
                .thenReturn(Optional.of(item));
        when(shoppingCartRepository.findShoppingCartById(USER_ID))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.updateCartItemQuantity(USER_ID, ONE, TWO.intValue())
        );

        assertTrue(exception.getMessage().contains("Shopping cart not found with id: "
                + USER_ID));
        verify(cartItemRepository).findByIdAndShoppingCartId(ONE, USER_ID);
        verify(shoppingCartRepository).findShoppingCartById(USER_ID);
    }

    @Test
    @DisplayName("Delete existing cart item")
    void deleteCartItem_ValidExistingItem_NoContent() {
        ShoppingCart cart = new ShoppingCart();
        cart.setCartItems(new HashSet<>());

        CartItem item = new CartItem();
        item.setId(ONE);
        item.setShoppingCart(cart);
        cart.getCartItems().add(item);

        when(cartItemRepository.findByIdAndShoppingCartId(ONE, USER_ID))
                .thenReturn(Optional.of(item));

        shoppingCartService.deleteCartItem(USER_ID, ONE);

        assertTrue(cart.getCartItems().isEmpty());
        verify(cartItemRepository).delete(item);
    }

    @Test
    @DisplayName("Delete cart item throws exception when item not found")
    void deleteCartItem_NonExistingItem_ThrowsEntityNotFoundException() {
        when(cartItemRepository.findByIdAndShoppingCartId(ONE, USER_ID))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                shoppingCartService.deleteCartItem(USER_ID, ONE));

        assertTrue(exception.getMessage().contains("Cart item not found cartItem id: " + ONE));
        verify(cartItemRepository).findByIdAndShoppingCartId(ONE, USER_ID);
    }
}
