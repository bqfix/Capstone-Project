package com.example.diceydice;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class DiceResults {
    private String mName;
    private String mDescrip;
    private int mTotal;
    private long mDateCreated;

    public DiceResults(String name, String descrip, int total) {
        mName = name;
        mDescrip = descrip;
        mTotal = total;
        mDateCreated = System.currentTimeMillis();
    }

    public DiceResults(String name, String descrip, int total, long date){
        mName = name;
        mDescrip = descrip;
        mTotal = total;
        mDateCreated = date;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescrip() {
        return mDescrip;
    }

    public void setDescrip(String descrip) {
        mDescrip = descrip;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public long getDateCreated(){
        return mDateCreated;
    }

    /** A helper method used to save the Results to both SharedPreferences (as the latest roll) and History
     *
     * @param context used to save the results
     */
    public void save(Context context){ //TODO Additionally add to history
        //Logic to add to SharedPrefs
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(context.getString(R.string.dice_results_name_key), mName);
        editor.putString(context.getString(R.string.dice_results_descrip_key), mDescrip);
        editor.putInt(context.getString(R.string.dice_results_total_key), mTotal);
        editor.putLong(context.getString(R.string.dice_results_date_key), mDateCreated);
        editor.apply();
    }
}
