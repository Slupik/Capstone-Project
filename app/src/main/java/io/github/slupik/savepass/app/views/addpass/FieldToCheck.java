/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.addpass;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;

class FieldToCheck {
    private EditText field;
    private String message;
    private boolean needToConfirm;
    private SpecialChecker checker = null;

    FieldToCheck(EditText field, String message, boolean isNeedToConfirm) {
        this.field = field;
        this.message = message;
        this.needToConfirm = isNeedToConfirm;
    }

    FieldToCheck(EditText field, SpecialChecker checker, boolean isNeedToConfirm) {
        this.field = field;
        this.checker = checker;
        this.message="";
        this.needToConfirm = isNeedToConfirm;
    }

    void setSpecialCheck(SpecialChecker checker){
        this.checker = checker;
    }

    SpecialChecker getSpecialCheck(){
        return checker;
    }

    boolean isEmpty() {
        if(checker==null) {
            return isViewEmpty(field);
        } else {
            return !checker.isFieldOk(field);
        }
    }

    private static boolean isViewEmpty(EditText view) {
        return TextUtils.isEmpty(getTextFromView(view));
    }

    private static String getTextFromView(EditText view) {
        return view.getText().toString();
    }

    boolean isContainsCustomDialog() {
        return checker != null && checker.isContainsCustomDialog();
    }

    AlertDialog.Builder getCustomDialog(SpecialChecker.Callback callback) {
        if(checker!=null) {
            return checker.getCustomDialog(callback);
        } else {
            return null;
        }
    }

    boolean isNeedToConfirm() {
        return needToConfirm;
    }

    String getMessage() {
        return message;
    }
}
