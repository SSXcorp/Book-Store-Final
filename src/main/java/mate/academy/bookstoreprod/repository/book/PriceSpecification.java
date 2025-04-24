package mate.academy.bookstoreprod.repository.book;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import mate.academy.bookstoreprod.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecification implements SpecificationProvider<Book> {
    private static final String PRICE = "price";

    @Override
    public String getKey() {
        return PRICE;
    }

    @Override
    public Specification<Book> getSpecification(String[] prices) {
        BigDecimal[] priceConverted = parsePrices(prices);
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.between(root.get(PRICE),
                    priceConverted[0], priceConverted[1]);
            return criteriaBuilder.and(predicate);
        };
    }

    private BigDecimal[] parsePrices(String[] prices) {
        BigDecimal[] result = new BigDecimal[prices.length];
        for (int i = 0; i < prices.length; i++) {
            result[i] = new BigDecimal(prices[i]);
        }
        return result;
    }
}
