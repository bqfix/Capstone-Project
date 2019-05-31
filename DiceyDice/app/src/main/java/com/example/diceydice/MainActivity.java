package com.example.diceydice;

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mCommandInputEditText;
    private Button mRollButton;
    private TextView mResultsNameTextView;
    private TextView mResultsTotalTextView;
    private TextView mResultsDescripTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();

        mRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formula = mCommandInputEditText.getText().toString();
                if (Utils.isValidDiceRoll(formula)){ //If formula is okay, make a new nameless DiceRoll for display in the results text
                    DiceRoll diceRoll = new DiceRoll(formula);
                    setDataToResultsViews(diceRoll);
                }
                else {
                    Toast.makeText(MainActivity.this, R.string.command_not_valid_error_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /** A helper method that assigns all of the views to their initial values in onCreate */
    private void assignViews(){
        mCommandInputEditText = findViewById(R.id.command_input_et);
        mRollButton = findViewById(R.id.roll_button);
        mResultsNameTextView = findViewById(R.id.results_name_tv);
        mResultsTotalTextView = findViewById(R.id.results_total_tv);
        mResultsDescripTextView = findViewById(R.id.results_descrip_tv);
    }

    /** A helper method to populate the results views with data
     *
     * @param diceRoll to populate the views from
     */
    private void setDataToResultsViews(DiceRoll diceRoll) {
        Pair<String, Integer> randomRoll = diceRoll.roll(); // Roll the diceRoll once and save results
        String descrip = diceRoll.getFormula() + " =\n\n" + randomRoll.first;

        mResultsNameTextView.setText(diceRoll.getName());
        mResultsTotalTextView.setText(randomRoll.second.toString());
        mResultsDescripTextView.setText(descrip);
    }
}
