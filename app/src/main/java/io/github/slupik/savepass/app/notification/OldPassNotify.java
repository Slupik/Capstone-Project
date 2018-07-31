/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.notification;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.views.main.MainActivity;
import io.github.slupik.savepass.data.password.room.EntityPassword;
import io.github.slupik.savepass.model.utils.Randomizer;

public class OldPassNotify extends NotifyBase<EntityPassword> {
    private final static String GROUP_KEY = "io.github.slupik.savepass.PASS_CHANGE_REMIND";
    private final static int SUMMARY_ID = 0;
    private final static String CHANNEL_ID = "pass-change-reminder";
    private final static String CHANNEL_NAME = "Password change reminder";
    private final static String CHANNEL_DESC = "Notifies about old passwords which should be changed.";

    public OldPassNotify(Context context) {
        super(context);
    }

    @Override
    public void sendNotifies(@NonNull EntityPassword... values) {
        Notification[] notifications = new Notification[values.length];
        for(int i=0;i<values.length;i++) {
            EntityPassword pass = values[i];
            notifications[i]=getNotifyBuilder(pass).build();
        }
        sendNotifies(getSummaryBuilder().build(), notifications);
    }

    @Override
    protected String getGroupKey() {
        return GROUP_KEY;
    }

    @Override
    protected int getSummaryId() {
        return SUMMARY_ID;
    }

    @Override
    protected String getSummaryTitle() {
        return getString(R.string.notify_reminder_summary_title);
    }

    @Override
    protected String getSummaryContent() {
        return getString(R.string.notify_reminder_summary_content);
    }

    @Override
    protected int getNotifyId() {
        return Randomizer.randomInteger(0, 99999999);
    }

    @Override
    protected String getTitle(EntityPassword password) {
        return getString(R.string.notify_reminder_title);
    }

    @Override
    protected String getContent(EntityPassword password) {
        if(password==null) {
            return mContext.getString(R.string.notify_reminder_content, "error", "error");
        } else {
            return mContext.getString(R.string.notify_reminder_content, password.getPasswordName(), password.getLogin());
        }
    }

    @Override
    protected Intent getIntent(EntityPassword password) {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(MainActivity.ARG_DATA, new Gson().toJson(password));
        intent.putExtra(MainActivity.ARG_SOURCE, MainActivity.ARG_VALUE_SOURCE_NOTIFY);
        return intent;
    }

    @Override
    protected String getChannelId() {
        return CHANNEL_ID;
    }

    @Override
    protected String getChannelName() {
        return CHANNEL_NAME;
    }

    @Override
    public String getChannelDesc() {
        return CHANNEL_DESC;
    }
}
