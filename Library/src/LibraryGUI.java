import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class LibraryGUI extends Application {
	private Library library = new Library();
    private Transaction transaction = Transaction.getTransaction();
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Library Management System");

        VBox mainMenu = new VBox(10);
        mainMenu.setPadding(new Insets(10));
        
        Text welcomeLabel = new Text("Library Management System");
        welcomeLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        welcomeLabel.setFill(Color.PURPLE);
        
        Button addMemberButton = new Button("Add Member");
        Button addBookButton = new Button("Add Book");
        Button borrowBookButton = new Button("Borrow Book");
        Button returnBookButton = new Button("Return Book");
        Button viewBorrowedBooksButton = new Button("View Borrowed Books");
        Button viewTransactionHistoryButton = new Button("View Transaction History");
        Button exitButton = new Button("Exit");

        mainMenu.setStyle("-fx-background-color: #fec9f1; -fx-border-color: #e899dc; -fx-border-width: 2;");
        mainMenu.getChildren().addAll(
                welcomeLabel,
                addMemberButton,
                addBookButton,
                borrowBookButton,
                returnBookButton,
                viewBorrowedBooksButton,
                viewTransactionHistoryButton,
                exitButton
        );

        Scene mainScene = new Scene(mainMenu, 400, 300);

        // Event handlers
        addMemberButton.setOnAction(e -> showAddMemberDialog());
        addBookButton.setOnAction(e -> showAddBookDialog());
        borrowBookButton.setOnAction(e -> showBorrowBookDialog());
        returnBookButton.setOnAction(e -> showReturnBookDialog());
        viewBorrowedBooksButton.setOnAction(e -> showBorrowedBooksDialog());
        viewTransactionHistoryButton.setOnAction(e -> showTransactionHistoryDialog());
        exitButton.setOnAction(e -> primaryStage.close());

        primaryStage.setScene(mainScene);
        primaryStage.show();
	}
	
	private void showAddMemberDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add Member");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #FEC9F1; -fx-border-color: #e899dc; -fx-border-width: 2;");

        TextField idField = new TextField();
        idField.setPromptText("Member ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Member Name");
        Button addButton = new Button("Add Member");

        addButton.setOnAction(e -> {
            try {
                String idText = idField.getText().trim();
                String name = nameField.getText().trim();
                
                if (idText.isEmpty() || name.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "ID and Name cannot be empty.");
                    return;
                }
                
                int id = Integer.parseInt(idText);
                
                if (id <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Member ID must be a positive number.");
                    return;
                }
                
                if (name.length() < 2 || name.length() > 50) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Name must be between 2 and 50 characters.");
                    return;
                }
                
                library.addMember(new Member(id, name));
                showAlert(Alert.AlertType.INFORMATION, "Success", "Member added successfully!");
                dialog.close();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid input! Please try again.");
            }
        });

        layout.getChildren().addAll(new Label("Enter Member Details:"), idField, nameField, addButton);

        dialog.setScene(new Scene(layout, 300, 200));
        dialog.show();
    }

    private void showAddBookDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add Book");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #FEC9F1; -fx-border-color: #e899dc; -fx-border-width: 2");

        TextField idField = new TextField();
        idField.setPromptText("Book ID");
        TextField titleField = new TextField();
        titleField.setPromptText("Book Title");
        Button addButton = new Button("Add Book");

        addButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String title = titleField.getText();
                library.addBook(new Book(id, title));
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully!");
                dialog.close();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid input! Please try again.");
            }
        });

        layout.getChildren().addAll(new Label("Enter Book Details:"), idField, titleField, addButton);

        dialog.setScene(new Scene(layout, 300, 200));
        dialog.show();
    }

    private void showBorrowBookDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Borrow Book");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #FEC9F1; -fx-border-color: #e899dc; -fx-border-width: 2");

        ComboBox<Member> memberComboBox = new ComboBox<>();
        memberComboBox.getItems().addAll(library.getMembers());
        memberComboBox.setPromptText("Select Member");
        
        memberComboBox.setCellFactory(param -> new ListCell<Member>() {
            @Override
            protected void updateItem(Member member, boolean empty) {
                super.updateItem(member, empty);
                if (empty || member == null) {
                    setText(null);
                } else {
                    setText(member.getId() + " - " + member.getName());
                }
            }
        });
        
        memberComboBox.setButtonCell(new ListCell<Member>() {
            @Override
            protected void updateItem(Member member, boolean empty) {
                super.updateItem(member, empty);
                if (empty || member == null) {
                    setText(null);
                } else {
                    setText(member.getId() + " - " + member.getName());
                }
            }
        });


        ComboBox<Book> bookComboBox = new ComboBox<>();
        bookComboBox.getItems().addAll(library.getBooks().stream().filter(Book::isAvailable).collect(Collectors.toList()));
        bookComboBox.setPromptText("Select Book");
        
        bookComboBox.setCellFactory(param -> new ListCell<Book>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                if (empty || book == null) {
                    setText(null);
                } else {
                    setText(book.getId() + " - " + book.getTitle());
                }
            }
        });
        bookComboBox.setButtonCell(new ListCell<Book>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                if (empty || book == null) {
                    setText(null);
                } else {
                    setText(book.getId() + " - " + book.getTitle());
                }
            }
        });

        
        Button borrowButton = new Button("Borrow Book");

        borrowButton.setOnAction(e -> {
            Member member = memberComboBox.getValue();
            Book book = bookComboBox.getValue();

            if (member != null && book != null) {
                transaction.borrowBook(book, member);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book borrowed successfully!");
                dialog.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid selection! Please try again.");
            }
        });

        layout.getChildren().addAll(
                new Label("Select Member and Book to Borrow:"),
                memberComboBox,
                bookComboBox,
                borrowButton
        );

        dialog.setScene(new Scene(layout, 300, 200));
        dialog.show();
    }

    private void showReturnBookDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Return Book");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #FEC9F1; -fx-border-color: #e899dc; -fx-border-width: 2");

        ComboBox<Member> memberComboBox = new ComboBox<>();
        memberComboBox.getItems().addAll(library.getMembers());
        memberComboBox.setPromptText("Select Member");
        
        memberComboBox.setCellFactory(param -> new ListCell<Member>() {
            @Override
            protected void updateItem(Member member, boolean empty) {
                super.updateItem(member, empty);
                if (empty || member == null) {
                    setText(null);
                } else {
                    setText(member.getId() + " - " + member.getName());
                }
            }
        });
        memberComboBox.setButtonCell(new ListCell<Member>() {
            @Override
            protected void updateItem(Member member, boolean empty) {
                super.updateItem(member, empty);
                if (empty || member == null) {
                    setText(null);
                } else {
                    setText(member.getId() + " - " + member.getName());
                }
            }
        });

        ComboBox<Book> bookComboBox = new ComboBox<>();
        bookComboBox.setPromptText("Select Book to Return");
        
        bookComboBox.setCellFactory(param -> new ListCell<Book>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                if (empty || book == null) {
                    setText(null);
                } else {
                    setText(book.getId() + " - " + book.getTitle());
                }
            }
        });
        bookComboBox.setButtonCell(new ListCell<Book>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                if (empty || book == null) {
                    setText(null);
                } else {
                    setText(book.getId() + " - " + book.getTitle());
                }
            }
        });

        memberComboBox.setOnAction(e -> {
            Member member = memberComboBox.getValue();
            if (member != null) {
                bookComboBox.getItems().setAll(member.getBorrowedBooks());
            }
        });
        

        Button returnButton = new Button("Return Book");

        returnButton.setOnAction(e -> {
            Member member = memberComboBox.getValue();
            Book book = bookComboBox.getValue();

            if (member != null && book != null) {
                transaction.returnBook(book, member);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book returned successfully!");
                dialog.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid selection! Please try again.");
            }
        });

        layout.getChildren().addAll(
                new Label("Select Member and Book to Return:"),
                memberComboBox,
                bookComboBox,
                returnButton
        );

        dialog.setScene(new Scene(layout, 300, 200));
        dialog.show();
    }

    private void showBorrowedBooksDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Borrowed Books");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #FEC9F1; -fx-border-color: #e899dc; -fx-border-width: 2;");

        ComboBox<Member> memberComboBox = new ComboBox<>();
        memberComboBox.getItems().addAll(library.getMembers());
        memberComboBox.setPromptText("Select Member");
        memberComboBox.setCellFactory(param -> new ListCell<Member>() {
            @Override
            protected void updateItem(Member member, boolean empty) {
                super.updateItem(member, empty);
                if (empty || member == null) {
                    setText(null);
                } else {
                    setText(member.getId() + " - " + member.getName());
                }
            }
        });
        memberComboBox.setButtonCell(new ListCell<Member>() {
            @Override
            protected void updateItem(Member member, boolean empty) {
                super.updateItem(member, empty);
                if (empty || member == null) {
                    setText(null);
                } else {
                    setText(member.getId() + " - " + member.getName());
                }
            }
        });

        TextArea borrowedBooksArea = new TextArea();
        borrowedBooksArea.setEditable(false);

        memberComboBox.setOnAction(e -> {
            Member member = memberComboBox.getValue();
            if (member != null) {
                StringBuilder borrowedBooks = new StringBuilder();
                for (Book book : member.getBorrowedBooks()) {
                    borrowedBooks.append(book.getTitle()).append("\n");
                }
                borrowedBooksArea.setText(borrowedBooks.toString());
            }
        });

        layout.getChildren().addAll(
                new Label("Select Member to View Borrowed Books:"),
                memberComboBox,
                borrowedBooksArea
        );

        dialog.setScene(new Scene(layout, 400, 300));
        dialog.show();
    }

    private void showTransactionHistoryDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Transaction History");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #FEC9F1; -fx-border-color: #e899dc; -fx-border-width: 2;");

        TextArea transactionHistoryArea = new TextArea();
        transactionHistoryArea.setEditable(false);
        transactionHistoryArea.setWrapText(true);

        String history = Transaction.getTransaction().getTransactionHistory();
        transactionHistoryArea.setText(history);
        layout.getChildren().addAll(new Label("Transaction History:"), transactionHistoryArea);

        dialog.setScene(new Scene(layout, 400, 300));
        dialog.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

	public static void main(String[] args) {
		launch(args);
	}
}
