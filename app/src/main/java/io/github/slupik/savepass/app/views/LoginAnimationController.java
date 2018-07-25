/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.app.views;

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
    private FragmentController controller;

    LoginAnimationController(FragmentController controller, View root, ActionBar supportActionBar) {
        ButterKnife.bind(this, root);
        actionBar = supportActionBar;
        this.controller = controller;

        setupAnimation();
    }

    private void setupAnimation() {
        animation.setSpeed(0.75f);
        animation.setScale(1.25f);
    }

    void playAnimation(){
        hideOtherViews();
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
                        controller.hidePasswordFragment(true);
                        controller.hideListFragment(false);
                        actionBar.show();
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

    private void hideOtherViews() {
        if(actionBar!=null) {
            actionBar.hide();
        }
        controller.hidePasswordFragment(true);
        controller.hideListFragment(true);
        container.setVisibility(View.VISIBLE);
    }
}
