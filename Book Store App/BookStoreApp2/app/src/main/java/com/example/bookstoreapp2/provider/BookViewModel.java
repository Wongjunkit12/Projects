package com.example.bookstoreapp2.provider;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

// BookViewModel communicate with the BookRepository
public class BookViewModel extends AndroidViewModel {
    // The BookRepository which will be used to communicate with the Book Database.
    private BookRepository mRepository;
    // The Book items in the database.
    private LiveData<List<Book>> mAllBooks;

    // Construct a ViewModel.
    public BookViewModel(@NonNull Application application) {
        super(application);
        mRepository = new BookRepository(application);  // Create a new instance of the book repository.
        mAllBooks = mRepository.getAllBooks();
    }

    // Return all the Book objects in the database. Calls the repository method to communicate with the database.
    public LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

    // Insert a new Book object into the database. Calls the repository method to communicate with the database.
    public void insert(Book book) {
        mRepository.insert(book);
    }

    // Delete all Book objects in the database. Calls the repository method to communicate with the database.
    public void deleteAll(){
        mRepository.deleteAll();
    }

    // Delete the last Book object in the database. Calls the repository method to communicate with the database.
    public void deleteLast(){
        mRepository.deleteLast();
    }

    // Delete the Books in the database where the Price is > 50.
    public void deleteExpensiveBooks() {
        mRepository.deleteExpensiveBooks();
    }
}
