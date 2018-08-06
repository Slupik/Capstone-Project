/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.viewpass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.MyApplication;
import io.github.slupik.savepass.app.views.main.MainActivity;
import io.github.slupik.savepass.data.password.room.EntityPassword;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowPassFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ShowPassFragment extends Fragment {
    //In standard version
    @BindView(R.id.app_bar) @Nullable
    Toolbar appBar;
    @BindView(R.id.toolbar_title) @Nullable
    TextView title;
    @BindView(R.id.edit_pass) @Nullable
    ImageButton btnEditPass;

    //In tablet, landscape version
    @BindView(R.id.pass_info_core_container) @Nullable
    FrameLayout coreContainer;
    @BindView(R.id.fab_edit_pass) @Nullable
    FloatingActionButton fabEditPass;
    @BindView(R.id.pass_view_name) @Nullable
    TextView nameField;
    @BindView(R.id.name_info) @Nullable
    LinearLayout nameContainer;

    //In all versions
    @BindView(R.id.login_info)
    LinearLayout loginContainer;
    @BindView(R.id.login_field)
    TextInputEditText loginField;
    @BindView(R.id.copy_login)
    ImageButton btnCopyLogin;
    @BindView(R.id.password_field)
    TextInputEditText passField;
    @BindView(R.id.copy_password)
    ImageButton btnCopyPass;
    @BindView(R.id.short_desc_info)
    LinearLayout shortDescContainer;
    @BindView(R.id.short_desc)
    TextView shortDesc;
    @BindView(R.id.note_info)
    LinearLayout noteContainer;
    @BindView(R.id.note)
    TextView note;

    private EntityPassword mEntity;
    private OnFragmentInteractionListener mListener;

    public ShowPassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_pass, container, false);
        ButterKnife.bind(this, view);

        setupButtons();
        setupAppBar();

        return view;
    }
    private void setupAppBar() {
        if(null != appBar) {
            appBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.finish();
                }
            });
        }
    }

    @OnClick({R.id.fab_edit_pass, R.id.edit_pass}) @Optional
    public void onEditPass(){
        mListener.startEditActivity(mEntity);
    }

    private void setupButtons() {
        btnCopyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(getString(R.string.login), loginField.getText().toString());
            }
        });
        btnCopyPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(getString(R.string.pass), passField.getText().toString());
            }
        });
    }

    private void copyToClipboard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), getString(R.string.copied_successfully), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_while_copying), Toast.LENGTH_SHORT).show();
        }
    }

    public void loadData(EntityPassword entity) {
        mEntity=entity;
        setTitle(entity.getPasswordName());
        setLogin(entity.getLogin());
        try {
            setPassword(entity.getDecryptedPassword(getMyApplication().getAppPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setShortDesc(entity.getShortDesc());
        setNote(entity.getNotes());

        changeContainerVisibility();
    }

    private void changeContainerVisibility() {
        if(coreContainer!=null) {
            coreContainer.setVisibility(View.VISIBLE);
        }
    }

    public void setTitle(String title) {
        if (null != this.title) {
            this.title.setText(title);
        } else if (null != nameField) {
            nameField.setText(title);
        }
    }

    private void setLogin(String login){
        setFieldWithContainer(login, loginField, loginContainer);
    }

    private void setPassword(String password){
        passField.setText(password);
    }

    private void setShortDesc(String desc){
        setFieldWithContainer(desc, shortDesc, shortDescContainer);
    }

    private void setNote(String note){
        setFieldWithContainer(note, this.note, noteContainer);
    }

    private void setFieldWithContainer(String text, TextView field, LinearLayout container) {
        if(TextUtils.isEmpty(text)) {
            if(null != container) {
                container.setVisibility(View.GONE);
            }
        } else {
            if(null != field) {
                field.setText(text);
            }
            if(null != container) {
                container.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private Context getApplicationContext() {
        if(getContext()!=null) {
            return getContext().getApplicationContext();
        }
        return getContext();
    }

    public void onEntityDelete() {
        mEntity = null;
        if(coreContainer!=null) {
            coreContainer.setVisibility(View.GONE);
        }
    }

    private MyApplication getMyApplication(){
        return ((MyApplication) getActivity().getApplication());
    }

    public interface OnFragmentInteractionListener {
        void startEditActivity(EntityPassword entity);
        void finish();
    }
}
