package com.example.bookstoreapp2;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreapp2.provider.Book;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    // The List of Books.
    private List<Book> bookData;

    // Create an empty ArrayList for the bookData.
    public RecyclerViewAdapter() {
        this.bookData = new ArrayList<>();
    }

    // Set the bookData ArrayList to the input given.
    public void setBook(List<Book> bookData) {
        this.bookData = bookData;
    }

    // Create a ViewHolder.
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CardView inflated as RecyclerView list item to hold the data.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        // Create a ViewHolder class and pass the view to it. The ViewHolder class is to bind the view.
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    // Bind the contents in the ArrayList to the ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        // Set the TextView in the CardView to be the book data attributes.
        holder.id.setText(String.format("Key: " + bookData.get(position).getId()));
        holder.cardID.setText(String.format("ID: %s", bookData.get(position).getBookID()));
        holder.cardTitle.setText(String.format("Title: %s", bookData.get(position).getBookTitle()));
        holder.cardISBN.setText(String.format("ISBN: %s", bookData.get(position).getBookISBN()));
        holder.cardAuthor.setText(String.format("Author: %s", bookData.get(position).getBookAuthor()));
        holder.cardDesc.setText(String.format("Desc: %s", bookData.get(position).getBookDesc()));
        holder.cardPrice.setText(String.format("Price: %.2f", bookData.get(position).getBookPrice()));
    }

    // Method to return the size of the ArrayList.
    @Override
    public int getItemCount() {
        return bookData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // The TextViews inside the Recycler View.
        public View itemView;
        public TextView id;
        public TextView cardID;
        public TextView cardTitle;
        public TextView cardAuthor;
        public TextView cardISBN;
        public TextView cardDesc;
        public TextView cardPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            // Assign all the data attributes for each and every TextView of the book inside the
            // Recycler View.
            this.itemView = itemView;
            id = itemView.findViewById(R.id.id);
            cardID = itemView.findViewById(R.id.cardID);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardAuthor = itemView.findViewById(R.id.cardAuthor);
            cardISBN = itemView.findViewById(R.id.cardISBN);
            cardDesc = itemView.findViewById(R.id.cardDesc);
            cardPrice = itemView.findViewById(R.id.cardPrice);
        }
    }
}
