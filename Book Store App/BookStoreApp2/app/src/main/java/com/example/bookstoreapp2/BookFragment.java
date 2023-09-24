package com.example.bookstoreapp2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookstoreapp2.provider.BookViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerViewAdapter myAdapter;
    private BookViewModel mBookViewModel;

    public BookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookFragment newInstance(String param1, String param2) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment in order for the view components in this fragment to be accessed and viewed.
        View layout = inflater.inflate(R.layout.fragment_book, container, false);

        // Get the RecyclerView in the fragment_book layout and create a new layoutManager.
        RecyclerView recyclerView = layout.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(layout.getContext());
        recyclerView.setLayoutManager(layoutManager);   // Set the layoutManager to the RecyclerView.

        // Create a new RecyclerViewAdapter and set the recyclerView layout to the adapter in order to update changes
        // to database.
        myAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(myAdapter);

        // Create a BookViewModel to extract data from the database and display them in the RecyclerView.
        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        mBookViewModel.getAllBooks().observe(getViewLifecycleOwner(), newData -> {
            myAdapter.setBook(newData);
            myAdapter.notifyDataSetChanged();   // Notify the adapter to update the RecyclerView.
        });

        return layout;
    }
}