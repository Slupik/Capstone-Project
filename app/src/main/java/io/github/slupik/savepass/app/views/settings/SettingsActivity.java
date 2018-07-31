/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.background.DispatcherController;
import io.github.slupik.savepass.data.settings.ReminderSettings;
import io.github.slupik.savepass.data.settings.ServerSettings;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.entity_user_settings)
    EditText user;
    @BindView(R.id.entity_password)
    EditText password;
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
    }

    private void loadData() {
        password.setText(serverSettings.getPassword());
        user.setText(serverSettings.getLogin());
        setIntervalValue(serverSettings.getInterval());
        sendNotify.setChecked(reminderSettings.isSending());
    }

    @Override
    protected void onStop() {
        int lastInterval = serverSettings.getInterval();
        boolean lastIsSending = reminderSettings.isSending();

        serverSettings.setPassword(password.getText().toString());
        serverSettings.setLogin(user.getText().toString());
        serverSettings.setInterval(getInterval());
        reminderSettings.setSending(sendNotify.isChecked());

        if(serverSettings.getInterval()!=lastInterval) {
            DispatcherController.startSyncer(this, true);
        }
        if(reminderSettings.isSending()!=lastIsSending) {
            DispatcherController.startReminder(this, true);
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
}
