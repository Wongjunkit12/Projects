package com.example.bookstoreapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookstoreapp2.provider.Book;
import com.example.bookstoreapp2.provider.BookViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {
    // Declare EditText attributes
    private EditText bookIDInput;
    private EditText bookTitleInput;
    private EditText bookISBNInput;
    private EditText bookAuthorInput;
    private EditText bookDescInput;
    private EditText bookPriceInput;

    // Declared Book attributes
    private String bookID;
    private String bookTitle;
    private String bookISBN;
    private String bookAuthor;
    private String bookDesc;
    private double bookPrice;

    // Saved the bookdata.
    private SharedPreferences bookData;

    // UI layout attributes.
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private RecyclerViewAdapter myAdapter;

    // Gesture Detector
    private GestureDetector myGestureDetector;

    // Touchscreen UI and required attributes.
    private View touchFrameLayout;
    private int xDown;
    private int yDown;

    // The BookViewModel used to interact and communicate with the Book Database.
    private BookViewModel mBookViewModel;

    // Create a DatabaseReference instance variable which will point towards the Firebase Database.
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        // Get Book EditText objects for each user input and set it as data attributes declared above.
        this.bookIDInput = findViewById(R.id.idInput);
        this.bookTitleInput = findViewById(R.id.titleInput);
        this.bookISBNInput = findViewById(R.id.isbnInput);
        this.bookAuthorInput = findViewById(R.id.authorInput);
        this.bookDescInput = findViewById(R.id.descInput);
        this.bookPriceInput = findViewById(R.id.priceInput);

        // Get the book data preference and save it in file1.
        this.bookData = getSharedPreferences("file1", 0);

        // Get the ID for the UI Layouts and store it in their data attributes respectively.
        this.drawerLayout = findViewById(R.id.drawer_layout);
        this.navigationView = findViewById(R.id.navigation_view);
        this.toolbar = findViewById(R.id.toolbar);

        // Get the touch frame layout.
        this.touchFrameLayout = findViewById(R.id.touchFrame);

        // Create a new GestureDetector class and listener.
        this.myGestureDetector = new GestureDetector(this, new MyGestureDetector());

        // Set the action bar to be the custom made toolbar instead of the default one.
        setSupportActionBar(toolbar);

        // Create an action bar toggle for the drawer on the toolbar to open up the menu when the button is pressed.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);     // A drawer listener to listen when the toggle button is pressed.
        toggle.syncState();


        // Listen to which item is selected by the user in the navigation view drawer menu.
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        // Create a new RecyclerViewAdapter.
        myAdapter = new RecyclerViewAdapter();

        // Create a new BookViewModel.
        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        // Get the List of Book Data and pass it to the RecyclerViewAdapter setBook method.
        mBookViewModel.getAllBooks().observe(this, newData -> {
            myAdapter.setBook(newData);
            myAdapter.notifyDataSetChanged();   // Update the ViewAdapter.
        });

        // Get the FirebaseDatabase instance and create a DatabaseReference pointing towards it.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference("books");

        // Call the showFragment method to display the BookFragment at the bottom of the screen.
        showFragment();

        // Request permission to receive SMS.
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        // Create an Intent Filter with the action SMS_FILTER and create a broadcast receiver to listen to any SMS
        // messages received.
        IntentFilter intentFilter = new IntentFilter("SMS_FILTER");
        registerReceiver(myReceiver, intentFilter);     // Execute myReceiver method once SMS received.

        listenTouch();
    }

    // Method to listen and perform actions based on motion events.
    public void listenTouch() {
        touchFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                myGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    // Method to show the fragment at the layout specified.
    public void showFragment() {
        // Create a new instance of BookFragment fragment and set it to the frameLayout layout.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new BookFragment())
                .addToBackStack("fragment1")     // Press back button will go back to the previous fragment.
                .commit();                            // Commit changes to the application.
    }

    // Method to update the book attributes based on the user inputs.
    public void addBook(View view) {
        // Get Book EditText objects for each user input and set it as data attributes declared above.
        this.bookIDInput = findViewById(R.id.idInput);
        this.bookTitleInput = findViewById(R.id.titleInput);
        this.bookISBNInput = findViewById(R.id.isbnInput);
        this.bookAuthorInput = findViewById(R.id.authorInput);
        this.bookDescInput = findViewById(R.id.descInput);
        this.bookPriceInput = findViewById(R.id.priceInput);

        // Get user inputs and set the book attributes based on it.
        this.bookID = bookIDInput.getText().toString();         // Convert the text received from char to string.
        this.bookTitle = bookTitleInput.getText().toString();
        this.bookISBN = bookISBNInput.getText().toString();
        this.bookAuthor = bookAuthorInput.getText().toString();
        this.bookDesc = bookDescInput.getText().toString();
        this.bookPrice = Double.parseDouble(bookPriceInput.getText().toString());   // Convert the string into double as
        // price is a double data type.

        // Call the showAdded method to display a pop up for confirmation on book's title and price.
        showAdded();

        // Save the book attributes on added so that it's restored when app is rebooted.
        saveBook();

        // Add the saved book item to the list view which will show the saved book title and price.
        addBookData();
    }

    // Method to show a popup once button is pressed.
    public void showAdded() {
        // Create a message popup based on the book's title and price.
        Toast bookMessage = Toast.makeText(this, String.format("Book (%s) and the price (%.2f)", bookTitle, bookPrice),
                Toast.LENGTH_SHORT);
        bookMessage.show();     // Display the message.
    }

    // Method called on click of "Clear Fields" button which will clear all the text in the Plain Text.
    public void clearFields() {
        // Clear PlainText fields by getting the text and calling the .clear() method.
        bookIDInput.getText().clear();
        bookTitleInput.getText().clear();
        bookISBNInput.getText().clear();
        bookAuthorInput.getText().clear();
        bookDescInput.getText().clear();
        bookPriceInput.getText().clear();
    }

    // Saves the data attributes when "Add Book" button is pressed.
    public void saveBook() {
        // Saved book data.
        SharedPreferences.Editor bookEditor = bookData.edit();
        // Put the book attributes into the bookEditor with their respective keys to save it.
        bookEditor.putString("id", bookID);
        bookEditor.putString("title", bookTitle);
        bookEditor.putString("isbn", bookISBN);
        bookEditor.putString("author", bookAuthor);
        bookEditor.putString("desc", bookDesc);
        bookEditor.putString("price", String.format("%.2f", bookPrice));  // Convert price double to string by using .format()
        bookEditor.commit();    // Commit the data to persistent storage.
    }

    public void loadSavedState() {
        // Reload saved states and set their respective input fields.
        bookIDInput.setText(bookData.getString("id", ""));
        bookTitleInput.setText(bookData.getString("title", ""));
        bookISBNInput.setText(bookData.getString("isbn", ""));
        bookAuthorInput.setText(bookData.getString("author", ""));
        bookDescInput.setText(bookData.getString("desc", ""));
        bookPriceInput.setText(bookData.getString("price", ""));
    }

    // Method called on click of "Load Book" button which will reload saved data and restore it in the text view.
    public void loadBook() {
        // Retrieve saved book attribute data and set it in their respective input fields.
        loadSavedState();
    }

    // Method to increment the price by one whenever a movement is performed.
    public void incrementPrice() {
        // Get and set the bookPriceInput EditView.
        this.bookPriceInput = findViewById(R.id.priceInput);
        // Parse the price string to double.
        double currentPrice = Double.parseDouble(bookPriceInput.getText().toString());
        currentPrice++;     // Increment the price by one.

        // Set the bookPriceInput EditView to the incremented price with the format being 2 decimal places.
        bookPriceInput.setText(String.format(Locale.getDefault(),"%.2f",  currentPrice));
    }

    // Navigation listener class.
    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Get the ID of the selected item.
            int id = item.getItemId();

            // If the Add Book button is pressed, add the book to the list.
            if (id == R.id.addNewBook) {
                addBookData();
            }
            // Else if the Remove Last Book button is pressed, remove the last book on the database.
            else if (id == R.id.removeLastBook) {
                mBookViewModel.deleteLast();        // Call the deleteLast method to remove last Book Object in database.
                myAdapter.notifyDataSetChanged();   // Update the ViewAdapter to update the changes in dataset.
            }
            // Else if Remove All Books button is pressed, remove all books currently in the database.
            else if (id == R.id.removeAllBooks) {
                mBookViewModel.deleteAll();         // Call the deleteAll method to remove all Book Object in database.
                myRef.removeValue();                // Remove all the Books in the FirebaseDatabase.
                myAdapter.notifyDataSetChanged();   // Update the ViewAdapter to update the changes in dataset.
            }
            // Else if the List All Books button is pressed, go to new activity that lists all books in database.
            else if (id == R.id.listAllBooks) {
                // Create a new intent to go to the new activity when the button is pressed.
                Intent newIntent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(newIntent);      // Start the new activity.
            }
            // Else if the Remove Expensive Books button is pressed, remove all books with price > 50 in the database.
            else if (id == R.id.removeExpensiveBooks) {
                // Call the deleteExpensiveBooks method to remove all Book Object in database with price more than 50.
                mBookViewModel.deleteExpensiveBooks();
                myAdapter.notifyDataSetChanged();       // Update the ViewAdapter to update the changes in dataset.
            }

            // Close the drawer when a button is pressed.
            drawerLayout.closeDrawers();

            // Return true to signify that an item is selected in the navigation menu.
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);    // Inflate the options menu into the toolbar.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Get the ID of the selected item.
        int id = item.getItemId();

        // If the clear fields button in the option menu is pressed.
        if (id == R.id.clearFieldButton) {
            clearFields();      // Call the clear fields method to clear the input fields.
        }
        // Else if the load data button is pressed.
        else if (id == R.id.loadDataButton) {
            loadBook();         // Call the load book method to load the last book saved.
        }

        return super.onOptionsItemSelected(item);
    }

    public void addBookData() {
        // Create a new Book object by taking in user input and setting its data attributes.
        Book book = new Book(
                bookIDInput.getText().toString(),
                bookTitleInput.getText().toString(),
                bookAuthorInput.getText().toString(),
                bookISBNInput.getText().toString(),
                bookDescInput.getText().toString(),
                Double.parseDouble(bookPriceInput.getText().toString())
        );
        // Insert the Book object into the database via .insert method.
        mBookViewModel.insert(book);
        // Push the Book object into the Firebase Database using the DatabaseReference.
        myRef.push().setValue(book);

        myAdapter.notifyDataSetChanged();   // Notify the ViewAdapter to update the view as dataset has changed.
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get SMS message from the intent.
            String message = intent.getStringExtra("SMS_MSG_KEY");

            // Create a message popup based on SMS receive.
            Toast smsMessage = Toast.makeText(context, message,
                    Toast.LENGTH_SHORT);
            smsMessage.show();     // Display the message.

            // Parse incoming message for book data attributes separated by the '|' field.
            StringTokenizer stringTokenizer = new StringTokenizer(message, "|");

            // Parses the incoming message into their respective data attributes each separated by |.
            // Then sets the input fields based on their respective data attributes received from the SMS.
            bookIDInput.setText(stringTokenizer.nextToken());
            bookTitleInput.setText(stringTokenizer.nextToken());
            bookISBNInput.setText(stringTokenizer.nextToken());
            bookAuthorInput.setText(stringTokenizer.nextToken());
            bookDescInput.setText(stringTokenizer.nextToken());
            bookPriceInput.setText(stringTokenizer.nextToken());
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        // Retrieve saved book input data and set it in their respective input fields.
        loadSavedState();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Get the Title and ISBN view data and save it.
        outState.putString("titleViewData", bookTitleInput.getText().toString());
        outState.putString("isbnViewData", bookISBNInput.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore the saved book Title and ISBN view data and set it in their respective fields.
        bookTitleInput.setText(savedInstanceState.getString("titleViewData"));
        bookISBNInput.setText(savedInstanceState.getString("isbnViewData"));

        // Clear the other fields.
        bookIDInput.getText().clear();
        bookAuthorInput.getText().clear();
        bookDescInput.getText().clear();
        bookPriceInput.getText().clear();
    }

    // A GestureDetector class that extends from the SimpleOnGestureListener.
    public class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        // On single tap, generate a random ISBN and set the EditText View to it.
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // Create a random length for the random ISBN from 3 to 8 inclusive.
            int randomLength = RandomGenerator.createRandomNumber(3, 8);
            // Generate a random string for the ISBN with length randomly between 3 to 8 and set the ISBN to that.
            bookISBNInput.setText(RandomGenerator.createRandomString(randomLength));
            return true;
        }

        // On double tap, clear all text fields.
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // Call the clearFields method to clear all the text fields.
            clearFields();
            return true;
        }

        // On horizontal scroll, manipulate the price input. (Constant movement).
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // If any vertical movement is performed, set the text to "untitled".
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                bookTitleInput.setText("untitled");
            }
            // If the movement is right to left horizontally, decrement the price based on distance.
            else if (Math.abs(distanceY) < 40) {
                // Get the priceInput and decrement it based on distance.
                double priceInput = Double.parseDouble(bookPriceInput.getText().toString()) - distanceX;
                // Set the bookPriceInput EditText View to the now decremented priceInput.
                bookPriceInput.setText(String.format(Locale.getDefault(), "%.2f", priceInput));
            }
            // Else if the movement is left to right horizontally, increment the price based on distance.
            else if (Math.abs(distanceY) > 40) {
                // Get the priceInput and increment it based on distance.
                double priceInput = Double.parseDouble(bookPriceInput.getText().toString()) + distanceX;
                // Set the bookPriceInput EditText View to the now incremented priceInput.
                bookPriceInput.setText(String.format(Locale.getDefault(), "%.2f", priceInput));
            }
            return true;
        }

        // The gesture detected the moment the touch is released, fling movement detected, move the app to the background.
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Call moveTaskToBack to mvoe the app to the background.
            moveTaskToBack(true);
            return true;
        }

        // On Long Press, load the default saved values for the book.
        @Override
        public void onLongPress(MotionEvent e) {
            // Call the loadBook method to load the saved book data.
            loadBook();
        }
    }
}