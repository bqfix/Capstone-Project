package com.example.diceydice;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddFavoriteActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mFormulaEditText;
    private InputConnection mInputConnection;
    private DKeyboard mDKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);

        setTitle(R.string.add_favorite_activity_title);

        assignViews();

        setListeners();
        setupFormulaEditTextAndKeyboard();
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
            case (R.id.action_done):
                saveNewFavorite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A helper method for saving a new favorite
     */
    private void saveNewFavorite() {
        //Check that formula is valid
        String formula = mFormulaEditText.getText().toString();
        Pair<Boolean, String> validAndErrorPair = Utils.isValidDiceRoll(this, formula);
        if (validAndErrorPair.first) { //If formula is okay, make a new nameless DiceRoll for display in the results text
            String name = mNameEditText.getText().toString(); //Additionally get name
            DiceRoll diceRoll = new DiceRoll(name, formula);
            //TODO Actually save this to Firebase
            finish();
        } else {
            Toast.makeText(this, validAndErrorPair.second, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * A helper method that assigns all of the views to their initial values in onCreate
     */
    private void assignViews() {
        mNameEditText = findViewById(R.id.name_input_et);
        mFormulaEditText = findViewById(R.id.formula_input_et);
        mDKeyboard = (DKeyboard) findViewById(R.id.d_keyboard);
    }

    /**
     * A helper method to hide the system keyboard
     *
     * @param view used to get the window token (passed in from listener)
     */
    private void hideSystemKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * A helper method to show the custom keyboard
     */
    private void showCustomKeyboard() { //TODO Add animation to make appear/disappear less jarring
        mDKeyboard.setVisibility(View.VISIBLE);
    }

    /**
     * A helper method to hide the custom keyboard
     */
    private void hideCustomKeyboard() {
        mDKeyboard.setVisibility(View.GONE);
    }

    /**
     * Helper method to set listeners to various views, should only be called once in onCreate
     */
    private void setListeners() {
        mNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { //FocusChange listener to minimize keyboard when clicking outside of EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideSystemKeyboard(v);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDKeyboard.getVisibility() == View.VISIBLE) { //Override to hide custom keyboard if visible when back pressed
            hideCustomKeyboard();
            return;
        }
        super.onBackPressed();
    }

    /**
     * A helper method to setup the Formula EditText and Keyboard, to be called once in onCreate
     */
    private void setupFormulaEditTextAndKeyboard() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) { //Use custom keyboard if the Android version is over 21 (this is when showSoftInputOnFocus was implemented)
            mFormulaEditText.setShowSoftInputOnFocus(false);

            mInputConnection = mFormulaEditText.onCreateInputConnection(new EditorInfo());
            mDKeyboard.setInputConnection(mInputConnection);

            mFormulaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { //FocusChange listener to show custom keyboard, and minimize keyboard when clicking outside of EditText
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        showCustomKeyboard();
                    } else {
                        hideCustomKeyboard();
                    }
                }
            });

            mFormulaEditText.setOnClickListener(new View.OnClickListener() { //Necessary to make the custom keyboard visible if focus has not been lost, but the keyboard is minimized
                @Override
                public void onClick(View v) {
                    if (mDKeyboard.getVisibility() != View.VISIBLE) {
                        showCustomKeyboard();
                    }
                }
            });
        } else { //Use basic system keyboard
            mFormulaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { //FocusChange listener to minimize keyboard when clicking outside of EditText
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideSystemKeyboard(v);
                    }
                }
            });
        }
    }
}
