package mate.academy.bookstoreprod.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BookDto {
    private int id;
    private String title;
    private String author;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
