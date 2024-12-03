import static org.junit.Assert.*;
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
	
	@Test
	public void testSingletonTransaction() {
		fail("Not yet implemented");
	}

}
