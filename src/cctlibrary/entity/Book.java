package cctlibrary.entity;

import java.util.Objects;

/**
 * This class represents the Book entity.
 */
public class Book {
    
    private String id;
 
    private String authorFirstName;
    
    private String authorLastName;
    
    private String title;
    
    private String[] genres;

    public Book(String id, String authorFirstName, String authorLastName, String title, 
            String[] genres) {
        this.id = id;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.title = title;
        this.genres = genres;
    }
    
    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public String[] getGenres() {
        return genres;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", authorFirstName=" + authorFirstName + 
                ", authorLastName=" + authorLastName + ", title=" + title + '}';
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        final Book other = (Book) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }    
}
