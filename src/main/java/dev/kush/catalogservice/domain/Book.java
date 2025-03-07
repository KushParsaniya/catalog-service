package dev.kush.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("books")
public record Book(

        @Id
        Long id,

        @NotBlank(message = "The book is ISBN must be defined")
        @Pattern(
                regexp = "^[0-9]{10}|[0-9]{13}",
                message = "The ISBN format must be valid"
        )
        String isbn,

        @NotBlank(message = "The book title must be defined.")
        String title,

        @NotBlank(message = "The book author must be defined.")
        String author,

        @NotNull(message = "The book price must be defined.")
        @Positive(message = "The book price must be a positive number.")
        Double price,

        @CreatedDate
        Instant createdAt,

        @LastModifiedDate
        Instant modifiedAt,

        @Version
        int version
) {

    public static Book of(String isbn, String title, String author, Double price) {
        return new Book(null, isbn, title, author, price, null, null, 0);
    }

    public static Book of(Long id,String isbn, String title, String author, Double price) {
        return new Book(id, isbn, title, author, price, null, null, 0);
    }
}
