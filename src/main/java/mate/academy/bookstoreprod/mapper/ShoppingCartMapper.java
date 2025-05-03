package mate.academy.bookstoreprod.mapper;

import mate.academy.bookstoreprod.config.MapperConfig;
import mate.academy.bookstoreprod.dto.shoppingcart.CartItemResponseDto;
import mate.academy.bookstoreprod.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookstoreprod.model.CartItem;
import mate.academy.bookstoreprod.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartResponseDto toDto(ShoppingCart cart);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toDto(CartItem cartItem);

}
