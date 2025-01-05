package dev.kush.catalogservice;

import dev.kush.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookJsonTests {

    @Autowired
    private JacksonTester<Book> jacksonTester;

    @Test
    void testSerialize() throws IOException {
        var book = new Book("1234567890", "spring start here", "laur spilca", 599d);

        JsonContent<Book> jsonContent = jacksonTester.write(book);

        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
                .isEqualTo(book.isbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                .isEqualTo(book.title());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author")
                .isEqualTo(book.author());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.price());
    }

    @Test
    void testDeserialize() throws IOException {
        String content = """
                {
                  "isbn": "1234567890",
                  "title": "spring security in action",
                  "author": "laur spilca",
                  "price": 2000
                }
                """;
        assertThat(jacksonTester.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new Book("1234567890","spring security in action","laur spilca", 2000D));
    }
}
