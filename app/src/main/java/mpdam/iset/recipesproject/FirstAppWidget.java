package mpdam.iset.recipesproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class FirstAppWidget extends AppWidgetProvider {



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Perform operations when the App Widget is updated
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Get the layout for the App Widget
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.first_app_widget);

        // Set up any interactions or data updates here
        Intent launchIntent = new Intent(context, MainActivity.class);
        launchIntent.setAction("android.intent.action.MAIN");
        launchIntent.addCategory("android.intent.category.LAUNCHER");

        // Set an OnClickListener for the button
        views.setOnClickPendingIntent(R.id.appWidgetImg, PendingIntent.getActivity(context, 0, launchIntent, 0));

        // Update the App Widget with the remote views
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}