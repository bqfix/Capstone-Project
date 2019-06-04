package com.example.diceydice;

public class DiceResults {
    private String mName;
    private String mDescrip;
    private int mTotal;

    public DiceResults(String name, String descrip, int total) {
        mName = name;
        mDescrip = descrip;
        mTotal = total;
    }

    public DiceResults(String descrip, int total) {
        mName = "";
        mDescrip = descrip;
        mTotal = total;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescrip() {
        return mDescrip;
    }

    public void setmDescrip(String mDescrip) {
        this.mDescrip = mDescrip;
    }

    public int getmTotal() {
        return mTotal;
    }

    public void setmTotal(int mTotal) {
        this.mTotal = mTotal;
    }
}
