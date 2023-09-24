package com.example.bookstoreapp2.provider;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Create a database with all the entities/attributes from the Book class.
@Database(entities = {Book.class}, version = 1)
public abstract class BookDatabase extends RoomDatabase {
    // Name of database.
    public static final String BOOK_DATABASE_NAME = "book_database";
    public abstract BookDao bookDao();

    // Marking the database instance as volatile to ensure atomic access to the variable.
    // Create an instance of a book database.
    private static volatile BookDatabase INSTANCE;
    // Number of threads reserved for database.
    private static final int NUMBER_OF_THREADS = 4;

    // Manipulate the database.
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // To return the Database if it exist, if does not create one.
    static BookDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookDatabase.class) {
                // If no instance of the book database have been created yet, build one using the database name and Book
                // entities.
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BookDatabase.class, BOOK_DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;    // Return the database instance itself.
    }
}

