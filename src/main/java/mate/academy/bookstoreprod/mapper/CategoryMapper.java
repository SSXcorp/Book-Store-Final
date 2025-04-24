package mate.academy.bookstoreprod.mapper;

import mate.academy.bookstoreprod.config.MapperConfig;
import mate.academy.bookstoreprod.dto.category.CategoryDto;
import mate.academy.bookstoreprod.dto.category.CreateCategoryDto;
import mate.academy.bookstoreprod.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryDto toCategoryDto(Category category);

    Category toCategory(CreateCategoryDto categoryDto);
}
