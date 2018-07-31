/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app;

import android.app.Application;

import io.github.slupik.savepass.app.background.DispatcherController;

public class MyApplication extends Application {
    private String appPassword = "";

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DispatcherController.startJobs(getApplicationContext());
    }
}
