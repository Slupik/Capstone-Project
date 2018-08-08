/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.notification.oldpass;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.github.slupik.savepass.app.notification.base.NotifyManagerBase;
import io.github.slupik.savepass.data.password.room.EntityPassword;

public class OldPassNotifyManager extends NotifyManagerBase<EntityPassword, OldPassNotify, OldPassSummary, OldPassChannel> {

    public OldPassNotifyManager(Context context) {
        super(context);
    }

    @Override
    public void sendNotifies(List<EntityPassword> values) {
        sendNotifies(values.toArray(new EntityPassword[values.size()]));
    }

    @Override
    public void sendNotifies(EntityPassword... values) {
        List<OldPassNotify> notifies = new ArrayList<>();
        for(EntityPassword pass:values) {
            notifies.add(new OldPassNotify(mContext, pass));
        }

        sendNotifies(new OldPassChannel(mContext),
                new OldPassSummary(mContext),
                notifies.toArray(new OldPassNotify[notifies.size()]));
    }
}
