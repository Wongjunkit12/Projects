package com.example.bookstoreapp2.provider;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class BookContentProvider extends ContentProvider {
    private BookDatabase db;        // The BookDatabase instance.
    private final String TABLE_NAME = "books";      // Name of the database.
    // The authority of the content.
    public static final String CONTENT_AUTHORITY = "monash.edu.week8";
    // The URI address to access the database.
    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public BookContentProvider() {}

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletionCount;      // The index to delete the Book.

        deletionCount = db
                .getOpenHelper()
                // Write in the Database.
                .getWritableDatabase()
                // Specific conditionals on what data to delete.
                .delete(TABLE_NAME, selection, selectionArgs);
        return deletionCount;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        // When the URI is valid/accessible or not.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Use the database and insert a new
        long rowId = db
                .getOpenHelper()
                // Write on the Database.
                .getWritableDatabase()
                // Insert ContentValues container, where data is put in, into the Database Table.
                .insert(TABLE_NAME, 0, values);
        // Append the rowId with the CONTENT_URI when wanting to insert.
        return ContentUris.withAppendedId(CONTENT_URI, rowId);
    }

    @Override
    public boolean onCreate() {
        // Get the BookDatabase onCreate, passing the context into the getDatabase method.
        db = BookDatabase.getDatabase(getContext());
        return true;
    }

    // Method to use the query the database and return data to caller.
    // Uri maps to the table name.
    // projection is a list of columns in each row.
    // selection is a string represents the where clause in the Database.
    // selectionArgs is an ArrayList of strings representing values that should be embedded in the selection statement.
    // selectionArgs is to fill in the '?' in the selection.
    // sortOrder is whether the database is sorted in ascending or descending order with a certain column.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Uses the SQLiteQueryBuilder which allows us to manipulate our data.
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        // Set the Database Table to our BookDatabase.
        builder.setTables(TABLE_NAME);
        // The builder will run the query to access the data in the database.
        String query = builder.buildQuery(projection, selection, null, null, sortOrder, null);
        // The Cursor is querying the object based on the query we created, and then input in the Cursor.
        final Cursor cursor = db
                // Access the Database.
                .getOpenHelper()
                // Read the database.
                .getReadableDatabase()
                .query(query);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updateCount;

        updateCount = db
                .getOpenHelper()
                // Write in the table.
                .getWritableDatabase()
                // Update the database based on the ContentValue container and conditionals for selection.
                .update(TABLE_NAME, 0, values, selection, selectionArgs);
        return updateCount;
    }
}
