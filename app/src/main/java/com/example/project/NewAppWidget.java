package com.example.project;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.example.project.Emotion.EmotionUtils;


public class NewAppWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, Bitmap piechart) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        // Обновление ImageView в виджете с помощью Bitmap
        views.setImageViewBitmap(R.id.imageView, piechart);

        // Инструкция менеджеру виджетов обновить виджет
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        EmotionUtils.createBarChartBitmap(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}