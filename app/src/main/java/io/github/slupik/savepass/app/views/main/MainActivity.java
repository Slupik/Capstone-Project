/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.MyApplication;
import io.github.slupik.savepass.app.views.addpass.AddPassActivity;
import io.github.slupik.savepass.app.views.viewpass.ShowPassActivity;
import io.github.slupik.savepass.data.password.PasswordViewModel;
import io.github.slupik.savepass.data.password.room.EntityPassword;
import io.github.slupik.savepass.model.cryptography.Cryptography;

public class MainActivity extends AppCompatActivity
        implements PassListFragment.OnFragmentInteractionListener,
        LocalPasswordFragment.OnFragmentInteractionListener, FragmentController, KeyboardController,
        PassListListener {
    private PasswordViewModel mPasswordViewModel;
    private PassListAdapter mListAdapter;
    private List<EntityPassword> mPasswords = new ArrayList<>();

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

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setupAdapter();
    }

    private void setupAdapter() {
        mListAdapter = new PassListAdapter(getMyApplication(), this);
        getListFragment().setAdapter(mListAdapter);
        loadNewPasswords(mPasswordViewModel.getLivePasswords().getValue());
    }

    private void setupPasswordViewModel() {
        mPasswordViewModel = ViewModelProviders.of(this).get(PasswordViewModel.class);
        mPasswordViewModel.getLivePasswords().observe(this, new Observer<List<EntityPassword>>() {
            @Override
            public void onChanged(@Nullable final List<EntityPassword> passwords) {
                loadNewPasswords(passwords);
            }
        });
    }

    private void loadNewPasswords(List<EntityPassword> values) {
        mPasswords = values;
        mListAdapter.setNewData(mPasswords);
    }

    @Override
    public void onLoginAttempt(String password) {
        if(new Cryptography(this).isValidMainPassword(password)){
            onSuccessLogin(password);
        } else {
            getLoginListener().onLoginFail();
        }
    }

    @Override
    public void onRegister(String password) {
        new Cryptography(this).setAndEncryptMainPassword(password);
        onSuccessLogin(password);
    }

    private void onSuccessLogin(String password){
        getLoginListener().onLoginSuccess();
        getMyApplication().setAppPassword(password);
        animationController.playAnimation();

//        PasswordRepository.getInstance(getMyApplication()).generateExample(getMyApplication());
    }

    @Override
    public void onListFragmentInteraction(EntityPassword item) {
        Intent newActivity = new Intent(getApplicationContext(), ShowPassActivity.class);
        newActivity.putExtra(ShowPassActivity.ARG_DATA, new Gson().toJson(item));
        startActivity(newActivity);
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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private LoginListener getLoginListener(){
        return getLoginFragment();
    }

    private LocalPasswordFragment getLoginFragment(){
        return ((LocalPasswordFragment) getSupportFragmentManager().findFragmentById(R.id.login_fragment));
    }

    private PassListFragment getListFragment(){
        return ((PassListFragment) getSupportFragmentManager().findFragmentById(R.id.pass_list_fragment));
    }

    private MyApplication getMyApplication(){
        return ((MyApplication) getApplication());
    }

    @Override
    public void addPassAction() {
        Intent intent = new Intent(this, AddPassActivity.class);
        startActivity(intent);
    }
}
