/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.password;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

import io.github.slupik.savepass.data.password.room.EntityPassword;
import io.github.slupik.savepass.data.password.room.PasswordDAO;
import io.github.slupik.savepass.data.password.room.PasswordRoomDatabase;

class PasswordRepository {
    private static PasswordRepository sInstance;

    private PasswordDAO mPasswordDAO;
    private LiveData<List<EntityPassword>> mLivePasswords;

    private PasswordRepository(Application application) {
        PasswordRoomDatabase db = PasswordRoomDatabase.getDatabase(application);
        mPasswordDAO = db.passwordDAO();
        mLivePasswords = mPasswordDAO.getLiveDataOfAllPasswords();
    }

    //Singleton
    public static PasswordRepository getInstance(final Application database) {
        if (sInstance == null) {
            synchronized (PasswordRepository.class) {
                if (sInstance == null) {
                    sInstance = new PasswordRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<EntityPassword>> getLivePasswords() {
        return mLivePasswords;
    }

    public void insert(EntityPassword word) {
        new InsertAsyncTask(mPasswordDAO).execute(word);
    }
}
