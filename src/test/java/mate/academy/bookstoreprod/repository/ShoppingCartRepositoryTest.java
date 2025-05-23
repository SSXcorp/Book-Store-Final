package mate.academy.bookstoreprod.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;
import mate.academy.bookstoreprod.model.ShoppingCart;
import mate.academy.bookstoreprod.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {

    private static final Long EXISTING_USER_ID = 1L;
    private static final Long NON_EXISTING_USER_ID = 9999L;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("""
            Find ShoppingCart by user id returns ShoppingCart
            """)
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-shopping-carts.sql",
            "classpath:database/user/delete-all-from-users.sql",
            "classpath:database/user/add-one-user.sql",
            "classpath:database/shoppingcart/add-cart-for-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByUserId_WithExistingUserId_ReturnsCart() {
        ShoppingCart cart = shoppingCartRepository.findByUserId(EXISTING_USER_ID);

        assertNotNull(cart, "Expected cart to be found for user id: " + EXISTING_USER_ID);
        assertEquals(EXISTING_USER_ID, cart.getUser().getId());
    }

    @Test
    @DisplayName("""
            Find ShoppingCart by user id returns null
            """)
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-shopping-carts.sql",
            "classpath:database/user/delete-all-from-users.sql",
            "classpath:database/user/add-one-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByUserId_WithNonExistingId_ReturnsNull() {
        ShoppingCart cart = shoppingCartRepository.findByUserId(NON_EXISTING_USER_ID);

        assertNull(cart, "Expected null when no cart exists for user id: " + NON_EXISTING_USER_ID);
    }

    @Test
    @DisplayName("""
            Find ShoppingCart by id returns optional of cart
            """)
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-shopping-carts.sql",
            "classpath:database/user/delete-all-from-users.sql",
            "classpath:database/user/add-one-user.sql",
            "classpath:database/shoppingcart/add-cart-for-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findShoppingCartById_WithExistingUserId_ReturnsCart() {
        ShoppingCart cart = shoppingCartRepository.findShoppingCartById(EXISTING_USER_ID).get();

        assertNotNull(cart, "Expected cart to be found for user id: " + EXISTING_USER_ID);
        assertEquals(EXISTING_USER_ID, cart.getUser().getId());
    }

    @Test
    @DisplayName("""
            Find ShoppingCart by id returns Optional empty
            """)
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-all-from-shopping-carts.sql",
            "classpath:database/user/delete-all-from-users.sql",
            "classpath:database/user/add-one-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findShoppingCartById_WithNonExistingId_ReturnsOptionEmpty() {
        Optional<ShoppingCart> cart =
                shoppingCartRepository.findShoppingCartById(NON_EXISTING_USER_ID);

        assertFalse(cart.isPresent(), "Expected cart to be found for user id: "
                + NON_EXISTING_USER_ID);
    }
}
