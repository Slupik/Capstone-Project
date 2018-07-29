/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.addpass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.MyApplication;
import io.github.slupik.savepass.data.password.PasswordRepository;
import io.github.slupik.savepass.data.password.room.EntityPassword;

public class AddPassActivity extends AppCompatActivity {
    public static final String ARG_DATA = "data";
    public static final String ARG_RESULT_STATUS = "status";

    public static final int RESULT_STATUS_ADD = 0;
    public static final int RESULT_STATUS_DELETE = 1;

    @BindView(R.id.entity_address)
    EditText address;
    @BindView(R.id.entity_name)
    EditText name;
    @BindView(R.id.entity_notes)
    EditText notes;
    @BindView(R.id.entity_password)
    EditText password;
    @BindView(R.id.entity_short_desc)
    EditText shortDesc;
    @BindView(R.id.entity_user)
    EditText login;
    @BindView(R.id.entity_remind_time)
    EditText remindTime;

    @BindView(R.id.cbSyncWithServer)
    CheckBox syncWithServer;

    @BindView(R.id.cbIncludeLowerChar)
    CheckBox includeLowerChar;
    @BindView(R.id.cbIncludeNumbers)
    CheckBox includeNumbers;
    @BindView(R.id.cbIncludeSymbols)
    CheckBox includeSymbols;
    @BindView(R.id.cbIncludeUpperChar)
    CheckBox includeUpperChar;
    @BindView(R.id.pass_length)
    EditText passLength;

    @BindView(R.id.btnCancel)
    ImageButton btnCancel;
    @BindView(R.id.btn_delete)
    ImageButton btnDelete;
    @BindView(R.id.btn_save)
    ImageButton btnSave;
    @BindView(R.id.btnAdd)
    ImageButton btnAdd;

    private EntityPassword mEntity;
    private boolean mEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pass);
        ButterKnife.bind(this);
        loadDataFromBundle();
    }

    private void loadDataFromBundle() {
        String data = getIntent().getStringExtra(ARG_DATA);
        EntityPassword entity = new Gson().fromJson(data, EntityPassword.class);
        loadData(entity);
    }

    public void loadData(@Nullable EntityPassword entity) {
        mEntity = entity;
        mEditMode = entity!=null;

        if(mEditMode) {
            btnDelete.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);

            address.setText(entity.getWebAddress());
            name.setText(entity.getPasswordName());
            notes.setText(entity.getNotes());
            try {
                password.setText(entity.getDecryptedPassword(getLocalPassword()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            shortDesc.setText(entity.getShortDesc());
            login.setText(entity.getLogin());
            remindTime.setText(getParsedRemindTime(entity));
            syncWithServer.setChecked(entity.isToSyncWithServer());
        } else {
            btnDelete.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        }
    }

    private String getParsedRemindTime(EntityPassword entity) {
        return Long.toString(entity.getRemindTimeInMilis()/1000/60/60/24);
    }

    @OnClick(R.id.btn_delete)
    public void deleteThis(){
        PasswordRepository.getInstance(getApplication()).delete(mEntity);
        Intent data = new Intent();
        data.putExtra(ARG_RESULT_STATUS, RESULT_STATUS_DELETE);
        setResult(RESULT_OK, data);
        finish();
    }

    @OnClick(R.id.generatePassword)
    public void generatePassword(){
        String password = new PasswordGenerator(
                includeSymbols.isChecked(),
                includeNumbers.isChecked(),
                includeLowerChar.isChecked(),
                includeUpperChar.isChecked()
        ).generatePassword((int) getLongFromView(passLength));
        this.password.setText(password);
    }

    @OnClick(R.id.btnCancel)
    public void onCancel(){
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick({R.id.btnAdd, R.id.btn_save})
    public void onAddEntity(){
        new FieldChecker(this, getFieldsToCheck(), new FieldChecker.Callback() {
            @Override
            public void onAdd() {
                addEntityToDatabase();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void addEntityToDatabase() {
        try {
            EntityPassword entity = getCreatedEntity();
            if(mEditMode) {
                PasswordRepository.getInstance(getApplication()).update(entity);
            } else {
                PasswordRepository.getInstance(getApplication()).insert(entity);
            }
            Intent data = new Intent();
            data.putExtra(ARG_RESULT_STATUS, RESULT_STATUS_ADD);
            data.putExtra(ARG_DATA, new Gson().toJson(entity));
            setResult(RESULT_OK, data);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(R.string.unknow_error_with_password)
                    .setPositiveButton(getText(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    private EntityPassword getCreatedEntity() throws Exception {
        EntityPassword entity;
        if(mEditMode) {
            entity = mEntity;
        } else {
            entity = new EntityPassword();
        }
        entity.setAndEncryptPassword(
                getLocalPassword(),
                getTextFromView(password)
        );
        entity.setWebAddress(getTextFromView(address));
        entity.setShortDesc(getTextFromView(shortDesc));
        entity.setPasswordName(getTextFromView(name));
        entity.setNotes(getTextFromView(notes));
        entity.setLogin(getTextFromView(login));

        entity.setToSyncWithServer(syncWithServer.isChecked());
        entity.setRemindTimeInMilis(getLongFromView(remindTime)*24*60*60*1000);

        entity.setLastUpdate(-1);
        entity.setLastRemindTime(-1);
        return entity;
    }

    private String getLocalPassword() {
        return ((MyApplication) getApplication()).getAppPassword();
    }

    private static String getTextFromView(EditText view) {
        return view.getText().toString();
    }

    private static long getLongFromView(EditText view) {
        return Long.parseLong(view.getText().toString());
    }

    private boolean isViewEmpty(EditText view) {
        return TextUtils.isEmpty(getTextFromView(view));
    }

    private List<FieldToCheck> getFieldsToCheck(){
        final Context context = this;
        List<FieldToCheck> list = new ArrayList<>();
        list.add(new FieldToCheck(password, getString(R.string.dialog_want_empty_pass), true));
        list.add(new FieldToCheck(name, getString(R.string.dialog_error_empty_name), false));
        list.add(new FieldToCheck(address, getString(R.string.dialog_want_empty_address), true));
        list.add(new FieldToCheck(remindTime, new SpecialChecker() {
            private final int EMPTY_FIELD = 0;
            private final int TOO_MANY_DAYS = 1;
            private int errorCode = -1;

            @Override
            boolean isFieldOk(EditText field) {
                if(isViewEmpty(field)) {
                    errorCode = EMPTY_FIELD;
                    return false;
                }
                if(getLongFromView(field)>365) {
                    errorCode = TOO_MANY_DAYS;
                    return false;
                }
                return true;
            }

            @Override
            AlertDialog.Builder getCustomDialog(final Callback callback) {
                isFieldOk(remindTime);
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setPositiveButton(getText(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callback.onCancel();
                            }
                        });
                switch (errorCode) {
                    case EMPTY_FIELD: {
                        builder.setMessage(R.string.empty_reminder);
                        return builder;
                    }
                    case TOO_MANY_DAYS: {
                        builder.setMessage(R.string.too_many_days_in_reminder);
                        return builder;
                    }
                    default: {
                        builder.setMessage(R.string.unknow_reminder_field_error);
                        return builder;
                    }
                }
            }
        }, false));

        return list;
    }
}
