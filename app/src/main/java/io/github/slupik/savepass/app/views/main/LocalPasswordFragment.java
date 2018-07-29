/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.main;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.slupik.savepass.R;
import io.github.slupik.savepass.model.cryptography.Cryptography;

/**
 * Activities that contain this fragment must implement the
 * {@link LocalPasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocalPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalPasswordFragment extends Fragment implements LoginListener {
    private OnFragmentInteractionListener mListener;
    private boolean register = true;

    @BindView(R.id.log_in_app)
    Button btnLogIn;

    @BindView(R.id.app_password)
    AppCompatEditText tvPassword;

    @BindView(R.id.app_retype_password)
    AppCompatEditText tvRetypePassword;

    @BindView(R.id.app_retype_password_container)
    TextInputLayout tvRetypePasswordContainer;

    public LocalPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PassListFragment.
     */
    public static LocalPasswordFragment newInstance() {
        LocalPasswordFragment fragment = new LocalPasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_password, container, false);
        ButterKnife.bind(this, view);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToLogIn();
            }
        });
        setupRegistration();
        return view;
    }

    private void setupRegistration() {
        register = !new Cryptography(getContext()).isMainPasswordSaved();
        if(register){
            tvRetypePasswordContainer.setVisibility(View.VISIBLE);
            btnLogIn.setText(getString(R.string.register));
        } else {
            tvRetypePasswordContainer.setVisibility(View.GONE);
            btnLogIn.setText(getString(R.string.log_in));
        }
    }

    private void tryToLogIn() {
        String password = tvPassword.getText().toString();
        if(register) {
            String retypePassword = tvRetypePassword.getText().toString();
            if (password.equals(retypePassword)) {
                mListener.onRegister(password);
            } else {
                dialogPasswordsNotTheSame();
            }
        } else {
            mListener.onLoginAttempt(password);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLoginSuccess() {}

    @Override
    public void onLoginFail() {
        showWrongPasswordDialog();
    }

    private void dialogPasswordsNotTheSame() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.main_password_not_same_dialog_header));
        alertDialog.setMessage(getString(R.string.main_password_not_same_dialog_message));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.main_password_not_same_dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void showWrongPasswordDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.wrong_main_password_dialog_header));
        alertDialog.setMessage(getString(R.string.wrong_main_password_dialog_message));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.wrong_main_password_dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onLoginAttempt(String password);
        void onRegister(String password);
    }
}
