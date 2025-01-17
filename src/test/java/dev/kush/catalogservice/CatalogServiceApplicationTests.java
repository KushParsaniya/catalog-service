package dev.kush.catalogservice;

import dev.kush.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
//@ActiveProfiles(value = "test")
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenPostRequestThenBookCreated() {
        var book = Book.of("879453844328", "spring start here", "laur spilca", 599d);
        webTestClient
                .post()
                .uri("/books")
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .value(actualBook -> {
                    assertThat(actualBook.isbn()).isNotBlank();
                    assertThat(actualBook.isbn()).isEqualTo(book.isbn());
                });
    }

    @Test
    void whenPostWithExistingIsbnThenUnProcessableEntity() {
        var book1 = Book.of("0987654321", "spring start here", "laur spilca", 599d);
        webTestClient.post()
                .uri("/books")
                .bodyValue(book1)
                .exchange();

        var book2 = Book.of("0987654321", "spring security in action", "laur spilca", 2000D);

        webTestClient.post()
                .uri("/books")
                .bodyValue(book2)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void whenAfterSaveGetSameBook() {
        final String isbn = "5789347589";
        var book = Book.of(isbn, "spring start here", "laur spilca", 599d);
        webTestClient.post()
                .uri("/books")
                .bodyValue(book)
                .exchange();

        webTestClient.get()
                .uri("/books/" + isbn)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(Book.class)
                .value(savedBook ->
                        assertThat(savedBook.isbn()).isEqualTo(book.isbn())
                );
    }

    @Test
    void afterDeleteBookNotFound() {
        final String isbn = "5789348790";
        var book = Book.of(isbn, "spring start here", "laur spilca", 599d);
        webTestClient.post()
                .uri("/books")
                .bodyValue(book)
                .exchange();

        webTestClient.delete()
                .uri("/books/" + isbn)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NO_CONTENT);

        webTestClient.get()
                .uri("/books/" + isbn)
                .exchange()
                .expectStatus().isNotFound();
    }
}
