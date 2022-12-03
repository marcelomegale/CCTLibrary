package cctlibrary;

import cctlibrary.entity.Address;
import cctlibrary.exception.BookNotBorrowedException;
import cctlibrary.exception.BookAlreadyBorroewdException;
import cctlibrary.entity.Book;
import cctlibrary.entity.Borrowing;
import cctlibrary.entity.Student;
import cctlibrary.enums.BookInfoEnum;
import cctlibrary.enums.BorrowingInfoEnum;
import cctlibrary.enums.StudentInfoEnum;
import cctlibrary.struct.CustomQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This class represents the CCT Library. 
 * 
 */
public class CCTLibrary {
    
    private static final String COMMA = ",";
    
    private static final String DOUBLE_QUOTES = "\"";
    
    private static final String PIPE = "|";
    
    private CustomQueue<Book> books;
    
    private CustomQueue<Student> students;
    
    private CustomQueue<Borrowing> borrowings;
    
    private Map<String, CustomQueue<Student>> waitingList;

    public CCTLibrary() {
        loadData();
    }
    
    /**
     * Loads CCT Library data from text files.
     */
    private void loadData() {
        this.books = loadBooks();
        this.students = loadStudents();
        this.borrowings = loadBorrowings();
        this.waitingList = new HashMap<>();
    }
    
    /**
     * Load students from text file.
     * @return 
     */
    private CustomQueue<Student> loadStudents() {
                
        CustomQueue<Student> queue = new CustomQueue<>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try (Scanner scanner = new Scanner(new File("STUDENT_DATA.csv"))) {

            // Read line
            if (scanner.hasNextLine()) {
                // Discarting title
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                String studentId = line.substring(0, line.indexOf(COMMA));
                line = line.substring(line.indexOf(COMMA) + 1);
                
                String name = line.substring(0, line.indexOf(COMMA));
                line = line.substring(line.indexOf(COMMA) + 1);
                
                String birthdate = line.substring(0, line.indexOf(COMMA));
                line = line.substring(line.indexOf(COMMA) + 1);
                
                String streetAddress = line.substring(0, line.indexOf(COMMA));
                line = line.substring(line.indexOf(COMMA) + 1);
                
                String city = line.substring(0, line.indexOf(COMMA));
                line = line.substring(line.indexOf(COMMA) + 1);
                
                String state = line.substring(0, line.indexOf(COMMA));
                line = line.substring(line.indexOf(COMMA) + 1);
                
                String zipCode = line;
     
                Address address = new Address(streetAddress, city, state, zipCode);
                Student student = new Student(Long.parseLong(studentId), name, df.parse(birthdate), address);
                          
                queue.insert(student);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to load students. File not found.");
        } catch (ParseException pe) {
            System.out.println("Unable to load students. Invalid birth date.");
        }
        
        return queue;
    }

    /**
     * Load books from text file.
     * 
     * @return 
     */
    private CustomQueue<Book> loadBooks() {
        CustomQueue<Book> queue = new CustomQueue<>();
        
        try (Scanner scanner = new Scanner(new File("BOOK_DATA.csv"))) {

            // Read line
            if (scanner.hasNextLine()) {
                // Discarting title
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                String bookId = line.substring(0, line.indexOf(COMMA));
                line = line.substring(line.indexOf(COMMA) + 1);
                
                String authorFirstName = line.substring(0, line.indexOf(COMMA));
                line = line.substring(line.indexOf(COMMA) + 1);
                
                String authorLastName = line.substring(0, line.indexOf(COMMA));
                line = line.substring(line.indexOf(COMMA) + 1);
                
                String bookTitle;
                if (line.startsWith("\"")) {
                    bookTitle = line.substring(1, line.lastIndexOf(DOUBLE_QUOTES));
                    line = line.substring(line.lastIndexOf(DOUBLE_QUOTES) + 2);
                } else {
                    bookTitle = line.substring(0, line.indexOf(COMMA));
                    line = line.substring(line.indexOf(COMMA) + 1);
                }
                
                String[] genre = line.split(PIPE);
                
                Book book = new Book(bookId, authorFirstName, authorLastName, bookTitle, genre);
                queue.insert(book);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to load books. File not found.");
        }
        
        return queue;
    }
    
    /**
     * Load borrowings from text file.
     * 
     * @return 
     */
    private CustomQueue<Borrowing> loadBorrowings() {
        CustomQueue<Borrowing> queue = new CustomQueue<>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try (Scanner scanner = new Scanner(new File("BORROWING_DATA.csv"))) {

            // Read line
            if (scanner.hasNextLine()) {
                // Discarting title
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) { // Condition
                String line = scanner.nextLine();
                
                String id = line.substring(0, line.indexOf(COMMA));
                line = line.substring(line.indexOf(COMMA) + 1);
                
                String studentId = line.substring(0, line.indexOf(COMMA));
                line = line.substring(line.indexOf(COMMA) + 1);
                
                String bookTitle;
                if (line.startsWith("\"")) {
                    bookTitle = line.substring(1, line.lastIndexOf(DOUBLE_QUOTES));
                    line = line.substring(line.lastIndexOf(DOUBLE_QUOTES) + 2);
                } else {
                    bookTitle = line.substring(0, line.indexOf(COMMA));
                    line = line.substring(line.indexOf(COMMA) + 1);
                }
                
                String borrowingDate = line;          
                
                Student student = sortAndSearchStudent(StudentInfoEnum.ID, studentId);
                Book book = sortAndSearchBook(BookInfoEnum.TITLE, bookTitle);
                
                Borrowing borrowing = new Borrowing(Long.parseLong(id), student, book, df.parse(borrowingDate));                                 
                queue.insert(borrowing);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to load borrowing. File not found.");
        } catch (ParseException pe) {
            System.out.println("Unable to load borrowing. Invalid borrowing date.");
        }
        
        return queue;
    }

    /**
     * Sort books to search a book.
     * 
     * @param info Sort and search type.
     * @param textToSearch Content to search.
     * @return Book if found or null
     */
    public Book sortAndSearchBook(BookInfoEnum info, String textToSearch) {
        sortBooks(info);
        return searchBook(info, textToSearch);
    }

    /**
     * Sort students to search a student.
     * 
     * @param info Sort and search type.
     * @param textToSearch Content to search.
     * @return Student if found or null
     */
    public Student sortAndSearchStudent(StudentInfoEnum info, String textToSearch) {
        sortStudents(info);
        return searchStudent(info, textToSearch);
    }
    
    /**
     * Sort borrowings to search a Borrowing. 
     * 
     * @param info Sort and search type.
     * @param textToSearch ontent to search.
     * @return Borrowing if found or null
     */
    public Borrowing sortAndSearchBorrowing(BorrowingInfoEnum info, String textToSearch) {
        sortBorrowings(info);
        return searchBorrowing(info, textToSearch);
    }
    
    /**
     * Sort books. Algorithm to sort: Bubble Sort
     * 
     * 
     * @param info Sort type
     */
    public void sortBooks(BookInfoEnum info) {
        Book hold;
        int n = this.books.getSize();
        boolean switched = true;
        String text1, text2;
        
        for (int pass = 0; pass < n-1 && switched; pass++) {
            // External repeat controls the number of passes
            
            switched = false; // Initially no exchanges were made on this passage
            for (int j = 0; j < n-pass-1; j++) {
                // Inner repetition controls each individual pass 
                switch (info) {
                    case AUTHOR:
                        text1 = this.books.get(j).getAuthorFirstName();
                        text2 = this.books.get(j+1).getAuthorFirstName();
                        break;
                    default:
                        text1 = this.books.get(j).getTitle();
                        text2 = this.books.get(j+1).getTitle();
                }
                
                if (text1.compareTo(text2) > 0) {
                    // Elements out of order, replacement is required
                    switched = true;
                    hold = this.books.get(j);
                    this.books.set(j, this.books.get(j+1));
                    this.books.set(j+1, hold);
                }
            }
        }
    }
    
    /**
     * Search a book. Algorithm do search: Binary Search
     * 
     * @param info Search type
     * @param textToSearch Title or author first name to search
     */
    private Book searchBook(BookInfoEnum info, String textToSearch) {
        if (textToSearch == null) {
            return null;
        }
        
        String text;
        int low = 0;
        int hi = this.books.getSize() -1;
        while ( low <= hi ) {
            int mid = (low + hi) / 2;
            Book hold = this.books.get(mid);
            if (BookInfoEnum.AUTHOR.equals(info)) {
                text = hold.getAuthorFirstName();
                
            } else {
                text = hold.getTitle();
            }
            if (textToSearch.equalsIgnoreCase(text)) {
                return hold;
            }
            if (textToSearch.compareToIgnoreCase(text) < 0) {
                hi = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        
        return null;
    }
    
    /**
     * Sort students. Algorithm to sort: Bubble Sort
     * 
     * 
     * @param info  Sort type
     */
    public void sortStudents(StudentInfoEnum info) {
        Student hold;
        int n = this.students.getSize();
        boolean switched = true;
        String text1 = "", text2 = "";
        Long number1 = 0l, number2 = 0l;
        
        for (int pass = 0; pass < n-1 && switched; pass++) {
            // External repeat controls the number of passes
            
            switched = false; // Initially no exchanges were made on this passage
            for (int j = 0; j < n-pass-1; j++) {
                // Inner repetition controls each individual pass 
                boolean localSwitched = false;
                switch (info) {
                    case NAME:
                        text1 = this.students.get(j).getName();
                        text2 = this.students.get(j+1).getName();
                        if (text1.compareTo(text2) > 0) {
                            switched = true;
                            localSwitched = true;
                        }
                        break;
                    default:
                        number1 = this.students.get(j).getId();
                        number2 = this.students.get(j+1).getId();       
                        if (number1 > number2) {
                            switched = true;
                            localSwitched = true;
                        }
                }
                
                if (localSwitched) {
                    // Elements out of order, replacement is required
                    hold = this.students.get(j);
                    this.students.set(j, this.students.get(j+1));
                    this.students.set(j+1, hold);
                }
            }
        }
    }

    /**
     * Search a student. Algorithm do search: Binary Search
     * 
     * @param info  Search type
     * @param textToSearch Name or ID to search
     */
    private Student searchStudent(StudentInfoEnum info, String textToSearch) {
        if (textToSearch == null) {
            return null;
        }
        
        String text = "";
        Long number = 0l, numberToSearch = 0l;
        int low = 0;
        int hi = this.students.getSize() -1;
        while ( low <= hi ) {
            int mid = (low + hi) / 2;
            boolean isBefore = false;
            Student hold = this.students.get(mid);
            if (StudentInfoEnum.NAME.equals(info)) {
                text = hold.getName();
                if (textToSearch.equalsIgnoreCase(text)) {
                    return hold;
                }
                if (textToSearch.compareToIgnoreCase(text) < 0) {
                    isBefore = true;
                }
            } else {
                number = hold.getId();
                try {
                    numberToSearch = Long.valueOf(textToSearch);  
                } catch (NumberFormatException e) {
                    System.out.println("Invalid student ID: " + textToSearch);
                    return null;
                }
                if (numberToSearch.equals(number)) {
                    return hold;
                }
                if (numberToSearch < number) {
                    isBefore = true;
                }
            }
            
            if (isBefore) {
                hi = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        
        return null;
    }
    
    /**
     * Sort borrowings. Algorithm to sort: Bubble Sort
     * 
     * @param  info Sort type
     */
    private void sortBorrowings(BorrowingInfoEnum info) {
        Borrowing hold;
        int n = this.borrowings.getSize();
        boolean switched = true;
        String text1 = "", text2 = "";
        Long number1 = 0l, number2 = 0l;
        
        for (int pass = 0; pass < n-1 && switched; pass++) {
            // External repeat controls the number of passes
            
            switched = false; // Initially no exchanges were made on this passage
            for (int j = 0; j < n-pass-1; j++) {
                // Inner repetition controls each individual pass 
                boolean localSwitched = false;
                switch (info) {
                    case BOOK:
                        text1 = this.borrowings.get(j).getBook().getTitle();
                        text2 = this.borrowings.get(j+1).getBook().getTitle();
                        if (text1.compareTo(text2) > 0) {
                            // Elements out of order, replacement is required
                            switched = true;
                            localSwitched = true;
                        }
                        break;
                    default:
                        number1 = this.borrowings.get(j).getStudent().getId();
                        number2 = this.borrowings.get(j+1).getStudent().getId();
                        if (number1 > number2) {
                            // Elements out of order, replacement is required
                            switched = true;
                            localSwitched = true;
                        }
                }
                          
                if (localSwitched) {
                    // Elements out of order, replacement is required
                    hold = this.borrowings.get(j);
                    this.borrowings.set(j, this.borrowings.get(j+1));
                    this.borrowings.set(j+1, hold);
                }
            }
        }
    }

    /**
     * Search a borrowing. Algorithm do search: Binary Search
     * 
     * @param info Search type
     * @param textToSearch
     * @return 
     */
    private Borrowing searchBorrowing(BorrowingInfoEnum info, String textToSearch) {
        if (textToSearch == null) {
            return null;
        }
        
        String text = "";
        Long number = 0l, numberToSearch = 0l;
        int low = 0;
        int hi = this.borrowings.getSize() -1;
        while ( low <= hi ) {
            int mid = (low + hi) / 2;
            boolean isBefore = false;
            Borrowing hold = this.borrowings.get(mid);
            
            switch (info) {
                case BOOK:
                    text = hold.getBook().getTitle();
            
                    if (textToSearch.equalsIgnoreCase(text)) {
                        return hold;
                    }
                    if (textToSearch.compareToIgnoreCase(text) < 0) {
                        isBefore = true;
                    }
                    break;
                default:
                    number = hold.getStudent().getId();
            
                    try {
                        numberToSearch = Long.valueOf(textToSearch);  
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid student ID: " + textToSearch);
                        return null;
                    }
                    if (numberToSearch.equals(number)) {
                        return hold;
                    }
                    if (numberToSearch < number) {
                        isBefore = true;
                    }
            }
            
            if (isBefore) {
                hi = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        
        return null;
    }

    public String listAllBooks() {
        return this.books.toString();
    }

    public String listAllStudents() {
        return this.students.toString();
    }

    /**
     * Borrow a book to a student.
     * 
     * @param studentId
     * @param bookTitle
     * @return
     * @throws BookAlreadyBorroewdException 
     */
    public Borrowing borrowBook(Long studentId, String bookTitle) throws BookAlreadyBorroewdException {
        if (studentId == null || bookTitle == null) {
            return null;
        }
        
        Student student = sortAndSearchStudent(StudentInfoEnum.ID, studentId.toString());
        Book book = sortAndSearchBook(BookInfoEnum.TITLE, bookTitle);
        if (student == null || book == null) {
            return null;
        }
        
        Borrowing borrowing = sortAndSearchBorrowing(BorrowingInfoEnum.BOOK, book.getTitle());
        if (borrowing != null) {
            throw new BookAlreadyBorroewdException(borrowing.getStudent().toString());
        }
        
        Date now = new Date();
        borrowing = new Borrowing(now.getTime(), student, book, now);
        this.borrowings.insert(borrowing);
        
        return borrowing;
    }

    /**
     * Add a student on the waiting list of a book.
     * 
     * @param studentId
     * @param bookTitle
     * @return 
     */
    public boolean addWaitingList(Long studentId, String bookTitle) {
        if (studentId == null || bookTitle == null) {
            return false;
        }
        
        Student student = sortAndSearchStudent(StudentInfoEnum.ID, studentId.toString());
        Book book = sortAndSearchBook(BookInfoEnum.TITLE, bookTitle);
        if (student == null || book == null) {
            return false;
        }
        
        CustomQueue<Student> students;
        if (waitingList.containsKey(bookTitle)) {
            students = waitingList.get(bookTitle);
            students.insert(student);
        } else {
            students = new CustomQueue<>();
            students.insert(student);
            waitingList.put(bookTitle, students);
        }
        
        return true;
    }

    /**
     * Register that a student has returned a book. If a book is returned and has 
     * a waiting queue, display to the user the next student waiting for that book.
     * 
     * @param bookTitle The book being returned.
     * @return Next student waiting for the book.
     * 
     * @throws BookNotBorrowedException If the book is not borrowed.
     */
    public Student returnBook(String bookTitle) throws BookNotBorrowedException {
        if (bookTitle == null) {
            throw new BookNotBorrowedException("");
        }
        
        Book book = sortAndSearchBook(BookInfoEnum.TITLE, bookTitle);
        if (book == null) {
            throw new BookNotBorrowedException(bookTitle);
        }
        
        Borrowing borrowing = sortAndSearchBorrowing(BorrowingInfoEnum.BOOK, book.getTitle());
        if (borrowing == null) {
            throw new BookNotBorrowedException(book.toString());
        }   
        this.borrowings.remove(borrowing);
        
        // Searching book on waiting list.
        if (this.waitingList.containsKey(book.getTitle())) {
            CustomQueue<Student> studentsWaiting = this.waitingList.get(book.getTitle());
            return studentsWaiting.remove();
        }
        
        return null;
    }

    /**
     * For a specific student, list the books that they have borrowed.
     * 
     * @param studentId
     * @return List of books the student has borrowed.
     */
    List<Book> booksByStudent(Long studentId) {
        List<Book> books = new ArrayList<>();
        
        Student student = sortAndSearchStudent(StudentInfoEnum.ID, studentId.toString());
        if (student == null) {
            return books;
        }
        
        sortBorrowings(BorrowingInfoEnum.STUDENT);
        
        int i = 0, index = -1;
        while ((index == -1) && (i < this.borrowings.getSize())) {
            student = this.borrowings.get(i).getStudent();
            
            if (student.getId().equals(studentId)) {
                index = i;
            }
            i++;
        }
        
        if (index != -1) {
            while ((student.getId().equals(studentId)) && index < this.borrowings.getSize()) {
                books.add(this.borrowings.get(index).getBook());
                index++;
                student = this.borrowings.get(index).getStudent();
            }
        }
        
        return books;
    }
    
}
