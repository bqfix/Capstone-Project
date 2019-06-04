package com.example.diceydice;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class FavoriteActivity extends AppCompatActivity implements FavoriteDiceRollAdapter.FavoriteDiceRollClickHandler {

    private TextView mResultsNameTextView;
    private TextView mResultsTotalTextView;
    private TextView mResultsDescripTextView;
    private RecyclerView mRecyclerView;
    private FavoriteDiceRollAdapter mFavoriteDiceRollAdapter;
    private FloatingActionButton mAddFavoriteFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setTitle(R.string.favorites_title);

        assignViews();

        setupRecyclerView();

        mAddFavoriteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, AddFavoriteActivity.class);
                startActivity(intent);
            }
        });

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
        DiceResults diceResults = favoriteDiceRoll.roll(); // Roll the diceRoll once and save results
        setDataToResultsViews(diceResults);
    }

    /** Helper method to setup RecyclerView, should only be called once in onCreate */
    private void setupRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mFavoriteDiceRollAdapter = new FavoriteDiceRollAdapter(this);
        mRecyclerView.setAdapter(mFavoriteDiceRollAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mFavoriteDiceRollAdapter.setFavoriteDiceRolls(Utils.getFakeData()); //TODO This is to be replaced by real data accessed from Firebase
    }
}
