package com.example.bookstoreapp2.provider;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// Book Data Access Object.
@Dao
public interface BookDao {
    // Sellect data from the books database.
    // Save all the Books as a list.
    @Query("select * from books")
    LiveData<List<Book>> getAllBooks();

    // Insert a Book into the database.
    @Insert
    void addBook(Book book);

    // Delete all books in the database.
    @Query("delete FROM books")
    void deleteAllBooks();

    // Delete the last book from the database by getting the max id in the books databse and deleting that row.
    @Query("delete from books where id = (select max(id) from books)")
    void deleteLast();

    // Delete all books in which the price is more than 50.
    @Query("delete from books where bookPrice > 50")
    void deleteExpensiveBooks();
}

