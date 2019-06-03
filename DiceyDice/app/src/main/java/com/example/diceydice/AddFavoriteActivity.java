package com.example.diceydice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddFavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);

        setTitle(R.string.add_favorite_activity_title);
    }
}
