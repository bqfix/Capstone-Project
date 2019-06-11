package com.example.diceydice;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;

public class DKeyboard extends ConstraintLayout implements View.OnClickListener {

    private Button mOneButton, mTwoButton, mThreeButton, mFourButton,
            mFiveButton, mSixButton, mSevenButton, mEightButton,
            mNineButton, mZeroButton, mDButton, mPlusButton,
            mMinusButton, mDeleteButton, mEnterButton;

    private SparseArray<String> keyValues = new SparseArray<>();
    private InputConnection mInputConnection;

    public DKeyboard(Context context) {
        super(context);
        init(context);
    }

    public DKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.d_keyboard, this, true);

        //Assign view
        mOneButton = (Button) findViewById(R.id.one_button);
        mTwoButton = (Button) findViewById(R.id.two_button);
        mThreeButton = (Button) findViewById(R.id.three_button);
        mFourButton = (Button) findViewById(R.id.four_button);
        mFiveButton = (Button) findViewById(R.id.five_button);
        mSixButton = (Button) findViewById(R.id.six_button);
        mSevenButton = (Button) findViewById(R.id.seven_button);
        mEightButton = (Button) findViewById(R.id.eight_button);
        mNineButton = (Button) findViewById(R.id.nine_button);
        mZeroButton = (Button) findViewById(R.id.zero_button);
        mPlusButton  = (Button) findViewById(R.id.plus_button);
        mMinusButton  = (Button) findViewById(R.id.minus_button);
        mDButton = (Button) findViewById(R.id.d_button);
        mDeleteButton  = (Button) findViewById(R.id.delete_button);
        mEnterButton  = (Button) findViewById(R.id.enter_button);

        //Assign clicklisteners
        mOneButton.setOnClickListener(this);
        mTwoButton.setOnClickListener(this);
        mThreeButton.setOnClickListener(this);
        mFourButton.setOnClickListener(this);
        mFiveButton.setOnClickListener(this);
        mSixButton.setOnClickListener(this);
        mSevenButton.setOnClickListener(this);
        mEightButton.setOnClickListener(this);
        mNineButton.setOnClickListener(this);
        mZeroButton.setOnClickListener(this);
        mPlusButton.setOnClickListener(this);
        mMinusButton.setOnClickListener(this);
        mDButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
        mEnterButton.setOnClickListener(this);

        //Populate SparseArray with key value pairs
        keyValues.put(R.id.one_button, "1");
        keyValues.put(R.id.two_button, "2");
        keyValues.put(R.id.three_button, "3");
        keyValues.put(R.id.four_button, "4");
        keyValues.put(R.id.five_button, "5");
        keyValues.put(R.id.six_button, "6");
        keyValues.put(R.id.seven_button, "7");
        keyValues.put(R.id.eight_button, "8");
        keyValues.put(R.id.nine_button, "9");
        keyValues.put(R.id.zero_button, "0");
        keyValues.put(R.id.plus_button, "+");
        keyValues.put(R.id.minus_button, "-");
        keyValues.put(R.id.d_button, "d");
        keyValues.put(R.id.enter_button, "\n");
    }

    @Override
    public void onClick(View v) {
        if (mInputConnection == null) return;

        int viewId = v.getId();
        if(viewId == R.id.delete_button) { //Special delete logic
            CharSequence selectedText = mInputConnection.getSelectedText(0);

            if (TextUtils.isEmpty(selectedText)){ //If not highlighted, delete 1 before, 0 after
                mInputConnection.deleteSurroundingText(1,0);
            } else { //Simply replace highlighted text with empty string
                mInputConnection.commitText("", 1);
            }
        } else { //Other buttons simply append the value of the button, accessed from keyValues
            String value = keyValues.get(viewId);
            mInputConnection.commitText(value, 1);
        }
    }

    public void setInputConnection(InputConnection inputConnection){
        mInputConnection = inputConnection;
    }
}
