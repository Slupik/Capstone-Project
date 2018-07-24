/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import io.github.slupik.savepass.R;
import io.github.slupik.savepass.data.password.PasswordViewModel;
import io.github.slupik.savepass.data.password.room.EntityPassword;

public class MainPasswordActivity extends AppCompatActivity {
    private PasswordViewModel mPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_password);

        setupPasswordViewModel();
    }

    private void setupPasswordViewModel() {
        mPasswordViewModel = ViewModelProviders.of(this).get(PasswordViewModel.class);
        mPasswordViewModel.getLivePasswords().observe(this, new Observer<List<EntityPassword>>() {
            @Override
            public void onChanged(@Nullable final List<EntityPassword> passwords) {
                //Update something
            }
        });
    }
}
