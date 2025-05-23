package mate.academy.bookstoreprod.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.bookstoreprod.model.Status;

@Data
public class UpdateOrderStatusRequestDto {
    @NotNull
    private Status status;
}
