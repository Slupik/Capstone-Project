/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.widget.reminder;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import io.github.slupik.savepass.R;
import io.github.slupik.savepass.data.password.PasswordRepository;
import io.github.slupik.savepass.data.password.room.EntityPassword;

/**
 * Implementation of App Widget functionality.
 */
public class OldPassWidgetProvider extends AppWidgetProvider {
    public static List<EntityPassword> mPasswordList = new ArrayList<>();

    public static void updateOldPassWidgets(final Context context){
        new AsyncTask<Void, Void, List<EntityPassword>>() {
            @Override
            protected List<EntityPassword> doInBackground(Void... voids) {
                return PasswordRepository.getInstance(context).getPasswords();
            }

            @Override
            protected void onPostExecute(List<EntityPassword> passwords) {
                super.onPostExecute(passwords);
                final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                final int[] appWidgetIds = appWidgetManager.getAppWidgetIds
                        (new ComponentName(context, OldPassWidgetProvider.class));

                OldPassWidgetProvider.updateOldPassWidgets(context, appWidgetManager, appWidgetIds,
                        passwords);
            }
        }.execute();
    }

    public static void updateOldPassWidgets(Context context, AppWidgetManager appWidgetManager,
                                                int[] appWidgetIds, List<EntityPassword> reminderList) {
        mPasswordList = reminderList;

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.old_pass_widget);

            Intent intent = new Intent(context, WidgetOldPassService.class);
            views.setRemoteAdapter(R.id.widget_recipe_list, intent);
            ComponentName component = new ComponentName(context, OldPassWidgetProvider.class);

            //Trigger data update to handle the ListView widgets and force a data refresh
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_recipe_list);

            appWidgetManager.updateAppWidget(component, views);
        }
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
            updateOldPassWidgets(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}