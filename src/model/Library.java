package model;

import java.util.HashMap;
import java.util.Map;

public class Library {
    private static Map<String, Book> availableBooks = new HashMap<>();

    public static Map<String, Book> availableBooks() {
        return availableBooks;
    }

    static {
        availableBooks.put("ISBN00", new Book("Book1", 5));
        availableBooks.put("ISBN01" , new Book("Book2", 3));
        availableBooks.put("ISBN02" , new Book("Book3", 2));
        availableBooks.put("ISBN03" , new Book("Book4", 1));
    }
}
