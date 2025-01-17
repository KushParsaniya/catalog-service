package dev.kush.catalogservice;

import dev.kush.catalogservice.config.DataConfig;
import dev.kush.catalogservice.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        // Ensure the books are deleted before each test
        webTestClient.delete().uri("/books/8794538443").exchange().expectStatus().isNoContent();
        webTestClient.delete().uri("/books/0987654321").exchange().expectStatus().isNoContent();
        webTestClient.delete().uri("/books/5789347589").exchange().expectStatus().isNoContent();
        webTestClient.delete().uri("/books/5789348790").exchange().expectStatus().isNoContent();
    }

    @Test
    void whenPostRequestThenBookCreated() {
        var isbn = "8794538443";
        var book = Book.of(isbn, "spring start here", "laur spilca", 599d);
        webTestClient.post().uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .value(actualBook -> {
                    assertThat(actualBook.isbn()).isNotBlank();
                    assertThat(actualBook.isbn()).isEqualTo(book.isbn());
                });

        webTestClient.delete().uri("/books/" + isbn)
                .exchange()
                .expectStatus().isNoContent(); // Ensure the book is deleted after the test
    }

    @Test
    void whenPostWithExistingIsbnThenUnProcessableEntity() {
        var isbn = "0987654321";
        var book1 = Book.of(isbn, "spring start here", "laur spilca", 599d);
        webTestClient.post().uri("/books")
                .bodyValue(book1)
                .exchange()
                .expectStatus().isCreated();

        var book2 = Book.of(isbn, "spring security in action", "laur spilca", 2000D);
        webTestClient.post().uri("/books")
                .bodyValue(book2)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        webTestClient.delete().uri("/books/" + isbn)
                .exchange()
                .expectStatus().isNoContent(); // Ensure the book is deleted after the test
    }

    @Test
    void whenAfterSaveGetSameBook() {
        final String isbn = "5789347589";
        var book = Book.of(isbn, "spring start here", "laur spilca", 599d);
        webTestClient.post().uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.get().uri("/books/" + isbn)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(Book.class)
                .value(savedBook -> assertThat(savedBook.isbn()).isEqualTo(book.isbn()));

        webTestClient.delete().uri("/books/" + isbn)
                .exchange()
                .expectStatus().isNoContent(); // Ensure the book is deleted after the test
    }

    @Test
    @Transactional
    void afterDeleteBookNotFound() {
        final String isbn = "5789348790";
        var book = Book.of(isbn, "spring start here", "laur spilca", 599d);
        webTestClient.post().uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.delete().uri("/books/" + isbn)
                .exchange()
                .expectStatus().isNoContent(); // Ensure the book is deleted

        webTestClient.get().uri("/books/" + isbn)
                .exchange()
                .expectStatus().isNotFound();
    }
}