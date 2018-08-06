/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.model.server.backup.object;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import io.github.slupik.savepass.data.settings.ServerSettings;

public class BodyDownloadBackup {
    @SerializedName("user")
    private String user;
    @SerializedName("password")
    private String password;

    public BodyDownloadBackup(Context context) {
        user = new ServerSettings(context).getLogin();
        password = new ServerSettings(context).getPassword();
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
}
