package dev.kush.catalogservice;

import dev.kush.catalogservice.domain.Book;
import dev.kush.catalogservice.domain.BookRepository;
import dev.kush.catalogservice.domain.BookService;
import dev.kush.catalogservice.exception.BookAlreadyExists;
import dev.kush.catalogservice.exception.BookNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void whenBookToCreateAlreadyExistsThenThrowsError() {
        final String isbn = "1234567890";
        var book = Book.of(isbn, "spring start here", "laur spilca", 599d);

        when(bookRepository.existsByIsbn(isbn)).thenReturn(true);
        assertThatThrownBy(() -> bookService.addBookToCatalog(book))
                .isInstanceOf(BookAlreadyExists.class);
    }

    @Test
    void whenBookToReadDontExistsThenThrowsError() {
        final String isbn = "1234567890";
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.viewBookDetails(isbn)).isInstanceOf(BookNotFoundException.class);
    }
}
