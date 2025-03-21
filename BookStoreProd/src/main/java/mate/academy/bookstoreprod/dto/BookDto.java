package mate.academy.bookstoreprod.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookDto {
    private int id;
    private String title;
    private String author;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
