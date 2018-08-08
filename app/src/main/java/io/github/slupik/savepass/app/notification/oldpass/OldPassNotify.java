/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.notification.oldpass;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.notification.base.NotifyBase;
import io.github.slupik.savepass.app.notification.base.NotifyManagerBase;
import io.github.slupik.savepass.app.views.main.MainActivity;
import io.github.slupik.savepass.data.password.room.EntityPassword;
import io.github.slupik.savepass.model.utils.Randomizer;

public class OldPassNotify extends NotifyBase<EntityPassword> {
    private int id = Randomizer.randomInteger(10, 10000);

    protected OldPassNotify(Context context, EntityPassword value) {
        super(context, value);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    protected Intent getIntent(EntityPassword value) {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(MainActivity.ARG_DATA, new Gson().toJson(value));
        intent.putExtra(MainActivity.ARG_SOURCE, MainActivity.ARG_VALUE_SOURCE_NOTIFY);
        intent.putExtra(NotifyManagerBase.ARG_NOTIFY_ID, getId());
        return intent;
    }

    @Override
    protected String getTitle(EntityPassword value) {
        return getString(R.string.notify_reminder_title);
    }

    @Override
    protected String getContent(EntityPassword value) {
        if(null == value) {
            return mContext.getString(R.string.notify_reminder_content, "error", "error");
        } else {
            return mContext.getString(R.string.notify_reminder_content, value.getPasswordName(), value.getLogin());
        }
    }
}
