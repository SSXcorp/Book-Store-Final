package mate.academy.bookstoreprod.mapper;

import mate.academy.bookstoreprod.config.MapperConfig;
import mate.academy.bookstoreprod.dto.order.OrderResponseDto;
import mate.academy.bookstoreprod.dto.orderitem.OrderItemResponseDto;
import mate.academy.bookstoreprod.model.Order;
import mate.academy.bookstoreprod.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toDto(Order order);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toItemDto(OrderItem item);
}
