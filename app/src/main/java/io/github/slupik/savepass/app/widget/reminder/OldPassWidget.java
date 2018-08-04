/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.widget.reminder;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import java.util.List;

import io.github.slupik.savepass.R;
import io.github.slupik.savepass.data.password.PasswordRepository;
import io.github.slupik.savepass.data.password.room.EntityPassword;

import static io.github.slupik.savepass.app.widget.reminder.WidgetOldPassService.ARG_PASS_LIST;

/**
 * Implementation of App Widget functionality.
 */
public class OldPassWidget extends AppWidgetProvider {

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        new AsyncTask<Void, Void, List<EntityPassword>>() {
            @Override
            protected List<EntityPassword> doInBackground(Void... voids) {
                //TODO change to getPasswordsToRemind()
                return PasswordRepository.getInstance(context).getPasswords();
            }

            @Override
            protected void onPostExecute(List<EntityPassword> passwords) {
                super.onPostExecute(passwords);
                Log.d("UPDATEWIDGET", "passwords: "+passwords.size());
                Log.d("UPDATEWIDGET", "appWidgetId: "+appWidgetId);

                String passwordsInString = new Gson().toJson(passwords);
                Intent intent = new Intent(context, WidgetOldPassService.class);
                intent.putExtra(ARG_PASS_LIST, passwordsInString);

                // Construct the RemoteViews object
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.old_pass_widget);
                views.setRemoteAdapter(R.id.widget_recipe_list, intent);

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }.execute();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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