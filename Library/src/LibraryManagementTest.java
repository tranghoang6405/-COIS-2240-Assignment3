import static org.junit.Assert.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Before;
import org.junit.Test;

public class LibraryManagementTest {

	@Test
	public void testBookId() throws Exception {
		
		// Validate boundary cases
		// Valid
		try {
			Book valid1 = new Book (100, "Valid book");
			assertEquals(100, valid1.getId());
			assertEquals("Valid book", valid1.getTitle());
			assertTrue(valid1.isAvailable());
		} catch (Exception e) {
			fail("Exception should not be thrown for valid book ID 100.");
		}
		
		// Valid
		try {
			Book valid2 = new Book (999, "Valid book");
			assertEquals(999, valid2.getId());
			assertEquals("Valid book", valid2.getTitle());
			assertTrue(valid2.isAvailable());
		} catch (Exception e) {
			fail("Exception should not be thrown for valid book ID 999.");
		}
		
		// Invalid
		try {
            new Book(1000, "Invalid book");
            fail("Exception should be thrown for invalid book ID 1000.");
        } catch (Exception e) {
            assertEquals("Invalid book ID", e.getMessage());
        }
		
		// Test two other invalid numbers
		try {
            new Book(99, "Invalid book");
            fail("Exception should be thrown for invalid book ID 99.");
        } catch (Exception e) {
            assertEquals("Invalid book ID", e.getMessage());
        }
		
		try {
            new Book(1350, "Invalid book");
            fail("Exception should be thrown for invalid book ID 1350.");
        } catch (Exception e) {
            assertEquals("Invalid book ID", e.getMessage());
        }

	}
	
	private Book book;
    private Member member;
    private Transaction transaction;
    
    @Before
    public void setUp() {
        try {
            // Initialize the book and member
            book = new Book(101, "Test Book");
            member = new Member(1, "Test Member");

            // Retrieve the singleton Transaction instance
            transaction = Transaction.getTransaction();
        } catch (Exception e) {
            fail("Exception during setup: " + e.getMessage());
        }
    }
    
    @Test
    public void testBorrowReturn() {
        // Ensure the book is available
        assertTrue(book.isAvailable());

        // Borrow the book 
        boolean borrowSuccess = transaction.borrowBook(book, member);
        assertTrue(borrowSuccess); 
        assertFalse(book.isAvailable()); 
        assertTrue(member.getBorrowedBooks().contains(book)); 

        // Try borrowing the book again (should fail)
        boolean secondBorrowSuccess = transaction.borrowBook(book, member);
        assertFalse(secondBorrowSuccess); 

        // Return the book
        transaction.returnBook(book, member);
        assertTrue(book.isAvailable()); 
        assertFalse(member.getBorrowedBooks().contains(book)); 

        // Try returning the book again (fail)
        transaction.returnBook(book, member);
        assertTrue(book.isAvailable()); 
        assertFalse(member.getBorrowedBooks().contains(book));
    }

	
	@Test
	public void testSingletonTransaction() throws Exception {
		Constructor<Transaction> constructor = Transaction.class.getDeclaredConstructor();
		
		int modifiers = constructor.getModifiers();
        assertEquals(Modifier.PRIVATE, modifiers);
      
        Transaction trans1 = Transaction.getTransaction();
        Transaction trans2 = Transaction.getTransaction();
        assertNotNull(trans1);
        assertSame(trans1, trans2);
        
        constructor.setAccessible(true);
        try {
            Transaction newTrans = constructor.newInstance();
            fail("Singleton should not allow reflection-based instantiation.");
        } catch (Exception e) {
            // Expected behavior
            assertTrue(e instanceof IllegalAccessException || e.getCause() instanceof UnsupportedOperationException);
        }
	}

}
