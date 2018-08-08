/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.notification.base;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import java.util.List;

public abstract class NotifyManagerBase<V, N extends NotifyBase, S extends NotifySummaryBase, C extends NotifyChannelBase> {
    public static final String ARG_NOTIFY_ID = "notify-id";

    protected final Context mContext;

    protected NotifyManagerBase(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public abstract void sendNotifies(List<V> values);

    public abstract void sendNotifies(V... values);

    protected void sendNotifies(C channel, S summary, N... notifies) {
        channel.createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        for(N notify:notifies){
            notificationManager.notify(notify.getId(), notify.getNotification(channel, summary));
        }
        notificationManager.notify(summary.getSummaryId(), summary.getSummary(channel));
    }

    public static void cancelNotify(Context context, int notifyId){
        Context appContext = context.getApplicationContext();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(appContext);
        notificationManager.cancel(notifyId);
        notificationManager.cancel(null, notifyId);
    }
}
