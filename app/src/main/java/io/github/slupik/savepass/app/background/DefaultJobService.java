/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.background;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public abstract class DefaultJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchTask;

    @Override
    public boolean onStartJob(final JobParameters job) {

        mFetchTask = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                runAsyncTask(context);
                jobFinished(job, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job, false);
            }
        };

        mFetchTask.execute();
        return true;
    }

    protected abstract void runAsyncTask(Context context);

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mFetchTask != null) {
            mFetchTask.cancel(true);
        }
        return true;
    }
}
