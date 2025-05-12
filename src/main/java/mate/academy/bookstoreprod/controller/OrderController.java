package mate.academy.bookstoreprod.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.order.AddOrderRequestDto;
import mate.academy.bookstoreprod.dto.order.OrderResponseDto;
import mate.academy.bookstoreprod.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.bookstoreprod.dto.orderitem.OrderItemResponseDto;
import mate.academy.bookstoreprod.model.User;
import mate.academy.bookstoreprod.service.order.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Order management",
        description = "Endpoints for managing User's Order entity")
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Add Order",
            description = "Add a new Order")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public OrderResponseDto addOrder(@RequestBody @Valid AddOrderRequestDto order,
                                     Authentication authentication) {
        Long id = getCurrentUserId(authentication);
        return orderService.createOrder(id, order);
    }

    @GetMapping
    @Operation(summary = "Get all User's orders",
            description = "Get all User's orders")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Page<OrderResponseDto> getUserOrders(Authentication authentication,
                                                Pageable pageable) {
        Long id = getCurrentUserId(authentication);
        return orderService.getUserOrders(id, pageable);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all OrderItems",
            description = "Get all OrderItems from the given order")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Page<OrderItemResponseDto> getOrderItems(@PathVariable Long orderId,
                                                    Authentication authentication,
                                                    Pageable pageable) {
        Long id = getCurrentUserId(authentication);
        return orderService.getOrderItems(id, orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get OrderItem by id",
            description = "Get specific OrderItem by id")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public OrderItemResponseDto getSpecificOrderItem(@PathVariable Long orderId,
                                     @PathVariable Long id,
                                     Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return orderService.getSpecificOrderItem(orderId, id, userId);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update Order status by id",
            description = "Update Order status by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id,
                                              @RequestBody UpdateOrderStatusRequestDto dto) {
        return orderService.updateOrderStatus(id, dto.getStatus());
    }

    private Long getCurrentUserId(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}
