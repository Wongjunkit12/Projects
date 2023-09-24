package com.example.bookstoreapp2.provider;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Create this as an entity to be used in a database.
@Entity(tableName = "books")
public class Book {
    // Create a table with the column name as id as the first column.
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    // Declared Book attributes and their respective column headers.
    @ColumnInfo(name = "bookId")
    private String bookID;
    @ColumnInfo(name = "bookTitle")
    private String bookTitle;
    @ColumnInfo(name = "bookISBN")
    private String bookISBN;
    @ColumnInfo(name = "bookAuthor")
    private String bookAuthor;
    @ColumnInfo(name = "bookDesc")
    private String bookDesc;
    @ColumnInfo(name = "bookPrice")
    private double bookPrice;

    public Book(String bookID, String bookTitle, String bookISBN, String bookAuthor, String bookDesc, double bookPrice) {
        // Call the setters to set the Book data attributes.
        setBookID(bookID);
        setBookTitle(bookTitle);
        setBookISBN(bookISBN);
        setBookAuthor(bookAuthor);
        setBookDesc(bookDesc);
        setBookPrice(bookPrice);
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getBookID() {
        return bookID;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public String getBookISBN() {
        return bookISBN;
    }
    public String getBookAuthor() {
        return bookAuthor;
    }
    public String getBookDesc() {
        return bookDesc;
    }
    public double getBookPrice() {
        return bookPrice;
    }

    // Setters
    public void setId(@NonNull int id) {
        this.id = id;
    }
    public void setBookID(String bookID) {
        this.bookID = bookID;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }
    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }
}
