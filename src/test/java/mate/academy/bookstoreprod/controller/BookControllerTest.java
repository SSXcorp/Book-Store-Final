package mate.academy.bookstoreprod.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import mate.academy.bookstoreprod.dto.book.BookDto;
import mate.academy.bookstoreprod.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    private static final String URI = "/books";
    private static final String URI_WITH_ID = "/books/{id}";
    private static final String BOOK_TITLE = "Book Title";
    private static final String BOOK_AUTHOR = "John Doe";
    private static final String BOOK_DESCRIPTION = "Book Description";
    private static final String BOOK_ISBN = "1234567890";
    private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(200);
    private static final Long CATEGORY_ID = 1L;
    private static final Long BOOK_ID = 1L;

    private static final String UPDATED_TITLE = "Updated Title";
    private static final String UPDATED_AUTHOR = "Updated Author";
    private static final String UPDATED_DESCRIPTION = "Updated Description";
    private static final String UPDATED_ISBN = "9876543210";
    private static final BigDecimal UPDATED_PRICE = BigDecimal.valueOf(300);

    private static final String SEARCH_TITLE = "Book";
    private static final String SEARCH_AUTHOR = "John";

    protected static MockMvc mockMvc;

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
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @Sql(scripts = "classpath:database/book/add-three-books.sql",
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
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(1, actual[0].getId());
        Assertions.assertEquals("Clean Code", actual[1].getTitle());
        Assertions.assertEquals("Brian Goetz", actual[2].getAuthor());
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

        BookDto savedBookDto = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertNotNull(savedBookDto);
        Assertions.assertNotNull(savedBookDto.getId());
        Assertions.assertEquals("Clean Code", savedBookDto.getTitle());
        Assertions.assertEquals("Robert Martin", savedBookDto.getAuthor());
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
    @Sql(scripts = "classpath:database/book/add-one-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateBook_ShouldUpdateAndReturnBook() throws Exception {
        CreateBookRequestDto updateDto = new CreateBookRequestDto();
        updateDto.setAuthor(UPDATED_AUTHOR);
        updateDto.setTitle(UPDATED_TITLE);
        updateDto.setDescription(UPDATED_DESCRIPTION);
        updateDto.setIsbn(UPDATED_ISBN);
        updateDto.setPrice(UPDATED_PRICE);
        updateDto.setCategories(Set.of(CATEGORY_ID));

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
    public void deleteBook_ValidId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete(URI_WITH_ID, BOOK_ID))
                .andExpect(status().isNoContent());
    }
}

