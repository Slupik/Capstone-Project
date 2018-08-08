/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.model.server.backup;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import io.github.slupik.savepass.data.password.room.EntityPassword;

abstract class PasswordTask extends AsyncTask<List<EntityPassword>, Void, Void> {

    final Context mContext;

    PasswordTask(Context context){
        super();
        mContext = context.getApplicationContext();

    }

    static EntityPassword getIdenticalPasswordEntity(List<EntityPassword> list, EntityPassword entity) {
        for(EntityPassword item:list) {
            if(item.getUniqueCode().equals(entity.getUniqueCode())) {
                return item;
            }
        }
        return null;
    }

    static EntityPassword getEntityWithoutLocalId(EntityPassword oryg) {
        EntityPassword cloned = new EntityPassword();
        cloned.setUniqueCode(oryg.getUniqueCode());
        cloned.setWebAddress(oryg.getWebAddress());
        cloned.setPasswordName(oryg.getPasswordName());
        cloned.setLogin(oryg.getLogin());
        cloned.setEncryptedPassword(oryg.getEncryptedPassword());
        cloned.setNotes(oryg.getNotes());
        cloned.setShortDesc(oryg.getShortDesc());
        cloned.setRemindTimeInMilis(oryg.getRemindTimeInMilis());
        cloned.setLastUpdate(oryg.getLastUpdate());
        cloned.setLastRemindTime(oryg.getLastRemindTime());
        cloned.setToSyncWithServer(oryg.isToSyncWithServer());
        return cloned;
    }
}
