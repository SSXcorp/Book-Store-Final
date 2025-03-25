package mate.academy.bookstoreprod.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CreateBookRequestDto {
    @NotNull()
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9 .,'\"-]*")
    private String title;
    @NotNull()
    @Pattern(regexp = "^[A-Za-z]+([ '-][A-Za-z]+)*$")
    private String author;
    @NotNull()
    private String isbn;
    @NotNull()
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
