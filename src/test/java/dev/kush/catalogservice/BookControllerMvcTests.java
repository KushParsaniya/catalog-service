package dev.kush.catalogservice;

import dev.kush.catalogservice.domain.BookService;
import dev.kush.catalogservice.exception.BookNotFoundException;
import dev.kush.catalogservice.web.BookController;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(BookController.class)
public class BookControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "1234567890";

        BDDMockito.given(bookService.viewBookDetails(isbn))
                .willThrow(BookNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + isbn))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
