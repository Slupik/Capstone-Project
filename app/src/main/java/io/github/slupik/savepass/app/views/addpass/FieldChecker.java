/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.addpass;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.List;

import io.github.slupik.savepass.R;

class FieldChecker {
    private final List<FieldToCheck> itemsToCheck;
    private final Callback callback;
    private final Context context;

    FieldChecker(Context context, List<FieldToCheck> items, Callback callback) {
        this.context = context;
        itemsToCheck = items;
        this.callback = callback;

        startChecking();
    }

    private void startChecking() {
        if(isCompulsoryFieldsOk()){
            checkNextConfirmationField(-1);
        }
    }

    private boolean isCompulsoryFieldsOk() {
        for(FieldToCheck item:itemsToCheck) {
            if(!item.isNeedToConfirm()) {
                if(item.isEmpty()) {
                    showConfirmDialog(item);
                    return false;
                }
            }
        }
        return true;
    }

    private void checkNextConfirmationField(int lastChecked) {
        for(int i=lastChecked+1;i<itemsToCheck.size();i++) {
            FieldToCheck item = itemsToCheck.get(i);
            if(item.isNeedToConfirm()) {
                if(item.isEmpty()) {
                    showChoiceDialog(item, i);
                    return;
                }
            }
        }
        callback.onAdd();
    }

    private void showChoiceDialog(FieldToCheck item, final int actualId) {
        if(item.isContainsCustomDialog()) {
            item.getCustomDialog(new SpecialChecker.Callback() {
                @Override
                public void onConfirm() {
                    checkNextConfirmationField(actualId);
                }

                @Override
                public void onCancel() {
                    callback.onCancel();
                }
            }).show();
        } else {
            showChoiceDialog(item.getMessage(), actualId);
        }
    }

    private void showChoiceDialog(String message, final int actualId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                context.getString(R.string.dialog_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        checkNextConfirmationField(actualId);
                    }
                });

        builder.setNegativeButton(
                context.getString(R.string.dialog_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        callback.onCancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showConfirmDialog(FieldToCheck item) {
        if(item.isContainsCustomDialog()) {
            item.getCustomDialog(new SpecialChecker.Callback() {
                @Override
                public void onConfirm() {
                    callback.onCancel();
                }

                @Override
                public void onCancel() {
                    callback.onCancel();
                }
            }).show();
        } else {
            showConfirmDialog(item.getMessage());
        }
    }

    private void showConfirmDialog(String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getText(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onCancel();
                    }
                }).show();
    }

    interface Callback {
        void onAdd();
        void onCancel();
    }
}
