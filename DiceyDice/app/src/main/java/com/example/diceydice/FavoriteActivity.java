package com.example.diceydice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FavoriteActivity extends AppCompatActivity implements FavoriteDiceRollAdapter.FavoriteDiceRollClickHandler {

    private TextView mResultsNameTextView;
    private TextView mResultsTotalTextView;
    private TextView mResultsDescripTextView;
    private RecyclerView mRecyclerView;
    private FavoriteDiceRollAdapter mFavoriteDiceRollAdapter;
    private FloatingActionButton mAddFavoriteFAB;

    //Firebase
    private String mUserName;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setTitle(R.string.favorites_title);

        assignViews();

        initializeFirebase();

        setupRecyclerView();

        mAddFavoriteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, AddFavoriteActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMostRecentDiceResults();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    /** A helper method that assigns all of the views to their initial values in onCreate */
    private void assignViews(){
        mResultsNameTextView = findViewById(R.id.results_name_tv);
        mResultsTotalTextView = findViewById(R.id.results_total_tv);
        mResultsDescripTextView = findViewById(R.id.results_descrip_tv);
        mRecyclerView = findViewById(R.id.favorite_rv);
        mAddFavoriteFAB = findViewById(R.id.add_favorite_fab);
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

    /** Override of FavoriteDiceRoll click method
     *
     * @param favoriteDiceRoll the clicked DiceRoll to be used
     */
    @Override
    public void onItemClick(DiceRoll favoriteDiceRoll) {
        DiceResults diceResults = favoriteDiceRoll.roll(this); // Roll the diceRoll once and save results
        setDataToResultsViews(diceResults);
    }

    /** Helper method to setup RecyclerView, should only be called once in onCreate */
    private void setupRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mFavoriteDiceRollAdapter = new FavoriteDiceRollAdapter(this);
        mRecyclerView.setAdapter(mFavoriteDiceRollAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mFavoriteDiceRollAdapter.setFavoriteDiceRolls(Utils.getDiceRollFakeData()); //TODO This is to be replaced by real data accessed from Firebase
    }

    /** A helper method to load the most recent dice results into the results views.
     * Called in onStart, as it is cheaper than using a listener, since any new rolls occuring while this is the foreground activity will be updated directly.
     */
    private void loadMostRecentDiceResults(){
        DiceResults diceResults = Utils.retrieveLatestDiceResults(this);

        setDataToResultsViews(diceResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.favorite_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_history):
                Intent intent = new Intent(FavoriteActivity.this, HistoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A helper method to handle all of the initial setup for Firebase, to be called in onCreate
     */
    private void initializeFirebase(){
        //Auth setup
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) { //Signed in
                    mUserName = user.getUid();
                } else { //Signed out, finish activity
                    mUserName = null;
                    finish();
                }
            }
        };
    }
}
