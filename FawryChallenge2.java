import java.util.*;

abstract class Book {
    String ISBN;
    String title;
    int year;
    double price;
    String author;

    Book(String ISBN, String title, int year, double price, String author) {
        this.ISBN = ISBN;
        this.title = title;
        this.year = year;
        this.price = price;
        this.author = author;
    }

    abstract boolean isAvailable(int quantity);
    abstract void buy(int quantity) throws Exception;
    abstract boolean isSellable();
    abstract void deliver(String email, String address);
}

class PaperBook extends Book {
    int stock;

    PaperBook(String ISBN, String title, int year, double price, String author, int stock) {
        super(ISBN, title, year, price, author);
        this.stock = stock;
    }

    @Override
    boolean isAvailable(int quantity) {
        return stock >= quantity;
    }

    @Override
    void buy(int quantity) throws Exception {
        if (quantity > stock) throw new Exception("Quantum book store: Not enough stock for " + title);
        stock -= quantity;
    }

    @Override
    boolean isSellable() {
        return true;
    }

    @Override
    void deliver(String email, String address) {
        System.out.println("Quantum book store: Shipping paper book to " + address);
    }
}

class EBook extends Book {
    String fileType;

    EBook(String ISBN, String title, int year, double price, String author, String fileType) {
        super(ISBN, title, year, price, author);
        this.fileType = fileType;
    }

    @Override
    boolean isAvailable(int quantity) {
        return true; 
    }

    @Override
    void buy(int quantity) {
        
    }

    @Override
    boolean isSellable() {
        return true;
    }

    @Override
    void deliver(String email, String address) {
        System.out.println("Quantum book store: Sending ebook to " + email);
    }
}

class ShowcaseBook extends Book {
    ShowcaseBook(String ISBN, String title, int year, double price, String author) {
        super(ISBN, title, year, price, author);
    }

    @Override
    boolean isAvailable(int quantity) {
        return false;
    }

    @Override
    void buy(int quantity) {

    }

    @Override
    boolean isSellable() {
        return false;
    }

    @Override
    void deliver(String email, String address) {
        
    }
}

class QuantumBookstore {
    List<Book> inventory = new ArrayList<>();

    void addBook(Book book) {
        inventory.add(book);
        System.out.println("Quantum book store: Book added - " + book.title);
    }

    void removeOutdatedBooks(int maxYearsOld) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        inventory.removeIf(book -> (currentYear - book.year) > maxYearsOld);
        System.out.println("Quantum book store: Old books removed.");
    }

    double buyBook(String ISBN, int quantity, String email, String address) throws Exception {
        for (Book book : inventory) {
            if (book.ISBN.equals(ISBN)) {
                if (!book.isSellable())
                    throw new Exception("Quantum book store: Book not for sale: " + book.title);

                if (!book.isAvailable(quantity))
                    throw new Exception("Quantum book store: Book not available in enough quantity.");

                book.buy(quantity);
                book.deliver(email, address);
                double total = book.price * quantity;
                System.out.printf("Quantum book store: Purchase complete. Total: %.2f\n", total);
                return total;
            }
        }
        throw new Exception("Quantum book store: Book not found.");
    }

    void printInventory() {
        System.out.println("Quantum book store: Inventory Listing");
        for (Book book : inventory) {
            System.out.printf("- %s (%s) - %.2f EGP\n", book.title, book.author, book.price);
        }
    }
}

public class FawryChallenge2 {
    public static void main(String[] args) {
        QuantumBookstore store = new QuantumBookstore();

        store.addBook(new PaperBook("PB001", "Java Programming", 2020, 300, "John Smith", 10));
        store.addBook(new EBook("EB001", "Clean Code", 2015, 200, "Robert C. Martin", "PDF"));
        store.addBook(new ShowcaseBook("SB001", "Ancient Scripts", 1990, 0, "Unknown"));

        store.printInventory();

        System.out.println("\n--- Removing outdated books (> 10 years) ---");
        store.removeOutdatedBooks(10);
        store.printInventory();

        System.out.println("\n--- Buying Books ---");
        try {
            store.buyBook("PB001", 2, "user@example.com", "123 Main St");
            store.buyBook("EB001", 1, "user@example.com", "");
            // store.buyBook("SB001", 1, "", ""); // Uncomment to test error
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
