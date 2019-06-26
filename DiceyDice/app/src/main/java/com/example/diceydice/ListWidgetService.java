package com.example.diceydice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Context applicationContext = this.getApplicationContext();
        if (intent.hasExtra(getString(R.string.widget_dice_roll_parcelable_key))){
            String stringDiceRolls = intent.getStringExtra(getString(R.string.widget_dice_roll_parcelable_key));
            return new ListRemoteViewsFactory(applicationContext, stringDiceRolls);
        }
        return new ListRemoteViewsFactory(applicationContext, "");
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<DiceRoll> mDiceRolls;

    //Constructor for DiceRolls List
    public ListRemoteViewsFactory(Context applicationContext, String stringDiceRolls) {
        mContext = applicationContext;
        mDiceRolls = Utils.stringToDiceRolls(stringDiceRolls);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mDiceRolls == null) return 0;
        return mDiceRolls.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_favorite_list_item);
        DiceRoll diceRoll = mDiceRolls.get(position);
        views.setTextViewText(R.id.widget_item_name_tv, diceRoll.getName());
        views.setTextViewText(R.id.widget_item_formula_tv, diceRoll.getFormula());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}