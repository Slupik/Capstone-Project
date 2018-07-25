/*
 * Copyright (c) 2018. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package io.github.slupik.savepass.model;

import android.content.Context;

import io.github.slupik.savepass.data.settings.MainPasswordSettings;

public class Cryptography {
//    private static final String SALT = "o.[[FnqN$r:C+8K.O-K^+)&kBsj4sd++m+F`Y+e+t_b{^~bj5C#m|aK+B%K8ekyS";
    private static final String BCRYPT_SALT = "$2a$10$RJoAVj/HO3ASEbrTDMmIFO";

    private Context mContext;

    public Cryptography(Context context) {
        mContext = context;
    }

    public boolean isValidMainPassword(String password){
        String realHash = new MainPasswordSettings(mContext).getPasswordAppHash();
        String checkingHash = getHashedString(password);
        return realHash.equals(checkingHash);
    }

    private static String getHashedString(String string){
        return BCrypt.hashpw(string, BCRYPT_SALT);
    }
}
