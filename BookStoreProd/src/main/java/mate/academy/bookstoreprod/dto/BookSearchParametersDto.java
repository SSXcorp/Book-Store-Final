package mate.academy.bookstoreprod.dto;

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
}
