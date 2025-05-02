package mate.academy.bookstoreprod.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryDto {
    @NotBlank
    private String name;
    private String description;
}
