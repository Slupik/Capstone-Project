/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.settings;

import android.content.Context;

public class ServerSettings extends SharedPrefSettings {

    public ServerSettings(Context context) {
        super(context);
    }

    public String getPassword(){
        return getSharedPreferences().getString(SERVER_PASSWORD, "");
    }
    public boolean setPassword(String pass) {
        return getSharedPreferences().edit().putString(SERVER_PASSWORD, pass).commit();
    }

    public String getLogin(){
        return getSharedPreferences().getString(SERVER_LOGIN, "");
    }
    public boolean setLogin(String login) {
        return getSharedPreferences().edit().putString(SERVER_LOGIN, login).commit();
    }

    public int getInterval(){
        return getSharedPreferences().getInt(SERVER_INTERVAL, -1);
    }
    public boolean setInterval(int interval) {
        return getSharedPreferences().edit().putInt(SERVER_INTERVAL, interval).commit();
    }

    public boolean isSyncingEnabled() {
        return getInterval()>0;
    }
}
