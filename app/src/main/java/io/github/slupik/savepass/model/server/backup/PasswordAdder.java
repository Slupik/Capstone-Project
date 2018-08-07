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

class PasswordAdder extends PasswordTask {

    PasswordAdder(Context context) {
        super(context);
    }

    @Override
    protected Void doInBackground(List<EntityPassword>... lists) {
        List<EntityPassword> downloadedList = lists[0];
        List<EntityPassword> savedList = PasswordRepository.getInstance(mContext).getPasswords();

        List<EntityPassword> toAdd = new ArrayList<>();
        for(EntityPassword downloaded:downloadedList) {
            EntityPassword saved = getIdenticalPasswordEntity(savedList, downloaded);
            if(saved==null) {
                toAdd.add(downloaded);
            }
        }

        PasswordRepository.getInstance(mContext).insert(toAdd.toArray(new EntityPassword[toAdd.size()]));
        return null;
    }
}
