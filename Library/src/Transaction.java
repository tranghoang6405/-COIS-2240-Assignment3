import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class Transaction {
	// instance of the class
	private static Transaction instance;
	private static boolean instanceCheck = false;
	private Transaction() {
		if (instanceCheck) {
			throw new UnsupportedOperationException("Singleton instance already created!");
		}
		instanceCheck = true;
	}
	
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
    
    // Display transaction history
    public void displayTransactionHistory() {
    	try (BufferedReader reader = new BufferedReader(new FileReader("transaction.txt"))) {
    		String line;
    		int transactionCount = 0;
    		while ((line = reader.readLine()) != null) {
    			System.out.println(line);
    			transactionCount++;
    		}
    		
    		if (transactionCount == 0) {
    			System.out.println("No transaction is found.");
    		} else {
    			System.out.println("Total transactions: " + transactionCount);
    		}
    	} catch(IOException e) {
    		System.out.println("File doesn't exist or cannot be read");
    	}
    }
    
    // Return transaction history as a string for GUI
    public String getTransactionHistory() {
        StringBuilder history = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("transaction.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.append(line).append("\n");
            }
        } catch (IOException e) {
            return "No transaction history found or the file could not be read.";
        }

        if (history.length() == 0) {
            return "No transactions have been recorded yet.";
        }
        return history.toString();
    }
}