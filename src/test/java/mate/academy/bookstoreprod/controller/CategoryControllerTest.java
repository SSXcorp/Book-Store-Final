package mate.academy.bookstoreprod.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.bookstoreprod.dto.category.CreateCategoryDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;

    private static final String DESCRIPTION = "Travel category books";
    private static final Long NONEXISTING_ID = 9999L;
    private static final Long ONE = 1L;
    private static final String TRAVEL_NAME = "Travel";
    private static final String UPDATE_DESCRIPTION = "Updated description";
    private static final String UPDATE_TRAVEL_NAME = "Updated Travel";
    private static final String URI = "/categories";
    private static final String URI_WITH_ID = "/categories/{id}";

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUp(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Category saves and return CategoryDto")
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/category/delete-all-from-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createCategory_ValidRequest_ShouldReturnCreatedCategoryDto() throws Exception {
        CreateCategoryDto request = new CreateCategoryDto();
        request.setName(TRAVEL_NAME);
        request.setDescription(DESCRIPTION);

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(TRAVEL_NAME))
                .andExpect(jsonPath("$.description").value(DESCRIPTION));
    }

    @Test
    @DisplayName("Category updates and return CategoryDto")
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    @Sql(scripts = {"classpath:database/category/delete-all-from-categories.sql",
            "classpath:database/category/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void updateCategory_ValidRequest_ShouldReturnUpdatedCategoryDto() throws Exception {
        CreateCategoryDto request = new CreateCategoryDto();
        request.setName(UPDATE_TRAVEL_NAME);
        request.setDescription(UPDATE_DESCRIPTION);

        mockMvc.perform(put(URI_WITH_ID, ONE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(UPDATE_TRAVEL_NAME))
                .andExpect(jsonPath("$.description").value(UPDATE_DESCRIPTION));
    }

    @Test
    @DisplayName("Category deletes and return no content")
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    @Sql(scripts = {"classpath:database/category/delete-all-from-categories.sql",
            "classpath:database/category/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteCategory_ValidId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete(URI_WITH_ID, ONE))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Get all categories return CategoryDtos pageable")
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @Sql(scripts = {"classpath:database/category/delete-all-from-categories.sql",
            "classpath:database/category/add-three-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAllCategories_ShouldReturnListCategoryDto() throws Exception {
        mockMvc.perform(get(URI)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].name").value("Category1"))
                .andExpect(jsonPath("$.content[1].name").value("Category2"))
                .andExpect(jsonPath("$.content[2].name").value("Category3"));
    }

    @Test
    @DisplayName("Get category by id return CategoryDto")
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @Sql(scripts = {"classpath:database/category/delete-all-from-categories.sql",
            "classpath:database/category/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getCategoryById_ValidId_ShouldReturnCategory() throws Exception {
        mockMvc.perform(
                get(URI_WITH_ID, ONE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ONE))
                .andExpect(jsonPath("$.name").value(TRAVEL_NAME));
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @DisplayName("Get all books by category return BookDtos pageble")
    @Sql(scripts = {
            "classpath:database/book/delete-from-books-categories.sql",
            "classpath:database/book/add-books-with-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBooksByCategoryId_ValidCategoryId_ShouldReturnBooks() throws Exception {
        mockMvc.perform(get("/categories/{id}/books", 2L)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    @DisplayName("Get category by invalid id return not found")
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @Sql(scripts = {"classpath:database/category/delete-all-from-categories.sql",
            "classpath:database/category/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getCategoryById_InvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(
                        get(URI_WITH_ID, NONEXISTING_ID))
                .andExpect(status().isNotFound());
    }
}


