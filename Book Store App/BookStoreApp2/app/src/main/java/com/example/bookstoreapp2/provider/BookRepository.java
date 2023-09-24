package com.example.bookstoreapp2.provider;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

// BookRepository communicate with the database to retrieve and manipulate the data from the database.
public class BookRepository {
    private BookDao mBookDao;   // Instance of BookDao, required to extract the data from the database.
    private LiveData<List<Book>> mAllBooks;

    // Constructor
    BookRepository(Application application) {
        // Build a book database if one does not exist yet by calling the getDatabase. Else return an instance of the
        // book database.
        BookDatabase db = BookDatabase.getDatabase(application);
        mBookDao = db.bookDao();
        mAllBooks = mBookDao.getAllBooks();
    }

    // Get all Books, allows the UI to access the Book items.
    LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

    // Insert new Book object into the database.
    void insert(Book book) {
        // Execute the database write function and then add the Book object into the database.
        BookDatabase.databaseWriteExecutor.execute(() -> mBookDao.addBook(book));
    }

    // Delete all Book objects in the database,
    void deleteAll() {
        BookDatabase.databaseWriteExecutor.execute(()-> {
            mBookDao.deleteAllBooks();
        });
    }

    // Delete the last Book object in the database.
    void deleteLast() {
        BookDatabase.databaseWriteExecutor.execute(()-> {
            mBookDao.deleteLast();
        });
    }

    // Delete the Books where the Price is > 50.
    void deleteExpensiveBooks() {
        BookDatabase.databaseWriteExecutor.execute(()-> {
            mBookDao.deleteExpensiveBooks();
        });
    }
}
