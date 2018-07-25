/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.settings;

import android.content.Context;
import android.content.SharedPreferences;

import io.github.slupik.savepass.R;

public class MainPasswordSettings {
    private static final String LOCAL_PASSWORD = "local_pass";

    private Context mContext;

    public MainPasswordSettings(Context context) {
        mContext = context;
    }

    public String getPasswordAppHash(){
        return getSharedPreferences().getString(LOCAL_PASSWORD, "");
    }

    public boolean setPasswordAppHash(String hash) {
        return getSharedPreferences().edit().putString(LOCAL_PASSWORD, hash).commit();
    }

    private SharedPreferences getSharedPreferences(){
        return mContext.getSharedPreferences(
                mContext.getString(R.string.preference_local_password), Context.MODE_PRIVATE);
    }
}
