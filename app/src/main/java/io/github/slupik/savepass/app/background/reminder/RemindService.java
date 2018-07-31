/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.background.reminder;

import android.content.Context;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.slupik.savepass.app.background.DefaultJobService;
import io.github.slupik.savepass.app.notification.OldPassNotify;
import io.github.slupik.savepass.data.password.PasswordRepository;
import io.github.slupik.savepass.data.password.room.EntityPassword;

public class RemindService extends DefaultJobService {
    public static final String TASK_TAG = "remind-service";

    @Override
    protected void runAsyncTask(Context context) {
        OldPassNotify notifier = new OldPassNotify(context);
        List<EntityPassword> list = PasswordRepository.getInstance(getApplication()).getPasswordsToRemind();
        for(EntityPassword pass:list) {
            notifier.sendNotifies(pass);
        }
    }

    public static int getInterval() {
        return (int) TimeUnit.DAYS.toSeconds(1);
    }

    public static int getFlextime() {
        return (int) TimeUnit.HOURS.toSeconds(20);
    }
}
