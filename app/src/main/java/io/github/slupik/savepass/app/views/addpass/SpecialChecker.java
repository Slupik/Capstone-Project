/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.addpass;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

abstract class SpecialChecker {
    abstract boolean isFieldOk(EditText field);

    boolean isContainsCustomDialog() {
        return (getCustomDialog(new Callback() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {

            }
        })!=null);
    }

    @Nullable
    abstract AlertDialog.Builder getCustomDialog(Callback callback);

    interface Callback {
        void onConfirm();
        void onCancel();
    }
}
