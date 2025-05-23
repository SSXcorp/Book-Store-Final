package mate.academy.bookstoreprod.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.bookstoreprod.dto.shoppingcart.AddItemRequestDto;
import mate.academy.bookstoreprod.dto.shoppingcart.UpdateItemRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {

    protected static MockMvc mockMvc;

    private static final String CART_ITEM_URI = "/cart/cart-items/{cartItemId}";
    private static final String CART_URI = "/cart";
    private static final Long NONEXISTING_CART_ITEM = 999L;
    private static final Long ONE = 1L;
    private static final int QUANTITY = 2;
    private static final int UPDATE_QUANTITY = 5;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUp(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Get shopping cart returns ShoppingCartResponseDto")
    @WithUserDetails(value = "admin@admin.com")
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-cart-items.sql",
            "classpath:database/shoppingcart/delete-all-from-shopping-carts.sql",
            "classpath:database/user/delete-all-from-users.sql",
            "classpath:database/user/add-one-user.sql",
            "classpath:database/shoppingcart/add-cart-for-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-cart-items.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getShoppingCart_ValidInput_ReturnsShoppingCartResponseDto() throws Exception {
        mockMvc.perform(get(CART_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Add new item to shopping cart returns ShoppingCartResponseDto")
    @WithUserDetails(value = "admin@admin.com")
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-cart-items.sql",
            "classpath:database/shoppingcart/delete-all-from-shopping-carts.sql",
            "classpath:database/user/delete-all-from-users.sql",
            "classpath:database/book/delete-all-from-books.sql",
            "classpath:database/book/add-one-book.sql",
            "classpath:database/user/add-one-user.sql",
            "classpath:database/shoppingcart/add-cart-for-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-cart-items.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addItemToShoppingCart_WithNewItem_ReturnsUpdatedCart() throws Exception {
        AddItemRequestDto request = new AddItemRequestDto();
        request.setBookId(ONE);
        request.setQuantity(QUANTITY);

        mockMvc.perform(post(CART_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems").isArray());
    }

    @Test
    @DisplayName("Add item already present in shopping cart returns ShoppingCartResponseDto")
    @WithUserDetails(value = "admin@admin.com")
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-cart-items.sql",
            "classpath:database/shoppingcart/delete-all-from-shopping-carts.sql",
            "classpath:database/user/delete-all-from-users.sql",
            "classpath:database/book/delete-all-from-books.sql",
            "classpath:database/book/add-one-book.sql",
            "classpath:database/user/add-one-user.sql",
            "classpath:database/shoppingcart/add-cart-for-user.sql",
            "classpath:database/shoppingcart/add-cart-item-for-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-cart-items.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addItemToShoppingCart_ExistingItem_UpdatesQuantity() throws Exception {
        AddItemRequestDto request = new AddItemRequestDto();
        request.setBookId(ONE);
        request.setQuantity(QUANTITY);

        mockMvc.perform(post(CART_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems").isArray())
                .andExpect(jsonPath("$.cartItems[?(@.bookId == 1)].quantity").value(3));
    }

    @Test
    @DisplayName("Update valid cart item quantity returns ShoppingCartResponseDto")
    @WithUserDetails(value = "admin@admin.com")
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-cart-items.sql",
            "classpath:database/shoppingcart/delete-all-from-shopping-carts.sql",
            "classpath:database/user/delete-all-from-users.sql",
            "classpath:database/book/delete-all-from-books.sql",
            "classpath:database/user/add-one-user.sql",
            "classpath:database/shoppingcart/add-cart-for-user.sql",
            "classpath:database/book/add-one-book.sql",
            "classpath:database/shoppingcart/add-one-cart-item.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-cart-items.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCartItemQuantity_WithValidCartItem_ReturnsUpdatedCart() throws Exception {
        UpdateItemRequestDto request = new UpdateItemRequestDto();
        request.setQuantity(UPDATE_QUANTITY);

        mockMvc.perform(put(CART_ITEM_URI, ONE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems[0].quantity").value(UPDATE_QUANTITY));
    }

    @Test
    @DisplayName("Update non-existent cart item returns 404 Not Found")
    @WithUserDetails(value = "admin@admin.com")
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-cart-items.sql",
            "classpath:database/shoppingcart/delete-all-from-shopping-carts.sql",
            "classpath:database/user/delete-all-from-users.sql",
            "classpath:database/book/delete-all-from-books.sql",
            "classpath:database/user/add-one-user.sql",
            "classpath:database/shoppingcart/add-cart-for-user.sql",
            "classpath:database/book/add-one-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-cart-items.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateNonExistentCartItem_WithNonexistingCartItem_ReturnsNotFound() throws Exception {
        UpdateItemRequestDto request = new UpdateItemRequestDto();
        request.setQuantity(5);

        mockMvc.perform(put(CART_ITEM_URI, NONEXISTING_CART_ITEM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cart item not found cartItem id: 999,"
                        + " shoppingCartId: 1"));
    }

    @Test
    @DisplayName("Delete cart item by id")
    @WithUserDetails(value = "admin@admin.com")
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-cart-items.sql",
            "classpath:database/shoppingcart/delete-all-from-shopping-carts.sql",
            "classpath:database/user/delete-all-from-users.sql",
            "classpath:database/book/delete-all-from-books.sql",
            "classpath:database/user/add-one-user.sql",
            "classpath:database/shoppingcart/add-cart-for-user.sql",
            "classpath:database/book/add-one-book.sql",
            "classpath:database/shoppingcart/add-one-cart-item.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-cart-items.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCartItem_NoContent() throws Exception {
        mockMvc.perform(delete(CART_ITEM_URI, ONE))
                .andExpect(status().isNoContent());
    }
}
