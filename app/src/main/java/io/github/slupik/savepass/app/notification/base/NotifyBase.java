/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.notification.base;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;

import io.github.slupik.savepass.R;
import io.github.slupik.savepass.model.utils.Randomizer;

public abstract class NotifyBase<V> {
    protected final Context mContext;
    private final V mValue;

    protected NotifyBase(Context context, V value) {
        this.mContext = context.getApplicationContext();
        mValue = value;
    }

    public Notification getNotification(NotifyChannelBase channel, NotifySummaryBase summary) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, Randomizer.randomInteger(10000, 1000000), getIntent(mValue), 0);

        Notification notify = new NotificationCompat.Builder(mContext, channel.getChannelId())
                .setSmallIcon(R.drawable.ic_edit)
                .setContentTitle(getTitle(mValue))
                .setContentText(getContent(mValue))
                .setGroup(summary.getGroupKey())
                .setAutoCancel(true)
                .setOngoing(false)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notify.defaults |= Notification.DEFAULT_LIGHTS;
        notify.flags |= Notification.FLAG_AUTO_CANCEL;

        return notify;
    }

    public abstract int getId();

    protected abstract Intent getIntent(V value);
    protected abstract String getTitle(V value);
    protected abstract String getContent(V value);

    protected String getString(@StringRes int id) {
        return mContext.getString(id);
    }
}
