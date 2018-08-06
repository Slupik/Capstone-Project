/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.background.DispatcherController;
import io.github.slupik.savepass.app.online.backup.OnlineBackup;
import io.github.slupik.savepass.app.online.backup.ServerDefaultSettings;
import io.github.slupik.savepass.data.settings.ReminderSettings;
import io.github.slupik.savepass.data.settings.ServerSettings;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.entity_user_settings)
    EditText user;
    @BindView(R.id.entity_password)
    EditText password;
    @BindView(R.id.entity_base_url)
    EditText baseUrl;
    @BindView(R.id.tv_server_backup_send_url)
    TextView backupSendUrl;
    @BindView(R.id.tv_server_backup_download_url)
    TextView backupDownloadUrl;
    @BindView(R.id.sync_interval)
    Spinner interval;
    @BindView(R.id.send_notify)
    CheckBox sendNotify;

    private ServerSettings serverSettings;
    private ReminderSettings reminderSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        serverSettings = new ServerSettings(this);
        reminderSettings = new ReminderSettings(this);
        loadData();
        addBaseUrlChangeListener();
    }

    private void loadData() {
        password.setText(serverSettings.getPassword());
        user.setText(serverSettings.getLogin());
        baseUrl.setText(serverSettings.getBaseUrl());
        setIntervalValue(serverSettings.getInterval());
        sendNotify.setChecked(reminderSettings.isSending());
    }

    @Override
    protected void onStop() {
        int lastInterval = serverSettings.getInterval();
        boolean lastIsSending = reminderSettings.isSending();

        serverSettings.setPassword(password.getText().toString());
        serverSettings.setLogin(user.getText().toString());
        serverSettings.setBaseUrl(baseUrl.getText().toString());
        serverSettings.setInterval(getInterval());
        reminderSettings.setSending(sendNotify.isChecked());

        if(serverSettings.getInterval()!=lastInterval) {
            DispatcherController.switchSyncer(this, true);
        }
        if(reminderSettings.isSending()!=lastIsSending) {
            DispatcherController.switchReminder(this, true);
        }
        super.onStop();
    }

    private int getInterval() {
        int pos = interval.getSelectedItemPosition();
        String[] values = getResources().getStringArray(R.array.pref_sync_frequency_values);
        return Integer.valueOf(values[pos]);
    }

    public void setIntervalValue(int value) {
        String compareValue = getTitleForTime(value);
        int index = getItemSpinnerIndex(compareValue);
        interval.setSelection(index);
    }

    private String getTitleForTime(int oryg) {
        String[] values = getResources().getStringArray(R.array.pref_sync_frequency_values);
        String[] titles = getResources().getStringArray(R.array.pref_sync_frequency_titles);

        String parsedValue = Integer.toString(oryg);
        for(int i=0;i<values.length;i++) {
            if(values[i].equals(parsedValue)) {
                return titles[i];
            }
        }
        return "";
    }

    private int getItemSpinnerIndex(String myString){
        for (int i=0;i<interval.getCount();i++){
            if (interval.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    @SuppressLint("SetTextI18n")
    private void addBaseUrlChangeListener() {
        backupSendUrl.setText(baseUrl.getText().toString()+"/"+ ServerDefaultSettings.SENDING_URL);
        backupDownloadUrl.setText(baseUrl.getText().toString()+"/"+ ServerDefaultSettings.DOWNLOADING_URL);
        baseUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                backupSendUrl.setText(baseUrl.getText().toString()+"/"+ ServerDefaultSettings.SENDING_URL);
                backupDownloadUrl.setText(baseUrl.getText().toString()+"/"+ ServerDefaultSettings.DOWNLOADING_URL);
            }
        });
    }

    private AsyncTask<Void, Void, Void> syncTask = null;
    @OnClick(R.id.sync_now)
    void onSyncNow(){
        if(syncTask==null) {
            syncTask = new AsyncTask<Void, Void, Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {
                            Log.d("SYNCER", "bakcground");
                            Context context = getApplicationContext();
                            OnlineBackup.sendData(context);
                            OnlineBackup.saveData(context);
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            syncTask=null;
                        }
                    };
            syncTask.execute();
        }
    }
}
