package mate.academy.bookstoreprod.repository.cartitem;

import java.util.Optional;
import mate.academy.bookstoreprod.model.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = {"book", "shoppingCart"})
    Optional<CartItem> findByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);

    @EntityGraph(attributePaths = {"book", "shoppingCart"})
    Optional<CartItem> findByBookIdAndShoppingCartId(Long bookId, Long shoppingCartId);
}
