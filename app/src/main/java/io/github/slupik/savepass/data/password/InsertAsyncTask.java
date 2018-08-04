/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.password;

import io.github.slupik.savepass.data.password.room.EntityPassword;
import io.github.slupik.savepass.data.password.room.PasswordDAO;

class InsertAsyncTask extends DaoAsyncTask {

    InsertAsyncTask(PasswordDAO dao) {
        super(dao);
    }

    @Override
    protected void doBackgroundStuff(EntityPassword[] params) {
        for(EntityPassword password:params) {
            if(password.getLastUpdate()<=0) {
                password.setLastUpdate(System.currentTimeMillis());
            }
            if(password.getLastRemindTime()<=0) {
                password.setLastRemindTime(System.currentTimeMillis());
            }
        }
        getDao().insert(params);
    }
}
