/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.notification.base;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;

import io.github.slupik.savepass.R;

public abstract class NotifySummaryBase {
    protected final Context mContext;

    protected NotifySummaryBase(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public abstract int getSummaryId();

    public Notification getSummary(NotifyChannelBase channel) {
        return new NotificationCompat.Builder(mContext, channel.getChannelId())
                .setSmallIcon(R.drawable.ic_edit)
                .setContentTitle(getTitle())
                .setContentText(getContent())
                .setGroup(getGroupKey())
                .setGroupSummary(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setOngoing(false)
                .build();
    }

    protected abstract String getGroupKey();
    protected abstract String getContent();
    protected abstract String getTitle();

    protected String getString(@StringRes int id) {
        return mContext.getString(id);
    }
}
