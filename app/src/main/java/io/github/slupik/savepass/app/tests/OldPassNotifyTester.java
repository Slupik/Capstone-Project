/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.tests;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import io.github.slupik.savepass.app.notification.oldpass.OldPassNotifyManager;
import io.github.slupik.savepass.data.password.PasswordRepository;
import io.github.slupik.savepass.data.password.room.EntityPassword;

public final class OldPassNotifyTester {

    public static void sendNotify(final Context context, final int passId) {
        new AsyncTask<Void, Void, EntityPassword>() {
            @Override
            protected EntityPassword doInBackground(Void... voids) {
                List<EntityPassword> passwords = PasswordRepository.getInstance(context).getPasswords();
                return getPassById(passwords, passId);
            }

            @Override
            protected void onPostExecute(EntityPassword entity) {
                super.onPostExecute(entity);
                sendNotify(context, entity);
            }
        }.execute();
    }

    public static void sendNotify(Context context, EntityPassword entity) {
        if(null != entity) {
            new OldPassNotifyManager(context).sendNotifies(entity);
        } else {
            Log.d("OldPassNotifyTester", "Wrong pass id!");
        }
    }

    @Nullable
    private static EntityPassword getPassById(List<EntityPassword> passwords, int passId) {
        for(EntityPassword password:passwords) {
            if(password.getId()==passId) {
                return password;
            }
        }
        return null;
    }
}
