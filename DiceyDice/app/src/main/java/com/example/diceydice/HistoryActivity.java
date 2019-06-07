package com.example.diceydice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity implements HistoryResultsAdapter.HistoryResultsClickHandler {

    private TextView mResultsNameTextView;
    private TextView mResultsTotalTextView;
    private TextView mResultsDescripTextView;
    private RecyclerView mHistoryRecyclerView;
    private HistoryResultsAdapter mHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle(R.string.history_activity_title);

        assignViews();

        setupHistoryRecyclerView();

    }

    @Override
    public void onItemClick(DiceResults historyResults) {
        setDataToResultsViews(historyResults);
    }

    /** A helper method that assigns all of the views to their initial values in onCreate */
    private void assignViews(){
        mResultsNameTextView = findViewById(R.id.results_name_tv);
        mResultsTotalTextView = findViewById(R.id.results_total_tv);
        mResultsDescripTextView = findViewById(R.id.results_descrip_tv);
        mHistoryRecyclerView = findViewById(R.id.history_rv);
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

    /** Helper method to setup HistoryRecyclerView, should only be called once in onCreate */
    private void setupHistoryRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mHistoryRecyclerView.setLayoutManager(layoutManager);

        mHistoryAdapter = new HistoryResultsAdapter(this);
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);

        mHistoryRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mHistoryAdapter.setHistoryResults(Utils.getDiceResultsFakeData()); //TODO This is to be replaced by real data accessed from Firebase
    }
}
