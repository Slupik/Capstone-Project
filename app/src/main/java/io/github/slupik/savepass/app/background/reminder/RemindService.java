/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.background.reminder;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import io.github.slupik.savepass.app.background.DefaultJobService;

public class RemindService extends DefaultJobService {
    public static final String TASK_TAG = "remind-service";

    @Override
    protected void runAsyncTask(Context context) {

    }

    public static int getInterval() {
        return (int) TimeUnit.DAYS.toSeconds(1);
    }

    public static int getFlextime() {
        return (int) TimeUnit.HOURS.toSeconds(20);
    }
}
