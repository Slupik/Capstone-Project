/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import io.github.slupik.savepass.R;
import io.github.slupik.savepass.model.utils.Randomizer;

abstract class NotifyBase<V> {

    protected final Context mContext;

    protected NotifyBase(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public abstract void sendNotifies(V... values);

    protected void sendNotifies(Notification summary, Notification... notifies) {
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        for(Notification notify:notifies){
            notificationManager.notify(getNotifyId(), notify);
        }
        notificationManager.notify(getSummaryId(), summary);
    }

    protected NotificationCompat.Builder getNotifyBuilder(V value){
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, Randomizer.randomInteger(10000, 1000000), getIntent(value), 0);

        return new NotificationCompat.Builder(mContext, getChannelId())
                .setSmallIcon(R.drawable.ic_edit)
                .setContentTitle(getTitle(value))
                .setContentText(getContent(value))
                .setGroup(getGroupKey())
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
    }

    protected NotificationCompat.Builder getSummaryBuilder(){
        return new NotificationCompat.Builder(mContext, getChannelId())
                .setSmallIcon(R.drawable.ic_edit)
                .setContentTitle(getSummaryTitle())
                .setContentText(getSummaryContent())
                .setGroup(getGroupKey())
                .setGroupSummary(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getChannelId(), getChannelName(), importance);
            channel.setDescription(getChannelDesc());

            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            if(notificationManager!=null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    protected abstract String getGroupKey();
    protected abstract int getSummaryId();
    protected abstract String getSummaryTitle();
    protected abstract String getSummaryContent();

    protected abstract int getNotifyId();

    protected abstract String getTitle(V value);
    protected abstract String getContent(V value);
    protected abstract Intent getIntent(V value);

    protected abstract String getChannelId();
    protected abstract String getChannelName();
    protected abstract String getChannelDesc();

    protected String getString(@StringRes int id) {
        return mContext.getString(id);
    }
}
