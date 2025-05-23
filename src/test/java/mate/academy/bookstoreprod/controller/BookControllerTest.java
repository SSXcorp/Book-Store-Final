package mate.academy.bookstoreprod.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import mate.academy.bookstoreprod.dto.book.BookDto;
import mate.academy.bookstoreprod.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreprod.exception.EntityAlreadyExistsException;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.util.TestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    private static final Long BOOK_ID = 1L;
    private static final String BOOK_AUTHOR = "John Doe";
    private static final String BOOK_DESCRIPTION = "Book Description";
    private static final String BOOK_ISBN = "1234567890";
    private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(200);
    private static final String BOOK_TITLE = "Book Title";
    private static final Long CATEGORY_ID = 1L;
    private static final Long NONEXISTING_ID = 9999L;
    private static final String SEARCH_AUTHOR = "John";
    private static final String SEARCH_TITLE = "Book";
    private static final String UPDATED_AUTHOR = "New Author";
    private static final String UPDATED_DESCRIPTION = "Updated Description";
    private static final String UPDATED_TITLE = "NEW TITLE";
    private static final String URI = "/books";
    private static final String URI_WITH_ID = "/books/{id}";

    private final TestUtil testUtil = new TestUtil();

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUpBeforeClass(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/category/add-one-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void save_ValidData_ShouldReturnBookDtoOfSavedBook() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor(BOOK_AUTHOR);
        createBookRequestDto.setTitle(BOOK_TITLE);
        createBookRequestDto.setDescription(BOOK_DESCRIPTION);
        createBookRequestDto.setIsbn(BOOK_ISBN);
        createBookRequestDto.setPrice(BOOK_PRICE);
        createBookRequestDto.setCategories(Set.of(CATEGORY_ID));

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(BOOK_TITLE))
                .andExpect(jsonPath("$.author").value(BOOK_AUTHOR))
                .andExpect(jsonPath("$.description").value(BOOK_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(BOOK_PRICE));
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    @Sql(scripts = {"classpath:database/category/add-one-category.sql",
            "classpath:database/book/add-one-book.sql" },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void save_BookAlreadyExists_ThrowsEntityAlreadyExistsException() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        String duplicateIsbn = "ISBN1241423456";
        createBookRequestDto.setAuthor(BOOK_AUTHOR);
        createBookRequestDto.setTitle(BOOK_TITLE);
        createBookRequestDto.setDescription(BOOK_DESCRIPTION);
        createBookRequestDto.setIsbn(duplicateIsbn);
        createBookRequestDto.setPrice(BOOK_PRICE);
        createBookRequestDto.setCategories(Set.of(CATEGORY_ID));

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict())
                .andExpect(result -> assertInstanceOf(EntityAlreadyExistsException.class,
                        result.getResolvedException()))
                .andExpect(result ->
                        assertEquals("Book with isbn " + duplicateIsbn + " already exists",
                                result.getResolvedException().getMessage()));
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @Sql(scripts = {"classpath:database/category/delete-all-from-categories.sql",
            "classpath:database/book/add-three-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllBooks_ShouldReturnOk() throws Exception {
        MvcResult result = mockMvc.perform(
                        get(URI)
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        BookDto[] actual = objectMapper.treeToValue(jsonNode.get("content"), BookDto[].class);
        assertNotNull(actual);
        assertEquals(3, actual.length);
        assertEquals(1, actual[0].getId());
        assertEquals("Clean Code", actual[1].getTitle());
        assertEquals("Brian Goetz", actual[2].getAuthor());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @Sql(scripts = "classpath:database/book/add-one-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookById_ValidId_ShouldReturnBookDto() throws Exception {
        MvcResult result = mockMvc.perform(get(URI_WITH_ID, BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOK_ID))
                .andReturn();

        BookDto savedBookDto = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);

        assertNotNull(savedBookDto);
        assertNotNull(savedBookDto.getId());
        assertEquals("Clean Code", savedBookDto.getTitle());
        assertEquals("Robert Martin", savedBookDto.getAuthor());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @Sql(scripts = "classpath:database/book/add-one-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookById_InvalidId_ThrowsEntityNotFoundException() throws Exception {
        mockMvc.perform(get(URI_WITH_ID, NONEXISTING_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(EntityNotFoundException.class,
                        result.getResolvedException()))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void searchBooks_WithValidParams_ShouldReturnFilteredBooks() throws Exception {
        mockMvc.perform(get(URI + "/search")
                        .param("title", SEARCH_TITLE)
                        .param("author", SEARCH_AUTHOR)
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    @Sql(scripts = {"classpath:database/category/delete-all-from-categories.sql",
            "classpath:database/book/add-one-book.sql",
            "classpath:database/category/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/book/delete-all-from-books.sql",
            "classpath:database/category/delete-all-from-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateBook_ValidInput_ShouldUpdateAndReturnBook() throws Exception {
        CreateBookRequestDto updateDto = testUtil.getUpdateCreateBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(put(URI_WITH_ID, BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(UPDATED_TITLE))
                .andExpect(jsonPath("$.author").value(UPDATED_AUTHOR))
                .andExpect(jsonPath("$.description").value(UPDATED_DESCRIPTION));
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    @Sql(scripts = {"classpath:database/category/delete-all-from-categories.sql",
            "classpath:database/book/add-one-book.sql",
            "classpath:database/category/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/book/delete-all-from-books.sql",
            "classpath:database/category/delete-all-from-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateBook_NonexistingId_ThrowsEntityNotFoundException() throws Exception {
        CreateBookRequestDto updateDto = testUtil.getUpdateCreateBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(put(URI_WITH_ID, NONEXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(EntityNotFoundException.class,
                        result.getResolvedException()))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void deleteBook_ValidId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete(URI_WITH_ID, BOOK_ID))
                .andExpect(status().isNoContent());
    }
}

