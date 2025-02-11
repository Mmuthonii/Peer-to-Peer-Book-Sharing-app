package com.booksharing.controllers;

import com.booksharing.models.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/books")
public class BookController {
    private static final String FILE_PATH = "data/books.json";
    private static final Gson gson = new Gson();

    private List<Book> loadBooks() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Book>>() {}.getType();
            List<Book> books = gson.fromJson(reader, listType);
            return (books != null) ? new ArrayList<>(books) : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveBooks(List<Book> books) {
        try (Writer writer = new FileWriter(FILE_PATH, false)) {
            gson.toJson(books, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody Book newBook) {
        if (newBook.getOwnerId() == null || newBook.getOwnerId().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Owner ID is required");
        }
        if (newBook.getTitle() == null || newBook.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Book Title is required");
        }
        if (newBook.getGenre() == null || newBook.getGenre().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Genre is required");
        }
        if (newBook.getCondition() == null || newBook.getCondition().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Book Condition is required");
        }

        if (newBook.getImage() == null || newBook.getImage().isEmpty()) {
            newBook.setImage("https://via.placeholder.com/100x150");
        }

        List<Book> books = loadBooks();
        String newId = String.valueOf(books.size() + 1); // ✅ Convert int to String
        newBook.setId(newId);

        books.add(newBook);
        saveBooks(books);

        return ResponseEntity.ok("✅ Book added successfully!");
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String query) {
        List<Book> books = loadBooks();
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                        book.getGenre().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    @GetMapping("/owner/{userId}")
    public ResponseEntity<List<Book>> getBooksByOwner(@PathVariable String userId) {
        List<Book> books = loadBooks();
        List<Book> userBooks = books.stream()
                .filter(book -> book.getOwnerId().equals(userId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userBooks);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable String bookId) { // ✅ Change int to String
        List<Book> books = loadBooks();
        boolean removed = books.removeIf(book -> book.getId().equals(bookId));

        if (removed) {
            saveBooks(books);
            return ResponseEntity.ok("✅ Book deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Book not found.");
        }
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable String bookId) { // ✅ Ensure bookId is a String
        List<Book> books = loadBooks();
        Optional<Book> book = books.stream()
                .filter(b -> b.getId().equals(bookId)) // ✅ Use `.equals()` for String comparison
                .findFirst();

        if (book.isPresent()) {
            return ResponseEntity.ok(book.get()); // ✅ Return found book
        } else {  // ✅ `else` should be outside of the `if` block
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Book not found.");
        }
    }



    @PutMapping("/edit/{bookId}")
    public ResponseEntity<String> updateBook(@PathVariable String bookId, @RequestBody Book updatedBook) { // ✅ Change int to String
        List<Book> books = loadBooks();
        Optional<Book> bookToEdit = books.stream().filter(book -> book.getId().equals(bookId)).findFirst();

        if (bookToEdit.isPresent()) {
            Book existingBook = bookToEdit.get();
            books.remove(existingBook);

            updatedBook.setId(bookId); // ✅ Keep same ID

            if (updatedBook.getImage() == null || updatedBook.getImage().isEmpty()) {
                updatedBook.setImage(existingBook.getImage());
            }

            books.add(updatedBook);
            saveBooks(books);

            return ResponseEntity.ok("✅ Book updated successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Book not found.");
        }
    }
}

