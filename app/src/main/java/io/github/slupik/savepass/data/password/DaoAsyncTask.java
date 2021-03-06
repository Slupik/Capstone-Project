/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.password;

import android.os.AsyncTask;

import io.github.slupik.savepass.data.password.room.EntityPassword;
import io.github.slupik.savepass.data.password.room.PasswordDAO;

abstract class DaoAsyncTask extends AsyncTask<EntityPassword, Void, Void> {

    private PasswordDAO mAsyncTaskDAO;

    DaoAsyncTask(PasswordDAO dao) {
        mAsyncTaskDAO = dao;
    }

    @Override
    protected Void doInBackground(final EntityPassword... params) {
        doBackgroundStuff(params);
        return null;
    }

    protected abstract void doBackgroundStuff(EntityPassword[] params);

    protected PasswordDAO getDao(){
        return mAsyncTaskDAO;
    }
}
