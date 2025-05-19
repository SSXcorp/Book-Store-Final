package mate.academy.bookstoreprod.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstoreprod.dto.category.CategoryDto;
import mate.academy.bookstoreprod.dto.category.CreateCategoryDto;
import mate.academy.bookstoreprod.exception.EntityAlreadyExistsException;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.mapper.CategoryMapper;
import mate.academy.bookstoreprod.model.Category;
import mate.academy.bookstoreprod.repository.category.CategoryRepository;
import mate.academy.bookstoreprod.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    private static final Long ONE = 1L;
    private static final Long NONEXISTING_ID = 9999L;
    private static final String CATEGORY_NAME = "Action";
    private static final String UPDATED_CATEGORY_NAME = "Drama";
    private static final int TEN = 10;
    private static final int ZERO = 0;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify all categories; return pageable")
    public void findAll_Valid_ReturnsPageableCategoryDto() {
        Pageable pageable = PageRequest.of(ONE.intValue(), TEN);
        Category category = getCategory();
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        CategoryDto categoryDto = getCategoryDto();

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toCategoryDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> result = categoryService.findAll(pageable);

        assertNotNull(result);
        assertEquals(ONE, result.getTotalElements());
        assertEquals(CATEGORY_NAME, result.getContent().get(ZERO).getName());
        verify(categoryRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Verify category returns by valid ID")
    public void getById_WithValidId_ReturnsCategoryDto() {
        Category category = getCategory();
        CategoryDto categoryDto = getCategoryDto();

        when(categoryRepository.findById(ONE)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(ONE);

        assertNotNull(result);
        assertEquals(ONE, result.getId());
        assertEquals(CATEGORY_NAME, result.getName());
        verify(categoryRepository).findById(ONE);
    }

    @Test
    @DisplayName("Throws EntityNotFoundException when category not found by ID")
    public void getById_WithInvalidId_ThrowsEntityNotFoundException() {
        when(categoryRepository.findById(NONEXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(NONEXISTING_ID));
        verify(categoryRepository).findById(NONEXISTING_ID);
    }

    @Test
    @DisplayName("Verify category saves and returns DTO with valid input")
    public void save_WithValidInput_ReturnsCategoryDto() {
        CreateCategoryDto createDto = getCreateCategoryDto();
        Category category = getCategory();
        CategoryDto categoryDto = getCategoryDto();

        when(categoryRepository.existsByName(createDto.getName())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toCategory(createDto)).thenReturn(category);
        when(categoryMapper.toCategoryDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(createDto);

        assertNotNull(result);
        assertEquals(CATEGORY_NAME, result.getName());
        verify(categoryRepository).existsByName(CATEGORY_NAME);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Throws EntityAlreadyExistsException when saving duplicate category name")
    public void save_WithDuplicateName_ThrowsEntityAlreadyExistsException() {
        CreateCategoryDto createDto = getCreateCategoryDto();

        when(categoryRepository.existsByName(CATEGORY_NAME)).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> categoryService.save(createDto));
        verify(categoryRepository).existsByName(CATEGORY_NAME);
    }

    @Test
    @DisplayName("Verify updated category returns by valid input")
    public void update_WithValidIdAndInput_ReturnsUpdatedCategoryDto() {
        CreateCategoryDto updateDto = new CreateCategoryDto();
        updateDto.setName(UPDATED_CATEGORY_NAME);

        Category updated = new Category();
        updated.setId(ONE);
        updated.setName(UPDATED_CATEGORY_NAME);

        CategoryDto updatedDto = new CategoryDto();
        updatedDto.setId(ONE);
        updatedDto.setName(UPDATED_CATEGORY_NAME);

        Category existing = getCategory();

        when(categoryRepository.findById(ONE)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(updated);
        when(categoryMapper.toCategoryDto(updated)).thenReturn(updatedDto);

        CategoryDto result = categoryService.update(ONE, updateDto);

        assertNotNull(result);
        assertEquals(UPDATED_CATEGORY_NAME, result.getName());
        verify(categoryRepository).findById(ONE);
        verify(categoryRepository).save(existing);
    }

    @Test
    @DisplayName("Throws EntityNotFoundException when updating non-existing category")
    public void update_WithInvalidId_ThrowsEntityNotFoundException() {
        CreateCategoryDto updateDto = new CreateCategoryDto();
        updateDto.setName(UPDATED_CATEGORY_NAME);

        when(categoryRepository.findById(NONEXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(NONEXISTING_ID, updateDto));
        verify(categoryRepository).findById(NONEXISTING_ID);
    }

    @Test
    @DisplayName("Verify category is deleted by ID")
    public void deleteById_WithValidId_DeletesCategory() {
        doNothing().when(categoryRepository).deleteById(ONE);

        categoryService.deleteById(ONE);

        verify(categoryRepository).deleteById(ONE);
    }

    private Category getCategory() {
        Category category = new Category();
        category.setId(ONE);
        category.setName(CATEGORY_NAME);
        return category;
    }

    private CategoryDto getCategoryDto() {
        CategoryDto dto = new CategoryDto();
        dto.setId(ONE);
        dto.setName(CATEGORY_NAME);
        return dto;
    }

    private CreateCategoryDto getCreateCategoryDto() {
        CreateCategoryDto dto = new CreateCategoryDto();
        dto.setName(CATEGORY_NAME);
        return dto;
    }
}

