package dev.kush.catalogservice.exception;

public class BookAlreadyExists extends RuntimeException {
    public BookAlreadyExists(String isbn) {
        super("Book with isbn " + isbn + " already exists.");
    }
}
