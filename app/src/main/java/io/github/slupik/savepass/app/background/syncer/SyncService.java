/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.background.syncer;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import io.github.slupik.savepass.app.background.DefaultJobService;
import io.github.slupik.savepass.model.server.backup.OnlineBackup;
import io.github.slupik.savepass.data.settings.ServerSettings;

public class SyncService extends DefaultJobService {
    public static final String TASK_TAG = "sync-service";

    @Override
    protected void runAsyncTask(Context context) {
        OnlineBackup.sendData(context);
        OnlineBackup.saveData(context);
    }

    public static int getInterval(Context context) {
        return (int) TimeUnit.DAYS.toSeconds(getSettingsTime(context));
    }

    public static int getFlextime(Context context) {
        return (int) TimeUnit.HOURS.toSeconds(getSettingsTime(context)/5*4);
    }

    private static int getSettingsTime(Context context) {
        return new ServerSettings(context).getInterval();
    }
}
