/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.settings;

import android.content.Context;
import android.content.SharedPreferences;

import io.github.slupik.savepass.R;

abstract class SharedPrefSettings extends SettingsValues {
    private Context mContext;

    public SharedPrefSettings(Context context) {
        mContext = context.getApplicationContext();
    }

    protected SharedPreferences getSharedPreferences(){
        return mContext.getSharedPreferences(
                mContext.getString(R.string.preference_local_password), Context.MODE_PRIVATE);
    }
}
