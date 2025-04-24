package mate.academy.bookstoreprod.dto.book;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BookSearchParametersDto {
    private String[] title;
    private String[] author;
    private String[] price;
    private String[] category;
}
