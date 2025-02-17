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
            Type listType = new TypeToken<List<Book>>() {
            }.getType();
            List<Book> books = gson.fromJson(reader, listType);
            return (books != null) ? books : new ArrayList<>();
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

    // ✅ Add a new book
    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody Book newBook) {
        if (newBook.getTitle() == null || newBook.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Book Title is required");
        }

        if (newBook.getGenre() == null || newBook.getGenre().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Genre is required");
        }

        if (newBook.getCondition() == null || newBook.getCondition().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Book Condition is required");
        }

        if (newBook.getDescription() == null || newBook.getDescription().isEmpty()) {
            newBook.setDescription("No description provided.");  // ✅ Ensure description is not null
        }

        List<Book> books = loadBooks();
        newBook.setId("book" + (books.size() + 1)); // ✅ Auto-generate book ID
        books.add(newBook);
        saveBooks(books);

        return ResponseEntity.ok("✅ Book added successfully!");
    }

    // ✅ Get all books owned by a user
    @GetMapping("/owner/{userId}")
    public ResponseEntity<List<Book>> getBooksByOwner(@PathVariable String userId) {
        List<Book> books = loadBooks();
        List<Book> userBooks = books.stream()
                .filter(book -> Objects.equals(book.getOwnerId(), userId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userBooks);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable String bookId) {
        List<Book> books = loadBooks();
        Optional<Book> book = books.stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst();

        if (book.isPresent()) {
            return ResponseEntity.ok(book.get()); // ✅ Ensure correct return type
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Book not found."); // ✅ Ensure return type is ResponseEntity<String>
        }
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable String bookId) {
        List<Book> books = loadBooks();
        boolean removed = books.removeIf(book -> book.getId().equals(bookId));

        if (removed) {
            saveBooks(books);
            return ResponseEntity.ok("✅ Book deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Book not found.");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
        List<Book> books = loadBooks();
        List<Book> filteredBooks = books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                        book.getGenre().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredBooks);
    }

    @PutMapping("/update-status/{bookId}")
    public ResponseEntity<String> updateBookStatus(@PathVariable String bookId, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");

        List<Book> books = loadBooks();
        boolean updated = false;

        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                if (newStatus.equalsIgnoreCase("Unavailable") || newStatus.equalsIgnoreCase("Available")) {
                    book.setStatus(newStatus); // ✅ Update book status correctly
                } else {
                    return ResponseEntity.badRequest().body("❌ Invalid status update.");
                }
                updated = true;
                break;
            }
        }

        if (updated) {
            saveBooks(books);
            return ResponseEntity.ok("✅ Book status updated successfully.");
        } else {
            return ResponseEntity.status(404).body("❌ Book not found.");
        }
    }

    @PutMapping("/edit/{bookId}")
    public ResponseEntity<String> updateBook(@PathVariable String bookId, @RequestBody Book updatedBook) {
        List<Book> books = loadBooks();
        Optional<Book> bookToEdit = books.stream().filter(book -> book.getId().equals(bookId)).findFirst();

        if (bookToEdit.isPresent()) {
            Book existingBook = bookToEdit.get();

            // ✅ Only update fields that were changed
            existingBook.setTitle(updatedBook.getTitle() != null ? updatedBook.getTitle() : existingBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor() != null ? updatedBook.getAuthor() : existingBook.getAuthor());
            existingBook.setGenre(updatedBook.getGenre() != null ? updatedBook.getGenre() : existingBook.getGenre());
            existingBook.setCondition(updatedBook.getCondition() != null ? updatedBook.getCondition() : existingBook.getCondition());
            existingBook.setDescription(updatedBook.getDescription() != null ? updatedBook.getDescription() : existingBook.getDescription());
            existingBook.setImage(updatedBook.getImage() != null && !updatedBook.getImage().isEmpty() ? updatedBook.getImage() : existingBook.getImage());

            // ✅ Do NOT modify status
            updatedBook.setStatus(existingBook.getStatus());

            saveBooks(books);

            return ResponseEntity.ok("✅ Book updated successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Book not found.");
        }
    }
}


