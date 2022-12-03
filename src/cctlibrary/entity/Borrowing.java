package cctlibrary.entity;

import java.util.Date;
import java.util.Objects;

/**
 * This class represents the Borrowing entity.
 */
public class Borrowing {
    
    private Long id;
    
    private Student student;
    
    private Book book;
    
    private Date borrowingDate;

    public Borrowing(Long id, Student student, Book book, Date borrowingDate) {
        this.id = id;
        this.student = student;
        this.book = book;
        this.borrowingDate = borrowingDate;
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Book getBook() {
        return book;
    }

    public Date getBorrowingDate() {
        return borrowingDate;
    }

    @Override
    public String toString() {
        return "Borrowing{" + "id=" + id + ", student=" + student + ", book=" + 
                book + ", borrowingDate=" + borrowingDate + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Borrowing other = (Borrowing) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
   
}
