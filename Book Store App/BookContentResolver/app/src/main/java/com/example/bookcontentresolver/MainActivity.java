package com.example.bookcontentresolver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.bookcontentresolver.provider.Book;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    // The TextView to set the Number of Books.
    private TextView textView;
    // The URI of the database.
    private Uri uri;

    // Create a DatabaseReference instance variable which will point towards the Firebase Database.
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Parse the uri of the ContentProvider.
        uri = Uri.parse("content://monash.edu.week8/books");

        // Get the FirebaseDatabase instance and create a DatabaseReference pointing towards it.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference("books");

        // Call the getBooks to display the number of Books in the Database on the TextView on creation,
        getBooks(null);
    }

    // Get the number of Books in the Database in BookStoreApp2.
    public void getBooks(View v) {
        textView = findViewById(R.id.numberOfBooks);    // Get the TextView to set the number of books.
        // Query with the ContentProvider using the uri to get the Cursor result.
        Cursor result = getContentResolver().query(uri, null, null, null);

        // If no result returned, set the TextView to null as failed to make query.
        if (result == null) {
            textView.setText("null");
        }
        // Else set the TextView to the number of Books inside the database.
        else {
            textView.setText(result.getCount() + "");
        }
    }

    // Method to add a predefined book into the Database
    public void addBook(View v){
        // Book data attributes
        String bookID = "01234";
        String bookTitle = "Lord of the Rings";
        String bookISBN = "mno312";
        String bookAuthor = "J R R Tolkien";
        String bookDesc = "Fantasy";
        double bookPrice = 30;

        // Put all the book data attributes into a ContentValues container.
        ContentValues contentValues = new ContentValues();
        contentValues.put("bookId", bookID);
        contentValues.put("bookTitle", bookTitle);
        contentValues.put("bookISBN", bookISBN);
        contentValues.put("bookAuthor", bookAuthor);
        contentValues.put("bookDesc", bookDesc);
        contentValues.put("bookPrice", bookPrice);

        getContentResolver().insert(uri, contentValues);    // Insert the ContentValues created above into the Database.

        // Create amd push the book object to be pushed into the FirebaseDatabase.
        Book book = new Book(bookID, bookTitle, bookISBN, bookAuthor, bookDesc, bookPrice);
        myRef.push().setValue(book);
    }

    // Remove all Books in the Database.
    public void removeAllBooks(View v) {
        getContentResolver().delete(uri, null, null);
        myRef.removeValue();
    }
}