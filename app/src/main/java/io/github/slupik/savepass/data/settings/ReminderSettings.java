/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.data.settings;

import android.content.Context;

public class ReminderSettings extends SharedPrefSettings {
    public ReminderSettings(Context context) {
        super(context);
    }

    public boolean isSending(){
        return getSharedPreferences().getBoolean(REMINDER_SENDING, true);
    }
    public boolean setSending(boolean isSending) {
        return getSharedPreferences().edit().putBoolean(REMINDER_SENDING, isSending).commit();
    }
}
