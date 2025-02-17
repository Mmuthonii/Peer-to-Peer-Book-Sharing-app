

package com.booksharing.controllers;

import com.booksharing.models.Request;
import com.booksharing.models.Book;
import com.booksharing.models.Notification;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/requests")
public class RequestController {
    private static final String REQUESTS_FILE = "data/requests.json"; // ✅ Store requests in JSON
    private static final String NOTIFICATIONS_FILE = "data/notifications.json"; // ✅ Store notifications in JSON
    private static final String BOOKS_FILE = "data/books.json"; // ✅ Path for book data
    private static final Gson gson = new Gson();
    private static final String LOANED_FILE = "data/loaned.json"; // ✅ Add this line

    // ✅ Load Requests from JSON
    private List<Request> loadRequests() {
        File file = new File(REQUESTS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(REQUESTS_FILE)) {
            Type listType = new TypeToken<List<Request>>() {}.getType();
            List<Request> requests = gson.fromJson(reader, listType);
            return (requests != null) ? requests : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ✅ Save Requests to JSON
    private void saveRequests(List<Request> requests) {
        try (Writer writer = new FileWriter(REQUESTS_FILE, false)) {
            gson.toJson(requests, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Load Books from JSON
    private List<Book> loadBooks() {
        File file = new File(BOOKS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(BOOKS_FILE)) {
            Type listType = new TypeToken<List<Book>>() {}.getType();
            List<Book> books = gson.fromJson(reader, listType);
            return (books != null) ? books : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ✅ Save Books to JSON
    private void saveBooks(List<Book> books) {
        try (Writer writer = new FileWriter(BOOKS_FILE, false)) {
            gson.toJson(books, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Load Notifications from JSON
    private List<Notification> loadNotifications() {
        File file = new File(NOTIFICATIONS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(NOTIFICATIONS_FILE)) {
            Type listType = new TypeToken<List<Notification>>() {}.getType();
            List<Notification> notifications = gson.fromJson(reader, listType);
            return (notifications != null) ? notifications : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ✅ Save Notifications to JSON
    private void saveNotifications(List<Notification> notifications) {
        try (Writer writer = new FileWriter(NOTIFICATIONS_FILE, false)) {
            gson.toJson(notifications, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Load Loaned Books from JSON
    private List<Book> loadLoanedBooks() {
        File file = new File(LOANED_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new FileReader(LOANED_FILE)) {
            Type listType = new TypeToken<List<Book>>() {}.getType();
            List<Book> loanedBooks = gson.fromJson(reader, listType);
            return (loanedBooks != null) ? loanedBooks : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ✅ Save Loaned Books to JSON
    private void saveLoanedBooks(List<Book> loanedBooks) {
        try (Writer writer = new FileWriter(LOANED_FILE, false)) {
            gson.toJson(loanedBooks, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Book>> getBooksByOwner(@PathVariable String ownerId) {
        List<Book> books = loadBooks();

        // ✅ Get all books owned by the user (including unavailable books)
        List<Book> ownerBooks = books.stream()
                .filter(book -> book.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ownerBooks);
    }

    @GetMapping("/received/{userId}")
    public ResponseEntity<List<Request>> getReceivedRequests(@PathVariable String userId) {
        if (userId == null || userId.isEmpty()) {
            System.out.println("❌ Error: User ID is missing for fetching received requests.");
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }

        try {
            System.out.println("🔍 Fetching received requests for user ID: " + userId);

            List<Request> requests = loadRequests();
            if (requests.isEmpty()) {
                System.out.println("⚠️ No requests found in requests.json.");
                return ResponseEntity.ok(new ArrayList<>());
            }

            List<Request> incomingRequests = requests.stream()
                    .filter(request -> request.getReceiverId() != null && request.getReceiverId().equals(userId))
                    .collect(Collectors.toList());

            System.out.println("✅ Received Requests for User " + userId + ": " + incomingRequests.size());
            return ResponseEntity.ok(incomingRequests);
        } catch (Exception e) {
            System.err.println("❌ Error fetching received requests: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendRequest(@RequestBody Request request) {
        if (request.getSenderId() == null || request.getSenderId().isEmpty()) {
            System.out.println("❌ Error: Sender ID is missing.");
            return ResponseEntity.badRequest().body("❌ Sender ID is required.");
        }

        if (request.getReceiverId() == null || request.getReceiverId().isEmpty()) {
            System.out.println("❌ Error: Receiver ID is missing.");
            return ResponseEntity.badRequest().body("❌ Receiver ID is required.");
        }

        String requestId = UUID.randomUUID().toString();
        request.setId(requestId);
        request.setStatus("Pending");

        System.out.println("📌 Creating Request:");
        System.out.println("🔹 ID: " + requestId);
        System.out.println("🔹 Book: " + request.getBookTitle());
        System.out.println("🔹 Sender ID: " + request.getSenderId());
        System.out.println("🔹 Receiver ID: " + request.getReceiverId());

        List<Request> requests = loadRequests();
        requests.add(request);
        saveRequests(requests); // ✅ Save request to JSON file

        // ✅ Ensure notifications are also saved
        List<Notification> notifications = loadNotifications();
        Notification notification = new Notification(
                UUID.randomUUID().toString(),
                requestId,
                request.getReceiverId(),
                "📌 New request for: " + request.getBookTitle() + " from " + request.getSenderName(),
                false
        );
        notifications.add(notification);
        saveNotifications(notifications);

        System.out.println("✅ Request Sent Successfully!");
        return ResponseEntity.ok("✅ Request sent successfully!");
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<Request>> getSentRequests(@PathVariable String userId) {
        if (userId == null || userId.isEmpty()) {
            System.out.println("❌ Error: User ID is missing for fetching sent requests.");
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }

        try {
            System.out.println("🔍 Fetching sent requests for user ID: " + userId);

            List<Request> requests = loadRequests();
            if (requests.isEmpty()) {
                System.out.println("⚠️ No requests found in requests.json.");
                return ResponseEntity.ok(new ArrayList<>());
            }

            List<Request> userRequests = requests.stream()
                    .filter(request -> request.getSenderId() != null && request.getSenderId().equals(userId))
                    .collect(Collectors.toList());

            System.out.println("✅ Sent Requests for User " + userId + ": " + userRequests.size());
            return ResponseEntity.ok(userRequests);
        } catch (Exception e) {
            System.err.println("❌ Error fetching sent requests: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    @GetMapping("/details/{requestId}")
    public ResponseEntity<?> getRequestDetails(@PathVariable String requestId) {
        List<Request> requests = loadRequests();
        Optional<Request> request = requests.stream()
                .filter(r -> r.getId().equals(requestId))
                .findFirst();

        if (request.isPresent()) {
            Request foundRequest = request.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", foundRequest.getId());
            response.put("bookId", foundRequest.getBookId()); // ✅ Ensure bookId is included
            response.put("senderId", foundRequest.getSenderId());
            response.put("receiverId", foundRequest.getReceiverId());
            response.put("bookTitle", foundRequest.getBookTitle());
            response.put("status", foundRequest.getStatus());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Request not found.");
        }
    }

    @PutMapping("/respond/{requestId}")
    public ResponseEntity<String> respondToRequest(@PathVariable String requestId, @RequestBody Map<String, String> response) {
        List<Request> requests = loadRequests();
        List<Book> books = loadBooks();
        List<Notification> notifications = loadNotifications();
        List<Book> loanedBooks = loadLoanedBooks();

        boolean updated = false;
        String status = response.get("status");
        String bookId = null;
        String borrowerName = null;

        for (Request request : requests) {
            if (request.getId().equals(requestId)) {
                request.setStatus(status);
                bookId = request.getBookId();
                borrowerName = request.getSenderName();
                updated = true;

                notifications.add(new Notification(UUID.randomUUID().toString(), requestId, request.getSenderId(),
                        "📌 Your request for '" + request.getBookTitle() + "' has been " + status, false));

                break;
            }
        }

        if (!updated) {
            return ResponseEntity.badRequest().body("❌ Request not found.");
        }

        saveRequests(requests);
        saveNotifications(notifications);

        // ✅ Step 1: Ensure book is only updated, NOT removed
        if ("Accepted".equalsIgnoreCase(status) && bookId != null) {
            for (Book book : books) {
                if (book.getId().equals(bookId)) {
                    book.setStatus("Unavailable"); // ✅ Just update status
                    book.setBorrower(borrowerName);
                    saveBooks(books); // ✅ Save changes
                    break;
                }
            }

            return ResponseEntity.ok("✅ Request Accepted, book status updated to 'Unavailable'.");
        }

        return ResponseEntity.ok("✅ Request " + status);
    }

    @PutMapping("/update-status/{bookId}")
    public ResponseEntity<String> updateBookStatus(@PathVariable String bookId, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");

        List<Book> books = loadBooks();
        boolean updated = false;

        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                book.setStatus(newStatus); // ✅ Change status to "Unavailable"
                updated = true;
                break;
            }
        }

        if (updated) {
            saveBooks(books);
            return ResponseEntity.ok("✅ Book status updated to " + newStatus + " successfully.");
        } else {
            return ResponseEntity.status(404).body("❌ Book not found.");
        }
    }
    @GetMapping("/loaned")
    public ResponseEntity<List<Book>> getLoanedBooks() {
        List<Book> loanedBooks = loadLoanedBooks();
        if (loanedBooks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(loanedBooks);
    }
    @PutMapping("/return/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable String bookId) {
        List<Book> loanedBooks = loadLoanedBooks();
        List<Book> books = loadBooks();

        boolean bookFound = loanedBooks.stream().anyMatch(book -> book.getId().equals(bookId));

        if (bookFound) {
            // ✅ Remove book from loaned.json safely
            loanedBooks = loanedBooks.stream()
                    .filter(book -> !book.getId().equals(bookId))
                    .collect(Collectors.toList());

            saveLoanedBooks(loanedBooks); // ✅ Save updated loaned.json
        } else {
            return ResponseEntity.status(404).body("❌ Book not found in loaned.json.");
        }

        // ✅ Instead of removing, just mark book as "Available"
        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                book.setStatus("Available");  // ✅ Restore to Available
                book.setBorrower(null);       // ✅ Remove borrower info
                saveBooks(books);  // ✅ Ensure the book remains in books.json
                return ResponseEntity.ok("✅ Book returned and marked as Available.");
            }
        }

        return ResponseEntity.status(404).body("❌ Book not found in books.json.");
    }
    @PostMapping("/save-loan")
    public ResponseEntity<String> saveLoanedBook(@RequestBody Book loanedBook) {
        List<Book> loanedBooks = loadLoanedBooks();

        // ✅ Ensure book is not duplicated
        if (loanedBooks.stream().noneMatch(b -> b.getId().equals(loanedBook.getId()))) {
            loanedBooks.add(loanedBook);
            saveLoanedBooks(loanedBooks);
        }

        return ResponseEntity.ok("✅ Book successfully saved in loaned.json");
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable String bookId) {
        List<Book> books = loadBooks();

        // ✅ Find book by ID
        Optional<Book> book = books.stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst();

        if (book.isPresent()) {
            System.out.println("✅ Book found: " + book.get().getTitle() + ", Status: " + book.get().getStatus()); // ✅ Log book details
            return ResponseEntity.ok(book.get()); // ✅ Return book details
        } else {
            System.out.println("⚠️ Warning: Book with ID " + bookId + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Book not found.");
        }
    }
}
