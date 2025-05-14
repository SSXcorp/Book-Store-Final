package mate.academy.bookstoreprod.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import mate.academy.bookstoreprod.dto.orderitem.OrderItemResponseDto;
import mate.academy.bookstoreprod.model.Status;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private List<OrderItemResponseDto> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Status status;
}
