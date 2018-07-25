/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.slupik.savepass.R;
import io.github.slupik.savepass.data.password.PasswordViewModel;
import io.github.slupik.savepass.data.password.room.EntityPassword;

public class MainActivity extends AppCompatActivity
        implements PassListFragment.OnFragmentInteractionListener,
        LocalPasswordFragment.OnFragmentInteractionListener, FragmentController, KeyboardController {
    private PasswordViewModel mPasswordViewModel;

    @BindView(R.id.acceptation_animation)
    LottieAnimationView checkmarkAnimation;

    private LoginAnimationController animationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupPasswordViewModel();
        animationController = new LoginAnimationController(
                this,
                this,
                findViewById(R.id.logged_in_animation_view),
                getSupportActionBar());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                startAnimationLoggedIn();
            }
        }, 6000);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
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

    @Override
    public void onLoginAttempt(String login, String password) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void hidePasswordFragment(boolean hide) {
        if(hide) {
            findViewById(R.id.login_fragment).setVisibility(View.GONE);
        } else {
            findViewById(R.id.login_fragment).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideListFragment(boolean hide) {
        if(hide) {
            findViewById(R.id.pass_list_fragment).setVisibility(View.GONE);
        } else {
            findViewById(R.id.pass_list_fragment).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void hideKeyboard(View view) {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
