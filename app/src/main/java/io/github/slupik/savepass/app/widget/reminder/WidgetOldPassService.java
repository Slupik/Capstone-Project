/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.widget.reminder;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.slupik.savepass.data.password.room.EntityPassword;

public class WidgetOldPassService extends RemoteViewsService {
    public static final String ARG_PASS_LIST = "pass-list";

    public WidgetOldPassService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String data = intent.getStringExtra(ARG_PASS_LIST);
        Type collectionType = new TypeToken<ArrayList<EntityPassword>>(){}.getType();
        List<EntityPassword> passwords = new ArrayList<>();
        try {
            passwords = new Gson().fromJson(data, collectionType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("UPDATEWIDGET", "onGetViewFactory: "+passwords.size());
//        List<EntityPassword> passwords = PasswordRepository.getInstance(getApplicationContext()).getPasswordsToRemind();
        return new WidgetOldPassListFactory(this.getApplicationContext(), passwords);
    }

}
