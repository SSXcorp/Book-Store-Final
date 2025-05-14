package mate.academy.bookstoreprod.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.order.AddOrderRequestDto;
import mate.academy.bookstoreprod.dto.order.OrderResponseDto;
import mate.academy.bookstoreprod.dto.orderitem.OrderItemResponseDto;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.exception.OrderCreatingException;
import mate.academy.bookstoreprod.mapper.OrderMapper;
import mate.academy.bookstoreprod.model.CartItem;
import mate.academy.bookstoreprod.model.Order;
import mate.academy.bookstoreprod.model.OrderItem;
import mate.academy.bookstoreprod.model.ShoppingCart;
import mate.academy.bookstoreprod.model.Status;
import mate.academy.bookstoreprod.repository.order.OrderRepository;
import mate.academy.bookstoreprod.repository.orderitem.OrderItemRepository;
import mate.academy.bookstoreprod.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDto createOrder(Long userId, AddOrderRequestDto dto) {
        ShoppingCart cart = shoppingCartRepository.findShoppingCartById(userId).orElseThrow(
                () -> new EntityNotFoundException("Shopping cart not found with id: " + userId));

        if (cart.getCartItems().isEmpty()) {
            throw new OrderCreatingException("Cannot create order because no cart items found");
        }

        Order order = formOrder(cart, dto.getShippingAddress());

        BigDecimal total = BigDecimal.ZERO;
        Set<OrderItem> items = new HashSet<>();
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = createOrderItem(order, cartItem);
            items.add(orderItem);
            total = total.add(orderItem.getBook().getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }
        order.setTotal(total);
        order.setOrderItems(items);

        orderRepository.save(order);
        cart.getCartItems().clear();
        shoppingCartRepository.save(cart);
        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderResponseDto> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public Page<OrderItemResponseDto> getOrderItems(Long orderId, Long userId, Pageable pageable) {
        return orderItemRepository.findAllByOrderIdAndOrderUserId(orderId, userId, pageable)
                .map(orderMapper::toItemDto);
    }

    @Override
    public OrderItemResponseDto getSpecificOrderItem(Long orderId, Long itemId, Long userId) {
        OrderItem item = orderItemRepository.findByIdAndOrderIdAndOrderUserId(itemId,
                        orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found"));
        return orderMapper.toItemDto(item);
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(status);
        return orderMapper.toDto(orderRepository.save(order));
    }

    private OrderItem createOrderItem(Order order, CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        orderItem.setOrder(order);
        return orderItem;
    }

    private Order formOrder(ShoppingCart cart, String shippingAddress) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setShippingAddress(shippingAddress);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);
        return order;
    }
}
