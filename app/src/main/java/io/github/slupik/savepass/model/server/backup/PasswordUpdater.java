/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.model.server.backup;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.github.slupik.savepass.data.password.PasswordRepository;
import io.github.slupik.savepass.data.password.room.EntityPassword;

public class PasswordUpdater extends PasswordTask {

    PasswordUpdater(Context context) {
        super(context);
    }

    @Override
    protected Void doInBackground(List<EntityPassword>... lists) {
        List<EntityPassword> downloadedList = lists[0];
        List<EntityPassword> savedList = PasswordRepository.getInstance(mContext).getPasswords();

        List<EntityPassword> toUpdate = new ArrayList<>();
        for(EntityPassword downloaded:downloadedList) {
            EntityPassword saved = getIdenticalPasswordEntity(savedList, downloaded);
            if(isToSave(saved, downloaded)) {
                toUpdate.add(downloaded);
            }
        }

        PasswordRepository.getInstance(mContext).update(toUpdate.toArray(new EntityPassword[toUpdate.size()]));
        return null;
    }

    private static boolean isToSave(EntityPassword saved, EntityPassword downloaded) {
        return null == saved || (downloaded.getLastUpdate() > saved.getLastUpdate() && saved.isToSyncWithServer());
    }
}
