/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.model.server.backup.object;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.github.slupik.savepass.data.password.room.EntityPassword;
import io.github.slupik.savepass.data.settings.ServerSettings;

public class BodySendBackup {
    @SerializedName("user")
    private String user;
    @SerializedName("password")
    private String password;
    @SerializedName("data")
    private List<EntityPassword> data;

    public BodySendBackup(Context context, List<EntityPassword> data) {
        user = new ServerSettings(context).getLogin();
        password = new ServerSettings(context).getPassword();
        this.data = data;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<EntityPassword> getData() {
        return data;
    }

    public void setData(List<EntityPassword> data) {
        this.data = data;
    }
}
