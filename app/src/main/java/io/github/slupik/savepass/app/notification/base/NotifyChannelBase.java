/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.notification.base;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public abstract class NotifyChannelBase {

    private final Context mContext;

    protected NotifyChannelBase(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void createNotificationChannel() {
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
    protected abstract String getChannelDesc();
    protected abstract CharSequence getChannelName();
    protected abstract String getChannelId();
}
