/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.password;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import io.github.slupik.savepass.app.MyApplication;
import io.github.slupik.savepass.data.password.room.EntityPassword;
import io.github.slupik.savepass.data.password.room.PasswordDAO;
import io.github.slupik.savepass.data.password.room.PasswordRoomDatabase;
import io.github.slupik.savepass.model.utils.Randomizer;

public class PasswordRepository {
    private static PasswordRepository sInstance;

    private PasswordDAO mPasswordDAO;
    private LiveData<List<EntityPassword>> mLivePasswords;

    private PasswordRepository(Context context) {
        PasswordRoomDatabase db = PasswordRoomDatabase.getDatabase(context);
        mPasswordDAO = db.passwordDAO();
        mLivePasswords = mPasswordDAO.getLiveDataOfAllPasswords();
    }

    //Singleton
    public static PasswordRepository getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (PasswordRepository.class) {
                if (sInstance == null) {
                    sInstance = new PasswordRepository(context);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<EntityPassword>> getLivePasswords() {
        return mLivePasswords;
    }

    public List<EntityPassword> getPasswords() {
        return mPasswordDAO.getAllPasswords();
    }

    public List<EntityPassword> getPasswordsToRemind() {
        return mPasswordDAO.getPasswordsToRemind(System.currentTimeMillis());
    }

    public void insert(EntityPassword... entity) {
        new InsertAsyncTask(mPasswordDAO).execute(entity);
    }

    public void generateExample(MyApplication application){
        insert(getExampleEntity(application));
    }

    public static EntityPassword getExampleEntity(MyApplication application){
        EntityPassword entity = new EntityPassword();
        try {
            entity.setAndEncryptPassword(application.getAppPassword(), "password"+Randomizer.randomStandardString(5));
        } catch (Exception e) {
            e.printStackTrace();
        }
        entity.setLogin("login"+Randomizer.randomStandardString(5));
        entity.setNotes("Notes"+Randomizer.randomStandardString(20));
        entity.setPasswordName("passName"+Randomizer.randomStandardString(5));
        entity.setShortDesc("shortDesc"+Randomizer.randomStandardString(10));
        entity.setWebAddress("https://github.com/");
        entity.setToSyncWithServer(true);
        entity.setRemindTimeInMilis(Randomizer.randomInteger(1, 5)*24*60*60*1000);
        entity.setLastRemindTime(System.currentTimeMillis()-Randomizer.randomInteger(0, 7)*24*60*60*1000);
        entity.setLastUpdate(System.currentTimeMillis()-Randomizer.randomInteger(0, 7)*24*60*60*1000);

        return entity;
    }

    public void update(EntityPassword... entity) {
        new UpdateAsyncTask(mPasswordDAO).execute(entity);
    }

    public void delete(EntityPassword... entity) {
        new DeleteAsyncTask(mPasswordDAO).execute(entity);
    }
}
