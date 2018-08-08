/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.notification.oldpass;

import android.content.Context;

import io.github.slupik.savepass.app.notification.base.NotifyChannelBase;

public class OldPassChannel extends NotifyChannelBase {
    private final static String CHANNEL_ID = "pass-change-reminder";
    private final static String CHANNEL_NAME = "Password change reminder";
    private final static String CHANNEL_DESC = "Notifies about old passwords which should be changed.";

    protected OldPassChannel(Context context) {
        super(context);
    }

    @Override
    protected String getChannelId() {
        return CHANNEL_ID;
    }

    @Override
    protected String getChannelName() {
        return CHANNEL_NAME;
    }

    @Override
    protected String getChannelDesc() {
        return CHANNEL_DESC;
    }
}
