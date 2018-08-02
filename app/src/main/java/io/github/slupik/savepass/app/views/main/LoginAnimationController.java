/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views.main;

import android.animation.Animator;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.slupik.savepass.R;

class LoginAnimationController {
    @BindView(R.id.logged_in_animation_view)
    FrameLayout container;

    @BindView(R.id.acceptation_animation)
    LottieAnimationView animation;

    private ActionBar actionBar;
    private FragmentController viewController;
    private KeyboardController keyboardController;

    LoginAnimationController(FragmentController viewController, KeyboardController keyboardController, View root, ActionBar supportActionBar) {
        ButterKnife.bind(this, root);
        actionBar = supportActionBar;
        this.viewController = viewController;
        this.keyboardController = keyboardController;

        setupAnimation();
    }

    private void setupAnimation() {
        animation.setSpeed(0.75f);
        animation.setScale(1.25f);
    }

    void playAnimation(final Runnable callback){
        hideOtherViews();
        keyboardController.hideKeyboard(container);
        animation.playAnimation();
        animation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callback.run();
                        showLoggedInView();
                    }
                }, 300);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    void showLoggedInView() {
        viewController.hidePasswordFragment(true);
        viewController.hideListFragment(false);
        actionBar.show();
    }

    private void hideOtherViews() {
        if(actionBar!=null) {
            actionBar.hide();
        }
        viewController.hidePasswordFragment(true);
        viewController.hideListFragment(true);
        container.setVisibility(View.VISIBLE);
    }
}
