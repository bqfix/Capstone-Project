package com.example.diceydice;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddFavoriteActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mFormulaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);

        setTitle(R.string.add_favorite_activity_title);

        assignViews();

        setListeners();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.add_favorite_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_done) :
                saveNewFavorite();
                return true;
            default : return super.onOptionsItemSelected(item);
        }
    }

    /** A helper method for saving a new favorite */
    private void saveNewFavorite(){
        //Check that formula is valid
        String formula = mFormulaEditText.getText().toString();
        Pair<Boolean, String> validAndErrorPair = Utils.isValidDiceRoll(this, formula);
        if (validAndErrorPair.first){ //If formula is okay, make a new nameless DiceRoll for display in the results text
            String name = mNameEditText.getText().toString(); //Additionally get name
            DiceRoll diceRoll = new DiceRoll(name, formula);
            //TODO Actually save this to Firebase
            finish();
        }
        else {
            Toast.makeText(this, validAndErrorPair.second, Toast.LENGTH_SHORT).show();
        }

    }

    /** A helper method that assigns all of the views to their initial values in onCreate */
    private void assignViews(){
        mNameEditText = findViewById(R.id.name_input_et);
        mFormulaEditText = findViewById(R.id.formula_input_et);
    }

    /** A helper method to hide the soft keyboard
     *
     * @param view used to get the window token (passed in from listener)
     */
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /** Helper method to set listeners to various views, should only be called once in onCreate */
    private void setListeners() {
        mNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { //FocusChange listener to minimize keyboard when clicking outside of EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        mFormulaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { //FocusChange listener to minimize keyboard when clicking outside of EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(v);
                }
            }
        });
    }
}
