package mate.academy.bookstoreprod.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
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
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9 .,'\"-]*")
    private String title;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+([ '-][A-Za-z]+)*$")
    private String author;
    @NotBlank
    private String isbn;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
    private String description;
    private String coverImage;
}
