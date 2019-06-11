package com.example.diceydice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FavoriteDiceRollAdapter.FavoriteDiceRollClickHandler {

    private EditText mCommandInputEditText;
    private Button mRollButton;
    private TextView mResultsNameTextView;
    private TextView mResultsTotalTextView;
    private TextView mResultsDescripTextView;
    private RecyclerView mFavoriteRecyclerView;
    private FavoriteDiceRollAdapter mFavoriteDiceRollAdapter;
    private Button mAllFavoritesButton;
    private ImageButton mHelpButton;
    private DKeyboard mDKeyboard;
    private InputConnection mCommandInputConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();

        setOnClickListeners();

        setupFavoriteRecyclerView();

        setupEditTextAndKeyboard();

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMostRecentDiceResults();
    }

    /**
     * A helper method that assigns all of the views to their initial values in onCreate
     */
    private void assignViews() {
        mCommandInputEditText = findViewById(R.id.command_input_et);
        mRollButton = findViewById(R.id.roll_button);
        mResultsNameTextView = findViewById(R.id.results_name_tv);
        mResultsTotalTextView = findViewById(R.id.results_total_tv);
        mResultsDescripTextView = findViewById(R.id.results_descrip_tv);
        mFavoriteRecyclerView = findViewById(R.id.main_favorite_rv);
        mAllFavoritesButton = findViewById(R.id.favorites_button);
        mHelpButton = findViewById(R.id.main_help_button);
        mDKeyboard = (DKeyboard) findViewById(R.id.d_keyboard);
    }

    /**
     * A helper method to populate the results views with data
     *
     * @param diceResults to populate the views from
     */
    private void setDataToResultsViews(DiceResults diceResults) {
        mResultsNameTextView.setText(diceResults.getName());
        mResultsTotalTextView.setText(String.valueOf(diceResults.getTotal()));
        mResultsDescripTextView.setText(diceResults.getDescrip());
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

    /** A helper method to show the custom keyboard */
    private void showCustomKeyboard() { //TODO Add animation to make appear/disappear less jarring
        mDKeyboard.setVisibility(View.VISIBLE);
    }

    /** A helper method to hide the custom keyboard */
    private void hideCustomKeyboard() {
        mDKeyboard.setVisibility(View.GONE);
    }

    /**
     * Override of FavoriteDiceRoll click method
     *
     * @param favoriteDiceRoll the clicked DiceRoll to be used
     */
    @Override
    public void onItemClick(DiceRoll favoriteDiceRoll) {
        DiceResults diceResults = favoriteDiceRoll.roll(this);
        setDataToResultsViews(diceResults);
    }

    /**
     * Helper method to setup FavoriteRecyclerView, should only be called once in onCreate
     */
    private void setupFavoriteRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mFavoriteRecyclerView.setLayoutManager(layoutManager);

        mFavoriteDiceRollAdapter = new FavoriteDiceRollAdapter(this);
        mFavoriteRecyclerView.setAdapter(mFavoriteDiceRollAdapter);

        mFavoriteRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mFavoriteDiceRollAdapter.setFavoriteDiceRolls(Utils.getDiceRollFakeData()); //TODO This is to be replaced by real data accessed from Firebase
    }

    /**
     * Helper method to set click listeners to various views, should only be called once in onCreate
     */
    private void setOnClickListeners() {
        mAllFavoritesButton.setOnClickListener(new View.OnClickListener() { //Click listener to launch FavoritesActivity
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });

        mRollButton.setOnClickListener(new View.OnClickListener() { //Click listener to check the validity of a roll, and execute it if it is acceptable
            @Override
            public void onClick(View v) {
                String formula = mCommandInputEditText.getText().toString();
                Pair<Boolean, String> validAndErrorPair = Utils.isValidDiceRoll(MainActivity.this, formula); //Get a boolean of whether the
                if (validAndErrorPair.first) { //If formula is okay, make a new nameless DiceRoll for display in the results text
                    DiceRoll diceRoll = new DiceRoll(formula);
                    DiceResults diceResults = diceRoll.roll(MainActivity.this);
                    setDataToResultsViews(diceResults);
                    hideCustomKeyboard();
                    hideSystemKeyboard(v);
                } else {
                    Toast.makeText(MainActivity.this, validAndErrorPair.second, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //No special action needed, simply dismisses the dialog.
                    }
                })
                        .setMessage(R.string.help_formula_advice)
                        .setTitle(R.string.help_header);
                builder.show();
            }
        });
    }

    /**
     * A helper method to load the most recent dice results into the results views.
     * Called in onStart, as it is cheaper than using a listener, since any new rolls occuring while this is the foreground activity will be updated directly.
     */
    private void loadMostRecentDiceResults() {
        DiceResults diceResults = Utils.retrieveLatestDiceResults(this);

        setDataToResultsViews(diceResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_history):
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupEditTextAndKeyboard(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) { //Use custom keyboard if the Android version is over 21 (this is when showSoftInputOnFocus was implemented)
            mCommandInputEditText.setShowSoftInputOnFocus(false);

            mCommandInputConnection = mCommandInputEditText.onCreateInputConnection(new EditorInfo());
            mDKeyboard.setInputConnection(mCommandInputConnection);

            mCommandInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { //FocusChange listener to show custom keyboard, and minimize keyboard when clicking outside of EditText
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        showCustomKeyboard();
                    } else {
                        hideCustomKeyboard();
                    }
                }
            });
        } else { //Use basic system keyboard
            mCommandInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { //FocusChange listener to minimize keyboard when clicking outside of EditText
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
