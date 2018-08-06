/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.background.DispatcherController;

public class MyApplication extends Application {
    private String appPassword = "";
    private static GoogleAnalytics analytics;
    private static Tracker tracker;

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
        setupGoogleAnal();
    }

    private void setupGoogleAnal() {
        analytics = GoogleAnalytics.getInstance(this);
        tracker = analytics.newTracker(R.xml.global_tracker);

        tracker.enableExceptionReporting(true);
        // https://developers.google.com/analytics/devguides/collection/android/display-features
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        tracker.send(new HitBuilders.ScreenViewBuilder().setCustomDimension(1, null).build());
    }

    @SuppressWarnings("unused")
    public static Tracker tracker() {
        return tracker;
    }

    @SuppressWarnings("unused")
    public static GoogleAnalytics analytics() {
        return analytics;
    }
}
