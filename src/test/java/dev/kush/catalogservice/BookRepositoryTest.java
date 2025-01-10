package dev.kush.catalogservice;

import dev.kush.catalogservice.config.DataConfig;
import dev.kush.catalogservice.domain.Book;
import dev.kush.catalogservice.domain.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findBookByIsbnWhenExisting() {
        var bookIsbn = "1234567890";
        var book = Book.of(bookIsbn, "Spring start here", "laur spilca", 1234d);
        jdbcAggregateTemplate.insert(book);
        Optional<Book> savedBook = bookRepository.findByIsbn(bookIsbn);

        assertThat(savedBook).isPresent();
        assertThat(savedBook.get().isbn()).isEqualTo(bookIsbn);

    }
}
