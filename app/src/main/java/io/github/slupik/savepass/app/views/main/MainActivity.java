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
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import io.github.slupik.savepass.app.views.settings.SettingsActivity;
import io.github.slupik.savepass.app.views.viewpass.ShowPassActivity;
import io.github.slupik.savepass.app.views.viewpass.ShowPassFragment;
import io.github.slupik.savepass.data.password.PasswordViewModel;
import io.github.slupik.savepass.data.password.room.EntityPassword;
import io.github.slupik.savepass.model.cryptography.Cryptography;

import static io.github.slupik.savepass.app.views.viewpass.ShowPassActivity.REQUEST_EDIT_PASSWORD_ENTITY;

public class MainActivity extends AppCompatActivity
        implements PassListFragment.OnFragmentInteractionListener,
        LocalPasswordFragment.OnFragmentInteractionListener, FragmentController, KeyboardController,
        PassListListener, ShowPassFragment.OnFragmentInteractionListener {
    public static final String ARG_DATA = "data";
    public static final String ARG_SOURCE = "source";

    public static final int ARG_VALUE_SOURCE_NOTIFY = 1;

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
        setupIfLoggedIn();
    }

    private void setupIfLoggedIn() {
        if(!TextUtils.isEmpty(getMyApplication().getAppPassword())) {
            animationController.showLoggedInView();
            String data = getIntent().getStringExtra(ARG_DATA);
            if(!TextUtils.isEmpty(data)) {
                EntityPassword pass = new Gson().fromJson(data, EntityPassword.class);
                ShowPassFragment fragment = getShowingFragment();
                if(null!=fragment) {
                    fragment.loadData(pass);
                }
            }
        }
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
        animationController.playAnimation(new Runnable() {
            @Override
            public void run() {
                if(getIntent().getIntExtra(ARG_SOURCE, -1)==ARG_VALUE_SOURCE_NOTIFY
                        && !TextUtils.isEmpty(getIntent().getStringExtra(ARG_DATA))){
                    Intent detailAct = new Intent(getApplicationContext(), AddPassActivity.class);
                    detailAct.putExtra(ShowPassActivity.ARG_DATA, getIntent().getStringExtra(ARG_DATA));
                    startActivity(detailAct);
                }
            }
        });

//        new OldPassNotify(this).sendNotifies(mPasswords.get(1), mPasswords.get(0), mPasswords.get(2));
//        PasswordRepository.getInstance(getMyApplication()).generateExample(getMyApplication());
    }

    @Override
    public void onListFragmentInteraction(EntityPassword item) {
        ShowPassFragment fragment = getShowingFragment();
        if(null!=fragment) {
            fragment.loadData(item);
        } else {
            Intent newActivity = new Intent(getApplicationContext(), ShowPassActivity.class);
            newActivity.putExtra(ShowPassActivity.ARG_DATA, new Gson().toJson(item));
            startActivity(newActivity);
        }
    }

    @Nullable
    private ShowPassFragment getShowingFragment() {
        if(findViewById(R.id.pass_info_fragment_atv_main)==null) {
            return null;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (ShowPassFragment)fragmentManager.findFragmentById(R.id.pass_info_fragment_atv_main);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pass_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void startEditActivity(EntityPassword entity) {
        Intent editAct = new Intent(getApplicationContext(), AddPassActivity.class);
        editAct.putExtra(AddPassActivity.ARG_DATA, new Gson().toJson(entity));
        startActivityForResult(editAct, REQUEST_EDIT_PASSWORD_ENTITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_EDIT_PASSWORD_ENTITY == requestCode) {
            if (resultCode == RESULT_OK && data!=null) {
                if(data.getIntExtra(AddPassActivity.ARG_RESULT_STATUS, -1) == AddPassActivity.RESULT_STATUS_DELETE) {
                    getShowingFragment().onEntityDelete();
                }
                if(data.getIntExtra(AddPassActivity.ARG_RESULT_STATUS, -1) == AddPassActivity.RESULT_STATUS_ADD) {
                    String entData = data.getStringExtra(AddPassActivity.ARG_DATA);
                    loadData(new Gson().fromJson(entData, EntityPassword.class));
                }
            }
        }
    }

    private void loadData(EntityPassword mEntity) {
        ShowPassFragment fragment = getShowingFragment();
        fragment.loadData(mEntity);
    }
}
