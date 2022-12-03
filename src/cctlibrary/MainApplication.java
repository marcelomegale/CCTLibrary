package cctlibrary;

import cctlibrary.entity.Book;
import cctlibrary.entity.Borrowing;
import cctlibrary.entity.Student;
import cctlibrary.enums.BookInfoEnum;
import cctlibrary.enums.StudentInfoEnum;
import cctlibrary.exception.BookAlreadyBorroewdException;
import cctlibrary.exception.BookNotBorrowedException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The main class of CCT Library System. It permits to execute the system.
 */
public class MainApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CCTLibrary library = new CCTLibrary();
        showMenu(library);
    }

    private static void showMenu(CCTLibrary library) {
        System.out.println("Welcome to CCT Library System!\n");

        int userInput = 0;
        String textToSearch;
        BookInfoEnum bookInfoEnum;
        StudentInfoEnum studentInfoEnum;
        Long studentId;
        String bookTitle;
        Borrowing borrowing;

        Scanner scanner = new Scanner(System.in);
        String invalidOptionMessage = "Invalid option. Choose a valid option.\n";

        do {
            System.out.println("*************************** Choose an option *************************** \n");
            System.out.println("1 - SEARCH A BOOK");
            System.out.println("2 - LIST ALL BOOKS");
            System.out.println("3 - SEARCH FOR A STUDENT");
            System.out.println("4 - LIST ALL STUDENTS");
            System.out.println("5 - REGISTER BOOK LOAN");
            System.out.println("6 - ADD TO WAITING LIST");
            System.out.println("7 - REGISTER BOOK RETURN");
            System.out.println("8 - CONSULT LOANS PER STUDENT");
            System.out.println("9 - EXIT");
            System.out.println("");
            System.out.println("Inform the option:");

            // Getting user option
            try {
                userInput = Integer.parseInt(scanner.nextLine());

                switch (userInput) {
                    case 1:
                        // Search for a specific book by title and/or author first name.
                        System.out.println("Search for a specific book by title and/or author first name.");
                        System.out.println("Choose: " + Arrays.toString(BookInfoEnum.values()));
                        try {
                            bookInfoEnum = BookInfoEnum.valueOf(scanner.nextLine());
                        } catch (IllegalArgumentException ex) {
                            System.out.println(invalidOptionMessage);
                            continue;
                        }
                        System.out.println("Inform text to search:");
                        textToSearch = scanner.nextLine();

                        Book book = library.sortAndSearchBook(bookInfoEnum, textToSearch);
                        if (book == null) {
                            System.out.println("\nBook not found.");
                        } else {
                            System.out.println("\nBook data: \n" + book.toString());
                        }

                        break;
                    case 2:
                        // List all books by title and/or author name alphabetical order.
                        System.out.println("List all books by title and/or author name alphabetical order.");
                        System.out.println("Choose: " + Arrays.toString(BookInfoEnum.values()));
                        try {
                            bookInfoEnum = BookInfoEnum.valueOf(scanner.nextLine());
                        } catch (IllegalArgumentException ex) {
                            System.out.println(invalidOptionMessage);
                            continue;
                        }
                        library.sortBooks(bookInfoEnum);
                        System.out.println("\nAll books: \n" + library.listAllBooks());

                        break;
                    case 3:
                        // Search for a specific student by name and/or ID.
                        System.out.println("Search for a specific student by name and/or ID.");
                        System.out.println("Choose: " + Arrays.toString(StudentInfoEnum.values()));
                        try {
                            studentInfoEnum = StudentInfoEnum.valueOf(scanner.nextLine());
                        } catch (IllegalArgumentException ex) {
                            System.out.println(invalidOptionMessage);
                            continue;
                        }
                        System.out.println("Inform text to search:");
                        textToSearch = scanner.nextLine();

                        Student student = library.sortAndSearchStudent(studentInfoEnum, textToSearch);
                        if (student == null) {
                            System.out.println("\nStudent not found.");
                        } else {
                            System.out.println("\nStudent data: \n" + student.toString());
                        }

                        break;
                    case 4:
                        // List all students by alphabetical name and/or ID order.
                        System.out.println("List all students by alphabetical name and/or ID order.");
                        System.out.println("Choose: " + Arrays.toString(StudentInfoEnum.values()));
                        try {
                            studentInfoEnum = StudentInfoEnum.valueOf(scanner.nextLine());
                        } catch (IllegalArgumentException ex) {
                            System.out.println(invalidOptionMessage);
                            continue;
                        }
                        library.sortStudents(studentInfoEnum);
                        System.out.println("\nAll students: \n" + library.listAllStudents());

                        break;
                    case 5:
                        // Register that a student has borrowed a book.
                        System.out.println("Register that a student has borrowed a book.");
                        System.out.println("Inform student ID:");
                        studentId = Long.parseLong(scanner.nextLine());
                        System.out.println("Inform book title:");
                        bookTitle = scanner.nextLine();

                        try {
                            borrowing = library.borrowBook(studentId, bookTitle);
                            if (borrowing != null) {
                                System.out.println("\nSuccessfully borrowed book!");
                                System.out.println(borrowing);
                            } else {
                                System.out.println("\nUnable to borrow the book. Possibly "
                                        + "the student or the book was not found.");
                            }
                        } catch (BookAlreadyBorroewdException ex) {
                            System.out.println("\nUnable to borrow the book because it already "
                                    + "borrowed to student: \n" + ex.getMessage() + "\n");
                        }

                        break;
                    case 6:
                        // If a book is borrowed and another student wants to borrow 
                        // it, allow the user to add that reader to a waiting list (queue).
                        System.out.println("Register a student is waiting a book.");
                        System.out.println("Inform student ID:");
                        studentId = Long.parseLong(scanner.nextLine());
                        System.out.println("Inform book title:");
                        bookTitle = scanner.nextLine();

                        boolean ok = library.addWaitingList(studentId, bookTitle);
                        if (ok) {
                            System.out.println("\nSuccessfully add student on waiting list!");

                        } else {
                            System.out.println("Unable to add student on waiting list. Possibly "
                                    + "the student or the book was not found.");
                        }

                        break;
                    case 7:
                        // Register that a student has returned a book.
                        // If a book is returned and has a waiting queue, display to 
                        // the user the next student waiting for that book.
                        System.out.println("Register that a student has returned a book.");
                        System.out.println("Inform book title:");
                        bookTitle = scanner.nextLine();

                        Student nextStudentWaiting = null;
                        try {
                            nextStudentWaiting = library.returnBook(bookTitle);
                            System.out.println("\nSuccessfully return book!");
                            if (nextStudentWaiting != null) {
                                System.out.println("\nNext student waiting for the book:");
                                System.out.println(nextStudentWaiting);
                            }
                        } catch (BookNotBorrowedException ex) {
                            System.out.println("\nBook is not borrowed or not found: " + ex.getMessage());
                        }
                        
                        break;
                    case 8:
                        // For a specific student, list the books that they have borrowed.
                        System.out.println("List the books a student has borrowed.");
                        System.out.println("Inform student ID:");
                        studentId = Long.parseLong(scanner.nextLine());
                        
                        List<Book> books = library.booksByStudent(studentId);
                        if (books == null || books.isEmpty()) {
                            System.out.println("The student did not borrow any books.");
                        } else {
                            System.out.println("Books:");
                            System.out.println(books);
                        }

                        break;
                    case 9:
                        System.out.println("Exiting.");
                        break;

                    default:
                        System.out.println(invalidOptionMessage);
                        break;
                }

                System.out.println("Press enter to continue...");
                scanner.nextLine();

            } catch (NumberFormatException e) {
                System.out.println(invalidOptionMessage);
            }
        } while (userInput != 9);
    }
}
