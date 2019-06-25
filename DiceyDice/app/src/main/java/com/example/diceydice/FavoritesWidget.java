package com.example.diceydice;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class FavoritesWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetIds, List<DiceRoll> diceRolls) {
        //Iterate through all widgets and update each
        for (int appWidgetId : appWidgetIds) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorites_widget);

            //TODO Add update logic

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //This diceRolls is blank, because the onUpdate method should never be called.  Rather, updates occur from the above updateAppWidget method being called by changes in the app.
        List<DiceRoll> diceRolls = new ArrayList<>();
        updateAppWidget(context, appWidgetManager, appWidgetIds, diceRolls);

    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }
}

