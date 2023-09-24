package com.example.bookcontentresolver.provider;

public class Book {
    // Declared Book attributes.
    private String bookID;
    private String bookTitle;
    private String bookISBN;
    private String bookAuthor;
    private String bookDesc;
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
