/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.background;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import io.github.slupik.savepass.app.background.reminder.RemindService;
import io.github.slupik.savepass.app.background.syncer.SyncService;
import io.github.slupik.savepass.data.settings.ReminderSettings;
import io.github.slupik.savepass.data.settings.ServerSettings;

public final class DispatcherController {
    private static boolean sInitialized;

    synchronized public static void startJobs(@NonNull Context context){
        if (sInitialized) return;
        sInitialized = true;
        Context appContext = context.getApplicationContext();
        switchReminder(appContext, false);
        switchSyncer(appContext, false);
    }

    public static void switchReminder(Context context, boolean forceChanges){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        if(new ReminderSettings(context).isRemindingEnabled()) {
            Job myJob = dispatcher.newJobBuilder()
                    .setService(RemindService.class)
                    .setTag(RemindService.TASK_TAG)
                    .setRecurring(true)
                    .setLifetime(Lifetime.FOREVER)
                    .setTrigger(Trigger.executionWindow(
                            RemindService.getInterval(),
                            RemindService.getInterval() + RemindService.getFlextime()))
                    .setReplaceCurrent(false)
                    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                    .setConstraints()
                    .build();
            if(forceChanges) {
                dispatcher.cancel(RemindService.TASK_TAG);
                dispatcher.mustSchedule(myJob);
            } else {
                dispatcher.schedule(myJob);
            }
        } else {
            dispatcher.cancel(RemindService.TASK_TAG);
        }
    }

    public static void switchSyncer(Context context, boolean forceChanges){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        if(new ServerSettings(context).isSyncingEnabled()) {
            Job myJob = dispatcher.newJobBuilder()
                    .setService(SyncService.class)
                    .setTag(SyncService.TASK_TAG)
                    .setRecurring(true)
                    .setLifetime(Lifetime.FOREVER)
                    .setTrigger(Trigger.executionWindow(
                            SyncService.getInterval(context),
                            SyncService.getInterval(context) + SyncService.getFlextime(context)))
                    .setReplaceCurrent(false)
                    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                    .setConstraints(
                            Constraint.ON_ANY_NETWORK
                    )
                    .build();
            if(forceChanges) {
                dispatcher.cancel(SyncService.TASK_TAG);
                dispatcher.mustSchedule(myJob);
            } else {
                dispatcher.schedule(myJob);
            }
        } else {
            dispatcher.cancel(SyncService.TASK_TAG);
        }
    }
}
