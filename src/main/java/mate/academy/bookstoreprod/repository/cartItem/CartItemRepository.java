package mate.academy.bookstoreprod.repository.cartItem;

import mate.academy.bookstoreprod.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
