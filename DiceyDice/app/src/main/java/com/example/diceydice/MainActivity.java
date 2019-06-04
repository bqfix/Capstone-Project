package com.example.diceydice;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();

        setListeners();

        setupFavoriteRecyclerView();

    }

    /** A helper method that assigns all of the views to their initial values in onCreate */
    private void assignViews(){
        mCommandInputEditText = findViewById(R.id.command_input_et);
        mRollButton = findViewById(R.id.roll_button);
        mResultsNameTextView = findViewById(R.id.results_name_tv);
        mResultsTotalTextView = findViewById(R.id.results_total_tv);
        mResultsDescripTextView = findViewById(R.id.results_descrip_tv);
        mFavoriteRecyclerView = findViewById(R.id.main_favorite_rv);
        mAllFavoritesButton = findViewById(R.id.favorites_button);
    }

    /** A helper method to populate the results views with data
     *
     * @param diceResults to populate the views from
     */
    private void setDataToResultsViews(DiceResults diceResults) {
        mResultsNameTextView.setText(diceResults.getName());
        mResultsTotalTextView.setText(String.valueOf(diceResults.getTotal()));
        mResultsDescripTextView.setText(diceResults.getDescrip());
    }

    /** A helper method to hide the soft keyboard
     *
     * @param view used to get the window token (passed in from listener)
     */
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /** Override of FavoriteDiceRoll click method
     *
     * @param favoriteDiceRoll the clicked DiceRoll to be used
     */
    @Override
    public void onItemClick(DiceRoll favoriteDiceRoll) {
        DiceResults diceResults = favoriteDiceRoll.roll();
        setDataToResultsViews(diceResults);
    }

    /** Helper method to setup FavoriteRecyclerView, should only be called once in onCreate */
    private void setupFavoriteRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mFavoriteRecyclerView.setLayoutManager(layoutManager);

        mFavoriteDiceRollAdapter = new FavoriteDiceRollAdapter(this);
        mFavoriteRecyclerView.setAdapter(mFavoriteDiceRollAdapter);

        mFavoriteRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mFavoriteDiceRollAdapter.setFavoriteDiceRolls(Utils.getFakeData()); //TODO This is to be replaced by real data accessed from Firebase
    }

    /** Helper method to set listeners to various views, should only be called once in onCreate */
    private void setListeners(){
        mAllFavoritesButton.setOnClickListener(new View.OnClickListener() { //Click listener to launch FavoritesActivity
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });

        mCommandInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { //FocusChange listener to minimize keyboard when clicking outside of EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(v);
                }
            }
        });

        mRollButton.setOnClickListener(new View.OnClickListener() { //Click listener to check the validity of a roll, and execute it if it is acceptable
            @Override
            public void onClick(View v) {
                String formula = mCommandInputEditText.getText().toString();
                Pair<Boolean, String> validAndErrorPair = Utils.isValidDiceRoll(MainActivity.this, formula); //Get a boolean of whether the
                if (validAndErrorPair.first){ //If formula is okay, make a new nameless DiceRoll for display in the results text
                    DiceRoll diceRoll = new DiceRoll(formula);
                    DiceResults diceResults = diceRoll.roll();
                    setDataToResultsViews(diceResults);
                    hideKeyboard(v);
                }
                else {
                    Toast.makeText(MainActivity.this, validAndErrorPair.second, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
