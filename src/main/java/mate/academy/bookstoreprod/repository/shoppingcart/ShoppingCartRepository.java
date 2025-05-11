package mate.academy.bookstoreprod.repository.shoppingcart;

import java.util.Optional;
import java.util.Set;
import mate.academy.bookstoreprod.model.CartItem;
import mate.academy.bookstoreprod.model.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByUserId(Long userId);

    @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
    Optional<ShoppingCart> findShoppingCartById(Long id);
}
