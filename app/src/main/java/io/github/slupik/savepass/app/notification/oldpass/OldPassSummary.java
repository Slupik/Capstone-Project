/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.notification.oldpass;

import android.content.Context;

import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.notification.base.NotifySummaryBase;

public class OldPassSummary extends NotifySummaryBase {
    private final static String GROUP_KEY = "io.github.slupik.savepass.PASS_CHANGE_REMIND";
    private final static int SUMMARY_ID = 0;

    protected OldPassSummary(Context context) {
        super(context);
    }

    @Override
    public int getSummaryId() {
        return SUMMARY_ID;
    }

    @Override
    protected String getGroupKey() {
        return GROUP_KEY;
    }

    @Override
    protected String getContent() {
        return getString(R.string.notify_reminder_summary_content);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.notify_reminder_summary_title);
    }
}
