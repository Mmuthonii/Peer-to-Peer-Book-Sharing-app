import java.util.Scanner;
import services.UserService;
import services.BookService;
import services.LoanService;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();
        BookService bookService = new BookService();
        LoanService loanService = new LoanService();

        System.out.println("Welcome to the Book Sharing System!");
        boolean running = true;

        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Add Book");
            System.out.println("4. Search Books");
            System.out.println("5. Send Loan Request");
            System.out.println("6. View History");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser(scanner, userService); // Call the registerUser method
                    break;
                case 2:
                    loginUser(scanner, userService); // Call the loginUser method
                    break;
                case 3:
                    addBook(scanner, bookService); // Call the addBook method
                    break;
                case 4:
                    searchBooks(scanner, bookService); // Call the searchBooks method
                    break;
                case 5:
                    sendLoanRequest(scanner, loanService); // Call the sendLoanRequest method
                    break;
                case 6:
                    userService.viewHistory();
                    break;
                case 7:
                    running = false;
                    System.out.println("Exiting the application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    // New methods start here
    private static void registerUser(Scanner scanner, UserService userService) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        userService.registerUser(name, email, password);
    }

    private static void loginUser(Scanner scanner, UserService userService) {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        userService.login(email, password);
    }

    private static void addBook(Scanner scanner, BookService bookService) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter condition (New/Used): ");
        String condition = scanner.nextLine();
        bookService.addBook(title, author, genre, condition);
    }

    private static void searchBooks(Scanner scanner, BookService bookService) {
        System.out.print("Enter search keyword: ");
        String query = scanner.nextLine();
        bookService.searchBooks(query);
    }

    private static void sendLoanRequest(Scanner scanner, LoanService loanService) {
        System.out.print("Enter book ID to request: ");
        int bookId = scanner.nextInt();
        System.out.print("Enter your user ID: ");
        int requesterId = scanner.nextInt();
        loanService.sendLoanRequest(bookId, requesterId);
    }
}
