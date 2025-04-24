package mate.academy.bookstoreprod.repository.book;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.bookstoreprod.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CategorySpecification implements SpecificationProvider<Book> {
    private static final String CATEGORY = "categories";

    @Override
    public String getKey() {
        return CATEGORY;
    }

    @Override
    public Specification<Book> getSpecification(String[] categories) {
        Set<Long> categoryIds = Arrays.stream(categories)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
        return (root, query, criteriaBuilder) -> root.join(CATEGORY).get("id").in(categoryIds);
    }
}
