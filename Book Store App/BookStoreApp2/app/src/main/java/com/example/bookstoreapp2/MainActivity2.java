package com.example.bookstoreapp2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        // Create a new instance of BookFragment fragment and set it to the frameLayout2 layout for the second fragment.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout2, new BookFragment())
                .addToBackStack("fragment2")
                .commit();
    }
}