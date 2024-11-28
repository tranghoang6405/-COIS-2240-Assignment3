import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Transaction {
	// instance of the class
	private static Transaction instance;
	private Transaction() {}
	
	// Get the single instance of the class
	public static Transaction getTransaction() {
		if (instance == null) {
			synchronized (Transaction.class) {
				if (instance == null) {
					instance = new Transaction();
				}
			}
		}
		return instance;
	}
	
	// Perform the saving of transactions to file
	 	public void saveTransaction(String transactionDetails) {
	 		try (BufferedWriter writer = new BufferedWriter(new FileWriter("transaction.txt", true))) {
	 		    writer.write(transactionDetails);
	 		    writer.newLine();
	 		} catch (IOException e) {
	 			System.out.println("Error: Cannot save transaction.");
	 		}
	 	}

    // Perform the borrowing of a book
    public boolean borrowBook(Book book, Member member) {
        if (book.isAvailable()) {
            book.borrowBook();
            member.borrowBook(book); 
            String transactionDetails = getCurrentDateTime() + " - Borrowing: " + member.getName() + " borrowed " + book.getTitle();
            System.out.println(transactionDetails);

            // Save it to file
            saveTransaction(transactionDetails);
            
            return true;
        } else {
            System.out.println("The book is not available.");
            return false;
        }
    }

    // Perform the returning of a book
    public void returnBook(Book book, Member member) {
        if (member.getBorrowedBooks().contains(book)) {
            member.returnBook(book);
            book.returnBook();
            String transactionDetails = getCurrentDateTime() + " - Returning: " + member.getName() + " returned " + book.getTitle();
            System.out.println(transactionDetails);
            
            // Save it to file
            saveTransaction(transactionDetails);
        } else {
            System.out.println("This book was not borrowed by the member.");
        }
    }

    // Get the current date and time in a readable format
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
    
}