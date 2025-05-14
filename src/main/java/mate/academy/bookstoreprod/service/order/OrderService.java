package mate.academy.bookstoreprod.service.order;

import mate.academy.bookstoreprod.dto.order.AddOrderRequestDto;
import mate.academy.bookstoreprod.dto.order.OrderResponseDto;
import mate.academy.bookstoreprod.dto.orderitem.OrderItemResponseDto;
import mate.academy.bookstoreprod.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(Long userId, AddOrderRequestDto request);

    Page<OrderResponseDto> getUserOrders(Long userId, Pageable pageable);

    Page<OrderItemResponseDto> getOrderItems(Long orderId, Long userId, Pageable pageable);

    OrderItemResponseDto getSpecificOrderItem(Long orderId, Long itemId, Long userId);

    OrderResponseDto updateOrderStatus(Long orderId, Status status);
}

